package se.cultofpink.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;

import java.io.IOException;

/** JsonThingy is a container for working with JsonStyle objects
 *  in a handy way, without having to convert to/from POJO:s
 */

public class JsonThingy {

    private final JsonNode root;
    private final ObjectMapper mapper;

    public JsonThingy(String jsonString) throws IOException {
        mapper = new ObjectMapper();
        root = mapper.readTree(jsonString);
    }

    private JsonThingy(JsonNode treeNode) {
        mapper = new ObjectMapper();
        this.root = treeNode;
    }

    /** get in object... */
    public JsonThingy get(String key) {
        return new JsonThingy(root.get(key));
    }

    /** get element in array */
    public JsonThingy get(int index) {
        return new JsonThingy(root.get(index));
    }

    /**return a new JsonThingy-object containing the current JsonThingie at the specified key */
    public JsonThingy contain(String key) {
        ObjectNode objectNode = mapper.createObjectNode();
        return new JsonThingy(objectNode.set(key, this.root));
    }

    public JsonThingy set(String key, JsonThingy value) throws IOException {
        return new JsonThingy(((ObjectNode)(new JsonThingy(this.get()).root)).set(key, value.root));
    }

    /** get the string value currently navigated to. Could be simple string, JSON object or JSON array */
    public String get() {
        if (root.isArray() || root.isObject()) {
            return root.toString();
        }
        return root.asText();
    }
}
