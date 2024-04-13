package io.github.x45iq.jtube;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code VideoParser} class represents tool to get {@code Video} object by url.
 *
 * @author Artem Shein
 */
public final class VideoParser {
    private static final Logger logger = LoggerFactory.getLogger(VideoParser.class);
    private static final Pattern AUDIO_FORMAT_REGEX = Pattern.compile("audio/(\\S+?);");
    private static final Pattern JS_FUNCTION_REGEX = Pattern.compile("([{; =])([a-zA-Z$_][a-zA-Z0-9$]{0,2})\\(");
    private static final Pattern JS_VARIABLES_REGEX = Pattern.compile("([{; =])([a-zA-Z$][a-zA-Z0-9$]{0,2})\\.([a-zA-Z$][a-zA-Z0-9$]{0,2})\\(");
    private static final Pattern SIGNATURE_DEC_FUNCTION_REGEX = Pattern.compile("(?:\\b|[^a-zA-Z0-9$])([a-zA-Z0-9$]{1,4})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
    private static final Pattern VIDEO_FORMAT_REGEX = Pattern.compile("video/(\\S+?);");
    private static final Pattern CODEC_REGEX = Pattern.compile("codecs=\"(\\S+?)\"");
    private static final Pattern HYBRID_MIME_TYPE_REGEX = Pattern.compile("codecs=\"(\\S+?),\\s*(\\S+?)\"");
    private static final Pattern ENCRYPTED_URL_REGEX = Pattern.compile("url=(\\S+)");
    private static final Pattern ENCRYPTED_URL_SIGNATURE_REGEX = Pattern.compile("s=(\\S+?)&");
    private static final Pattern DECIPHER_JS_FILE_REGEX = Pattern.compile("/s/player/(\\S+?)\\.js");
    private static final String DECRYPTION_DATA_FILE_NAME = "jtube.cache";
    private final CacheData cacheData;
    private static final PlayerResponseParser playerResponseParser = new PlayerResponseDefaultParser();

    /**
     * Constructor that allows {@code VideoParser} to save and load
     * decryption data from cache folder.
     * Uses a caching file with a name described as {@code DECRYPTION_DATA_FILE_NAME}
     *
     * @param cacheData data for caching
     */
    public VideoParser(CacheData cacheData) {
        this.cacheData = cacheData;
    }

    /**
     * Default constructor.
     */
    public VideoParser() {
        this.cacheData = null;
    }

    /**
     * Returns is url supported for parsing
     *
     * @param url video or shorts url
     * @return {@code true} if the URL is supported, otherwise {@code false}
     */
    public static boolean isUrlSupported(String url) {
        return UrlPatternsTool.isVideoUrl(url);
    }

    /**
     * Returns information about video located at the given url.
     *
     * @param url video or shorts url
     * @return {@code Video} object
     * @throws IOException              on Web error
     * @throws ResponseParsingException on error while parsing
     * @throws NoAccessException        if video has restricting access
     * @throws IllegalArgumentException on not supported url
     */
    public Video parse(String url) throws IOException, NoAccessException, ResponseParsingException, IllegalArgumentException {
        String id = UrlPatternsTool.getVideoUrlId(url).orElseThrow(() -> new IllegalArgumentException("Not supported url: " + url));
        Document page = NetTools.getPage(String.format("https://youtube.com/watch?v=%s", id));
        return parsePage(page);
    }

    private Video parsePage(Document page) throws NoAccessException, ResponseParsingException {
        assert page != null;
        JsonObject response = playerResponseParser.parse(page);
        checkVideoAvailable(response);
        List<VideoStreamingData> videoStreams = new ArrayList<>();
        List<HybridStreamingData> hybridStreams = new ArrayList<>();
        List<AudioStreamingData> audioStreams = new ArrayList<>();
        List<DecipherStreamParams> decipheredStreams = new ArrayList<>();
        List<Subtitles> subtitles = new ArrayList<>();

        VideoMeta meta = parseForMeta(response);
        parseHybridStreamingData(response, hybridStreams, decipheredStreams);
        parseAdaptiveStreamingData(response, videoStreams, audioStreams, decipheredStreams);
        parseSubtitles(response, subtitles);
        Video video = new Video(meta, videoStreams, audioStreams, hybridStreams, subtitles);
        if (decipheredStreams.isEmpty()) {
            return video;
        }
        try {
            decipherStreams(page.html(), decipheredStreams, cacheData);
        } catch (JTubeException | IOException e) {
            logger.error("Failed to decrypt links", e);
        }
        return video;
    }

    private static void parseSubtitles(JsonObject initialResponse, List<Subtitles> subtitles) {
        assert initialResponse != null;
        assert subtitles != null;
        if (initialResponse.has("captions")) {
            JsonArray array = initialResponse
                    .getAsJsonObject("captions")
                    .getAsJsonObject("playerCaptionsTracklistRenderer")
                    .getAsJsonArray("captionTracks");
            for (int i = 0; i < array.size(); i++) {
                JsonObject subtitle = array.get(i).getAsJsonObject();
                if (!subtitle.get("vssId").getAsString().contains("a.")) {
                    String baseUrl = subtitle.get("baseUrl").getAsString();
                    String lang = subtitle.getAsJsonObject("name").get("simpleText").getAsString();
                    String langCode = subtitle.get("languageCode").getAsString();
                    subtitles.add(new Subtitles(baseUrl, new Language(langCode, lang)));
                }
            }
        }
    }

    private static void decipherStreams(String pageHtml, List<DecipherStreamParams> decipheredStreams, CacheData cacheData) throws JTubeException, IOException {
        assert pageHtml != null;
        assert decipheredStreams != null;
        Matcher decipMatcher = DECIPHER_JS_FILE_REGEX.matcher(pageHtml);
        if (!decipMatcher.find()) {
            throw new JTubeException("jsFileName not found");
        }
        String jsFileName = decipMatcher.group(0);
        Optional<DecryptionData> decryptionDataOptional = CacheManager.getCacheObject(cacheData, DECRYPTION_DATA_FILE_NAME, DecryptionData.class)
                .filter(data -> jsFileName.equals(data.fileName));
        if (decryptionDataOptional.isPresent() && (System.currentTimeMillis() - decryptionDataOptional.get().created <= TimeUnit.HOURS.toMillis(1))) {
            try {
                decryptUrls(decipheredStreams, decryptionDataOptional.get());
                return;
            } catch (JTubeException e) {
                logger.error("", e);
            }
        }
        CacheManager.deleteFromCache(cacheData, DECRYPTION_DATA_FILE_NAME);
        DecryptionData decryptionData = parseDecryptionData(jsFileName);
        decryptUrls(decipheredStreams, decryptionData);
        CacheManager.cacheObject(cacheData, DECRYPTION_DATA_FILE_NAME, decryptionData);
    }

    private static void decryptUrls(List<DecipherStreamParams> decipherStreamParams, DecryptionData decryptionData) throws JTubeException {
        assert decipherStreamParams != null;
        assert decryptionData != null;
        StringBuilder stb = new StringBuilder(decryptionData.code + " function decipher(").append("){return ");
        for (int i = 0; i < decipherStreamParams.size() - 1; i++) {
            stb.append(String.format("%s('%s')+\"\\n\"+", decryptionData.funcName, decipherStreamParams.get(i).signature));
        }
        stb.append(String.format("%s('%s')", decryptionData.funcName, decipherStreamParams.get(decipherStreamParams.size() - 1).signature))
                .append("};");
        String answer = runJSFunc(stb.toString());
        String[] sigs = answer.split("\n");
        for (int i = 0; i < decipherStreamParams.size() && i < sigs.length; i++) {
            DecipherStreamParams dS = decipherStreamParams.get(i);
            String url = String.format("%s&sig=%s", dS.url, sigs[i]);
            dS.decipherReturn.accept(url);
        }
    }

    private static DecryptionData parseDecryptionData(String jsFileName) throws JTubeException, IOException {
        assert jsFileName != null;
        String jsFile = NetTools.getPage(String.format("https://youtube.com%s", jsFileName)).body().text();
        Matcher mat = SIGNATURE_DEC_FUNCTION_REGEX.matcher(jsFile);
        if (!mat.find()) {
            throw new JTubeException("SIGNATURE_DEC_FUNCTION_REGEX not found");
        }
        String funcName = mat.group(1);
        Pattern mainVariableRegex = Pattern.compile(String.format("(var |\\s|,|;) %s(=function\\((.{1,3})\\)\\{)", funcName.replace("$", "\\$")));
        Pattern mainFuncRegex = Pattern.compile(String.format("function %s(\\((.{1,3})\\)\\{)", funcName.replace("$", "\\$")));
        Matcher mainVariableMatch = mainVariableRegex.matcher(jsFile);
        Matcher mainFuncMatch = mainFuncRegex.matcher(jsFile);
        String mainFunc;
        int index;
        if (mainVariableMatch.find()) {
            mainFunc = String.format("var %s%s", funcName, mainVariableMatch.group(2));
            index = mainVariableMatch.end();
        } else if (mainFuncMatch.find()) {
            mainFunc = String.format("function %s%s", funcName, mainFuncMatch.group(2));
            index = mainFuncMatch.end();
        } else {
            throw new JTubeException("main func and variable not found");
        }
        mainFunc += parseFunc(jsFile, index);
        StringBuilder allFunctions = new StringBuilder(mainFunc);
        Matcher jsMatcher = JS_VARIABLES_REGEX.matcher(mainFunc);
        while (jsMatcher.find()) {
            String variableDef = "var " + jsMatcher.group(2) + "={";
            if (allFunctions.toString().contains(variableDef)) {
                continue;
            }
            index = jsFile.indexOf(variableDef) + variableDef.length();
            variableDef += parseFunc(jsFile, index);
            allFunctions.append(variableDef);
        }
        jsMatcher = JS_FUNCTION_REGEX.matcher(mainFunc);
        while (jsMatcher.find()) {
            String variableDef = String.format("function %s(", jsMatcher.group(2));
            if (allFunctions.toString().contains(variableDef)) {
                continue;
            }
            index = jsFile.indexOf(variableDef) + variableDef.length();
            variableDef += parseFunc(jsFile, index);
            allFunctions.append(variableDef);
        }
        return new DecryptionData(jsFileName, funcName, allFunctions.toString(), System.currentTimeMillis());
    }

    private static String parseFunc(String pageHtml, int index) {
        assert pageHtml != null;
        assert index >= 0;
        int count = 1;
        int lastIndex = index;
        while (count != 0) {
            switch (pageHtml.charAt(lastIndex)) {

                case '{':
                    count++;
                    break;
                case '}':
                    count--;
                    break;
            }
            lastIndex++;
        }
        return pageHtml.substring(index, lastIndex) + ";";
    }

    private static void parseAdaptiveStreamingData(JsonObject initialResponse, List<VideoStreamingData> videoStreams, List<AudioStreamingData> audioStreams, List<DecipherStreamParams> encStreams) {
        assert initialResponse != null;
        assert videoStreams != null;
        assert audioStreams != null;
        assert encStreams != null;
        JsonArray formats = initialResponse
                .getAsJsonObject("streamingData")
                .getAsJsonArray("adaptiveFormats");
        if(formats==null)return;
        for (int i = 0; i < formats.size(); i++) {
            JsonObject format = formats.get(i).getAsJsonObject();
            if (format.has("type") && "FORMAT_STREAM_TYPE_OTF".equals(format.get("type").getAsString())) {
                continue;
            }
            VideoTrack videoTrack = parseVideoTrack(format);
            AudioTrack audioTrack = parseAudioTrack(format);
            long contentLength = parseContentLength(format);
            if (videoTrack != null) {
                parseUrl(format, encStreams, (url) ->
                        videoStreams.add(new VideoStreamingData(url, contentLength, videoTrack)));
            } else if (audioTrack != null) {
                parseUrl(format, encStreams, (url) ->
                        audioStreams.add(new AudioStreamingData(url, contentLength, audioTrack)));
            } else {
                logger.info("streaming data has no video and audio track: " + format);
            }
        }
    }

    private static void parseHybridStreamingData(JsonObject initialResponse, List<HybridStreamingData> hybridStreams, List<DecipherStreamParams> encStreams) {
        assert initialResponse != null;
        assert hybridStreams != null;
        assert encStreams != null;
        JsonArray formats = initialResponse
                .getAsJsonObject("streamingData")
                .getAsJsonArray("formats");
        if(formats==null)return;
        for (int i = 0; i < formats.size(); i++) {
            JsonObject format = formats.get(i).getAsJsonObject();

            if (format.has("type") && "FORMAT_STREAM_TYPE_OTF".equals(format.get("type").getAsString())) {
                continue;
            }
            VideoTrack videoTrack = parseVideoTrack(format);
            PureAudioTrack audioTrack = parsePureAudioTrack(format);
            if (audioTrack == null || videoTrack == null) {
                logger.info("streaming data has no video or audio: " + format);
                continue;
            }
            AtomicLong contentLength = new AtomicLong(parseContentLength(format));
            parseUrl(format, encStreams, (url) ->
            {
                if (contentLength.get() == 0) {
                    try {
                        contentLength.set(NetTools.getContentLen(url));
                    } catch (IOException e) {
                        logger.error("couldn't get content len: " + url, e);
                        return;
                    }
                }
                hybridStreams.add(new HybridStreamingData(url, contentLength.get(), videoTrack, audioTrack));
            });
        }
    }

    private static AudioTrack parseAudioTrack(JsonObject format) {
        assert format != null;
        String mimeType = format.get("mimeType").getAsString();
        Matcher audioFormatMatch = AUDIO_FORMAT_REGEX.matcher(mimeType);
        if (!audioFormatMatch.find())
            return null;
        Optional<AudioFormat> audioFormatOptional = AudioFormat.fromString(audioFormatMatch.group(1));
        if (!audioFormatOptional.isPresent()) {
            return null;
        }
        AudioFormat audioFormat = audioFormatOptional.get();
        int bitrate = format.get("bitrate").getAsInt();
        int sampleRate = format.get("audioSampleRate").getAsInt();
        Matcher codecMatcher = CODEC_REGEX.matcher(mimeType);
        if (!codecMatcher.find()) {
            return null;
        }
        String codec = codecMatcher.group(1);
        Language language = null;
        if (format.has("audioTrack")) {
            String lang = format.getAsJsonObject("audioTrack").get("displayName").getAsString();
            String langCode = format.getAsJsonObject("audioTrack").get("id").getAsString();
            langCode = langCode.substring(0, langCode.indexOf("."));
            if (lang.contains("(")) {
                lang = lang.substring(0, lang.indexOf("(") - 1);
            }
            language = new Language(langCode, lang);
        }
        return new AudioTrack(bitrate, sampleRate, language, audioFormat, codec);
    }

    private static void parseUrl(JsonObject format, List<DecipherStreamParams> decipheredStreams, Consumer<String> decipherReturn) {
        assert format != null;
        assert decipheredStreams != null;
        assert decipherReturn != null;
        if (format.has("url")) {
            String url = format.get("url").getAsString();
            decipherReturn.accept(url);
        } else if (format.has("signatureCipher")) {
            try {
                String signatureCipher = URLDecoder.decode(format.get("signatureCipher").getAsString(), "UTF-8");
                Matcher urlMatch = ENCRYPTED_URL_REGEX.matcher(signatureCipher);
                Matcher sigMatch = ENCRYPTED_URL_SIGNATURE_REGEX.matcher(signatureCipher);
                if (urlMatch.find() && sigMatch.find()) {
                    decipheredStreams.add(new DecipherStreamParams(urlMatch.group(1), sigMatch.group(1), decipherReturn));
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("", e);
            }
        }
    }

    private static VideoTrack parseVideoTrack(JsonObject format) {
        assert format != null;
        String mimeType = format.get("mimeType").getAsString();
        Matcher videoFormatMatcher = VIDEO_FORMAT_REGEX.matcher(mimeType);
        if (!videoFormatMatcher.find())
            return null;
        Optional<VideoFormat> videoFormatOptional = VideoFormat.fromString(videoFormatMatcher.group(1));
        if (!videoFormatOptional.isPresent()) {
            return null;
        }
        VideoFormat videoFormat = videoFormatOptional.get();
        Matcher videoCodecMatcher = CODEC_REGEX.matcher(mimeType);
        Matcher hybridVideoCodecMatcher = HYBRID_MIME_TYPE_REGEX.matcher(mimeType);
        String codec;
        if (videoCodecMatcher.find()) {
            codec = videoCodecMatcher.group(1);
        } else if (hybridVideoCodecMatcher.find()) {
            codec = hybridVideoCodecMatcher.group(1);
        } else {
            return null;
        }
        if (codec.contains(".")) {
            codec = codec.substring(0, codec.indexOf("."));
        }
        String quality = format.get("qualityLabel").getAsString();
        Optional<VideoQuality> videoQualityOptional = VideoQuality.fromString(quality.substring(0, quality.indexOf("p") + 1));
        if (!videoQualityOptional.isPresent()) {
            return null;
        }
        VideoQuality videoQuality = videoQualityOptional.get();
        int width = format.get("width").getAsInt();
        int height = format.get("height").getAsInt();
        int fps = format.get("fps").getAsInt();

        return new VideoTrack(
                new VideoResolution(
                        width,
                        height,
                        videoQuality
                ),
                fps,
                videoFormat,
                codec
        );
    }

    private static PureAudioTrack parsePureAudioTrack(JsonObject format) {
        assert format != null;
        String mimeType = format.get("mimeType").getAsString();
        Matcher audioCodecMatcher = HYBRID_MIME_TYPE_REGEX.matcher(mimeType);
        if (!audioCodecMatcher.find())
            return null;
        String codec = audioCodecMatcher.group(2);
        int sampleRate = Integer.parseInt(format.get("audioSampleRate").getAsString());
        return new PureAudioTrack(codec, sampleRate);
    }

    private static long parseContentLength(JsonObject format) {
        assert format != null;
        if (format.has("contentLength")) {
            return format.get("contentLength").getAsLong();
        } else {
            return 0;
        }
    }

    private static VideoMeta parseForMeta(JsonObject initialResponse) {
        assert initialResponse != null;
        JsonObject videoDetails = initialResponse.getAsJsonObject("videoDetails");
        return new VideoMeta(
                videoDetails.get("title").getAsString(),
                videoDetails.get("author").getAsString(),
                videoDetails.get("videoId").getAsString(),
                videoDetails.get("shortDescription").getAsString()
        );
    }

    private static void checkVideoAvailable(JsonObject apiResponse) throws NoAccessException {
        assert apiResponse != null;
        String status = apiResponse.getAsJsonObject("playabilityStatus").get("status").getAsString();
        if (status.equals("OK")) {
            return;
        }
        throw new NoAccessException("status: " + status);
    }

    private static String runJSFunc(String javaScriptCode) throws JTubeException {
        assert javaScriptCode != null;
        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();
            rhino.evaluateString(scope, javaScriptCode, "JavaScript", 1, null);
            Object obj = scope.get("decipher", scope);
            if (obj instanceof Function) {
                Function jsFunc = (Function) obj;
                String res = Context.toString(jsFunc.call(rhino, scope, scope, new Object[]{}));
                if (!res.equals("undefined")) {
                    return res;
                }
            }
            throw new JTubeException("Bad js run");
        } finally {
            Context.exit();
        }

    }


    private static final class DecryptionData {
        private DecryptionData(String fileName, String funcName, String code, long created) {
            this.fileName = fileName;
            this.funcName = funcName;
            this.code = code;
            this.created = created;
        }

        private final String fileName;
        private final String funcName;
        private final String code;
        private final long created;
    }

    private static final class DecipherStreamParams {
        private final String url;
        private final String signature;
        private final Consumer<String> decipherReturn;

        private DecipherStreamParams(String url, String signature, Consumer<String> decipherReturn) {
            this.url = url;
            this.signature = signature;
            this.decipherReturn = decipherReturn;
        }
    }
}
