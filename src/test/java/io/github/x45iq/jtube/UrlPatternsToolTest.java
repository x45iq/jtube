package io.github.x45iq.jtube;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UrlPatternsToolTest {

    @Test
    void isVideoUrl() {
        assertTrue(UrlPatternsTool.isVideoUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        assertTrue(UrlPatternsTool.isVideoUrl("https://youtu.be/izGwDsrQ1eQ?si=5EW2w3GsJvODhyMS"));
        assertTrue(UrlPatternsTool.isVideoUrl("https://youtu.be/izGwDsrQ1eQ"));
        assertTrue(UrlPatternsTool.isVideoUrl("https://youtube.com/shorts/8rm_vRLhQVI?si=kL0Wz8kAa9og1RlN"));
        assertTrue(UrlPatternsTool.isVideoUrl("https://youtube.com/shorts/8rm_vRLhQVI"));
        assertTrue(UrlPatternsTool.isVideoUrl("https://m.youtube.com/shorts/8rm_vRLhQVI"));
        assertTrue(UrlPatternsTool.isVideoUrl("https://m.youtube.com/8rm_vRLhQVI"));

        assertFalse(UrlPatternsTool.isVideoUrl("https://www.youtube.com/watch?v=dQw4w9WcQ"));
        assertFalse(UrlPatternsTool.isVideoUrl("https://www.youtube.com/watch?v=d&w4w9WcQ44"));
    }

    @Test
    void isPlaylistUrl() {
        assertTrue(UrlPatternsTool.isPlaylistUrl("https://youtube.com/playlist?list=PLax_UpcX2f5kHmFtXxwRlAKPLz0T8eaFU&si=7lVcx8HilcRr9kdk"));
        assertTrue(UrlPatternsTool.isPlaylistUrl("https://youtube.com/playlist?list=PLax_UpcX2f5kHmFtXxwRlAKPLz0T8eaFU"));
    }

    @Test
    void isChannelUrl() {
        assertTrue(UrlPatternsTool.isChannelUrl("https://www.youtube.com/channel/UCW6G95TBLCh5SdC-DHDSf5w"));
        assertTrue(UrlPatternsTool.isChannelUrl("https://youtube.com/@elvispresley?si=svJSNWN_o-H7lTqb"));
        assertTrue(UrlPatternsTool.isChannelUrl("https://youtube.com/@elvispresley"));
    }

    @Test
    void getVideoUrlId() {
        assertEquals(UrlPatternsTool.getVideoUrlId("https://www.youtube.com/watch?v=dQw4w9WgXcQ"), Optional.of("dQw4w9WgXcQ"));
        assertEquals(UrlPatternsTool.getVideoUrlId("https://youtu.be/izGwDsrQ1eQ?si=5EW2w3GsJvODhyMS"), Optional.of("izGwDsrQ1eQ"));
        assertEquals(UrlPatternsTool.getVideoUrlId("https://youtu.be/izGwDsrQ1eQ"), Optional.of("izGwDsrQ1eQ"));
        assertEquals(UrlPatternsTool.getVideoUrlId("https://youtube.com/shorts/8rm_vRLhQVI?si=kL0Wz8kAa9og1RlN"), Optional.of("8rm_vRLhQVI"));
    }

    @Test
    void getPlaylistUrlId() {
        assertEquals(UrlPatternsTool.getPlaylistUrlId("https://youtube.com/playlist?list=PLax_UpcX2f5kHmFtXxwRlAKPLz0T8eaFU&si=7lVcx8HilcRr9kdk"), Optional.of("PLax_UpcX2f5kHmFtXxwRlAKPLz0T8eaFU"));
        assertEquals(UrlPatternsTool.getPlaylistUrlId("https://youtube.com/playlist?list=PLax_UpcX2f5kHmFtXxwRlAKPLz0T8eaFU"), Optional.of("PLax_UpcX2f5kHmFtXxwRlAKPLz0T8eaFU"));
    }

    @Test
    void getChannelUrlId() {
        assertEquals(UrlPatternsTool.getChannelUrlId("https://www.youtube.com/channel/UCW6G95TBLCh5SdC-DHDSf5w"), Optional.of("UCW6G95TBLCh5SdC-DHDSf5w"));
        assertEquals(UrlPatternsTool.getChannelUrlId("https://youtube.com/@elvispresley?si=svJSNWN_o-H7lTqb"), Optional.of("@elvispresley"));
        assertEquals(UrlPatternsTool.getChannelUrlId("https://youtube.com/@elvispresley"), Optional.of("@elvispresley"));
    }
}