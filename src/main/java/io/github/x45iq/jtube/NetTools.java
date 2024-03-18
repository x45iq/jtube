package io.github.x45iq.jtube;

import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class NetTools {
    private static final Logger logger = LoggerFactory.getLogger(NetTools.class);

    private NetTools() {

    }

    static Document getPage(String url) throws IOException {
        assert url != null;
        return Jsoup
                .connect(url)
                .userAgent(RandomUserAgent.create())
                .get();
    }

    static String postApi(String url, JsonObject post) throws IOException {
        assert url != null;
        assert post != null;
        return Jsoup.connect(url)
                .userAgent(RandomUserAgent.create())
                .ignoreContentType(true)
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .requestBody(post.toString())
                .post()
                .body().text();

    }

    static long getContentLen(String url) throws IOException {
        assert url != null;
        String len = Jsoup.connect(url)
                .method(Connection.Method.HEAD)
                .userAgent(RandomUserAgent.create())
                .ignoreContentType(true)
                .execute().header("Content-Length");
        return Long.parseLong(len == null ? "0" : len);
    }
}
