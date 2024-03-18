package io.github.x45iq.jtube;

import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;

abstract class PlayerResponseParser {
    abstract JsonObject parse(Document document) throws ResponseParsingException;
}
