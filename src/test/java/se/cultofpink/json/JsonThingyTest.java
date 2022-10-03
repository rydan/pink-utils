package se.cultofpink.json;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonThingyTest {
    private static final String simpleJson = "{\"hej\":\"hejhej\",\"obj\":{\"inner\":\"hej igen\"}}";
    private static final String jsonWithArray = "{\"hej\":\"hejsvej\",\"array\":[\"first\",\"second\",3]}";

    @Test
    public void conceptDeclaration() throws IOException {
        assertEquals("hejhej", new JsonThingy(simpleJson).get("hej").get());
        assertEquals("hej igen", new JsonThingy(simpleJson).get("obj").get("inner").get());
        assertEquals("{\"inner\":\"hej igen\"}", new JsonThingy(simpleJson).get("obj").get());
        assertEquals("hejsvej", new JsonThingy(jsonWithArray).get("hej").get());
        assertEquals("second", new JsonThingy(jsonWithArray).get("array").get(1).get());
        assertEquals("[\"first\",\"second\",3]", new JsonThingy(jsonWithArray).get("array").get());
        assertEquals("3", new JsonThingy(jsonWithArray).get("array").get(2).get());

        assertEquals(simpleJson, new JsonThingy(simpleJson).contain("contained").get("contained").get());
        assertEquals("{\"test\":\"fjuff\",\"add\":" + jsonWithArray + "}",
                new JsonThingy("{\"test\":\"fjuff\"}").set("add", new JsonThingy(jsonWithArray)).get());
    }
}