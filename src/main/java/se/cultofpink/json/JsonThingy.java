package se.cultofpink.json;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/** JsonThingy is a container for working with JsonStyle objects
 *  in a handy way, without having to convert to/from POJO:s
 */

public class JsonThingy {

    private final JsonNode root;

    public JsonThingy(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        root = mapper.readTree(jsonString);
    }

    private JsonThingy(JsonNode treeNode) {
        this.root = treeNode;
    }

    /** get everything but array... */
    public JsonThingy get(String key) {
        return new JsonThingy(root.get(key));
    }

    /** get element in array */
    public JsonThingy get(int index) {
        return new JsonThingy(root.get(index));
    }

    /** get the string value currently navigated to. Could be simple string, JSON object or JSON array */
    public String get() {
        if (root.isArray() || root.isObject()) {
            return root.toString();
        }
        return root.asText();
    }

//    public Optional<String> get(String key) {
//        try {
//            return NoNull.get(() -> parsedJson.get(key).getAsString());
//        } catch (ClassCastException | UnsupportedOperationException e) {
//            return Optional.empty();
//        }
//    }

//    public JsonThingy getJsonThingy(String key) {
//        return new JsonThingy(parsedJson.getAsJsonObject(key));
//    }
}
