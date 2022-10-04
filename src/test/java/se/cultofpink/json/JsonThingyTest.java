package se.cultofpink.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonThingyTest {
    private static final String simpleJson = "{\"hej\":\"hejhej\",\"obj\":{\"inner\":\"hej igen\"}}";
    private static final String jsonWithArray = "{\"hej\":\"hejsvej\",\"array\":[\"first\",\"second\",3]}";

    @Test
    public void conceptDeclaration() {
        assertEquals("hej", new JsonThingy("\"hej\"").get());
        assertEquals("hejhej", new JsonThingy(simpleJson).get("hej").get());
        assertEquals("hej igen", new JsonThingy(simpleJson).get("obj").get("inner").get());
        assertEquals("{\"inner\":\"hej igen\"}", new JsonThingy(simpleJson).get("obj").get());
        assertEquals("hejsvej", new JsonThingy(jsonWithArray).get("hej").get());
        assertEquals("second", new JsonThingy(jsonWithArray).get("array").get(1).get());
        assertEquals("[\"first\",\"second\",3]", new JsonThingy(jsonWithArray).get("array").get());
        assertEquals("3", new JsonThingy(jsonWithArray).get("array").get(2).get());

        assertNull(new JsonThingy(simpleJson).get("nonexistent").get());
        try {
            assertNull(new JsonThingy(simpleJson).get("nonexistent").get("nonexistent").get());
            fail("Should have thrown...");
        } catch (UnsupportedOperationException e) {
            //OK, expected
        }


        assertEquals(simpleJson, new JsonThingy(simpleJson).contain("contained").get("contained").get());
        assertEquals("{\"test\":\"fjuff\",\"add\":" + jsonWithArray + "}",
                new JsonThingy("{\"test\":\"fjuff\"}").set("add", new JsonThingy(jsonWithArray)).get());
        try {
            new JsonThingy("[]").set("hepp", new JsonThingy(simpleJson));
            fail("Should have thrown...");
        } catch (UnsupportedOperationException e) {
            //OK!
        }
    }

    @Test
    public void testSafeGet() {
        assertEquals("hejsvej", JsonThingy.safeGet(() -> new JsonThingy(jsonWithArray).get("hej")));
        assertEquals("second", JsonThingy.safeGet(() -> new JsonThingy(jsonWithArray).get("array").get(1)));
        assertEquals("[\"first\",\"second\",3]", JsonThingy.safeGet(() -> new JsonThingy(jsonWithArray).get("array")));
        assertEquals("3", JsonThingy.safeGet(() -> new JsonThingy(jsonWithArray).get("array").get(2)));

        assertNull(JsonThingy.safeGet(() -> new JsonThingy(simpleJson).get("nonexistent")));
        assertNull(JsonThingy.safeGet(() -> new JsonThingy(simpleJson).get("nonexistent").get("nonexistent").get("nonexistent").get("nonexistent")));
    }

    @Test
    public void testOrElse() {
        assertEquals("[\"first\",\"second\",3]", new JsonThingy(jsonWithArray).get("array").orElse("Hej!"));
        assertEquals("3", new JsonThingy(jsonWithArray).get("array").get(2).orElse("Hej!"));
        assertEquals("Hej!", new JsonThingy(simpleJson).get("nonexistent").orElse("Hej!"));


    }
}