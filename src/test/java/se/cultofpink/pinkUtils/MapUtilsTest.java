package se.cultofpink.pinkUtils;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapUtilsTest {
    @Test
    public void testAsMap() throws Exception {
        ArrayList<TestObject<String>> testObjects = new ArrayList<>();
        TestObject<String> testObj1 = new TestObject<>("Key1", "Data1");
        testObjects.add(testObj1);
        TestObject<String> testObj2 = new TestObject<>("Key2", "Data2");
        testObjects.add(testObj2);
        TestObject<String> testObj3 = new TestObject<>("Key3", "Data3");
        testObjects.add(testObj3);

        Map<String, TestObject<String>> map = MapUtils.asMap(testObjects, TestObject::getKey);
        assertEquals(3, map.size());
        assertEquals(testObj1, map.get("Key1"));
        assertEquals(testObj2, map.get("Key2"));
        assertEquals(testObj3, map.get("Key3"));

        ArrayList<TestObject<TestObject<String>>> anotherTestObjects = new ArrayList<>();
        TestObject<TestObject<String>> noterTest1 = new TestObject<>(testObj1, "Data1 for another test");
        TestObject<TestObject<String>> noterTest2 = new TestObject<>(testObj2, "Data2 for another test");
        anotherTestObjects.add(noterTest1);
        anotherTestObjects.add(noterTest2);

        Map<TestObject, TestObject<TestObject<String>>> anotherMap = MapUtils.asMap(anotherTestObjects, TestObject::getKey);
        assertEquals(2, anotherMap.size());
        assertEquals(noterTest1, anotherMap.get(testObj1));
        assertEquals(noterTest2, anotherMap.get(testObj2));
    }

    private static class TestObject<KEY> {
        KEY key;
        String data;

        TestObject(KEY key, String data) {
            this.key = key;
            this.data = data;
        }

        KEY getKey() {
            return key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestObject<?> that = (TestObject<?>) o;

            return key.equals(that.key) && data.equals(that.data);

        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + data.hashCode();
            return result;
        }
    }

}