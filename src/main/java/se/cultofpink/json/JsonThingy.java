package se.cultofpink.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/** JsonThingy is a container for working with JsonStyle objects
 *  in a handy way, without having to convert to/from POJO:s
 */

public class JsonThingy {

    private final JsonNode root;
    private final ObjectMapper mapper;

    public JsonThingy(String jsonString) {
        JsonNode root1;
        mapper = new ObjectMapper();
        try {
            root1 = mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            root1 = mapper.nullNode();
        }
        root = root1;
    }

    private JsonThingy(JsonNode treeNode) {
        mapper = new ObjectMapper();
        this.root = treeNode;
    }

    /** get in object... */
    public JsonThingy get(String key) {
        if (!root.isObject()) {
            throw new UnsupportedOperationException("Can't get value for key on non-object: " + this.get());
        }
        JsonNode treeNode = root.get(key);
        return thingieFromNode(treeNode);
    }

    /** get element in array */
    public JsonThingy get(int index) {
        if (!root.isArray()) {
            throw new UnsupportedOperationException("Can't get position on non-array: " + this.get());
        }
        return thingieFromNode(root.get(index));
    }

    /**return a new JsonThingy-object containing the current JsonThingie at the specified key */
    public JsonThingy contain(String key) {
        ObjectNode objectNode = mapper.createObjectNode();
        return new JsonThingy(objectNode.set(key, this.root));
    }

    public JsonThingy set(String key, JsonThingy value) {
        if (!root.isObject()) {
            throw new UnsupportedOperationException("Can't set on non-object: " + this.get());
        }
        ((ObjectNode) this.root).set(key, value.root);
        return this;
    }

    /** get the string value currently navigated to. Could be simple string, JSON object or JSON array */
    public String get() {
        if (root.isArray() || root.isObject()) {
            return root.toString();
        } else if (root.isNull()) {
            return null;
        }
        return root.asText();
    }

    public String orElse(String s) {
        return Objects.requireNonNullElse(this.get(), s);
    }

    public static Optional<JsonThingy> safe(Supplier<JsonThingy> f) {
        try {
            return Optional.of(f.get());
        } catch (UnsupportedOperationException e) {
            return Optional.empty();
        }
    }

    public static String safeGet(Supplier<JsonThingy> f) {
        try {
            return f.get().get();
        } catch (UnsupportedOperationException e) {
            return null;
        }
    }

    private JsonThingy thingieFromNode(JsonNode treeNode) {
        if (null != treeNode) {
            return new JsonThingy(treeNode);
        }
        return new JsonThingy(mapper.nullNode());
    }

}
