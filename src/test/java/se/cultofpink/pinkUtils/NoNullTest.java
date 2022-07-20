package se.cultofpink.pinkUtils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class NoNullTest {

    @Test
    public void testGet() {

        TestA a = new TestA();
        Optional<String> value1 = NoNull.get(a::getTestString);
        assertFalse(value1.isPresent());

        TestB testB = new TestB();
        Optional<String> value2 = NoNull.get(() -> testB.getTestA().getTestString());
        //Internal NullPointer
        assertFalse(value2.isPresent());

        testB.setTestA(a);
        Optional<String> value3 = NoNull.get(() -> testB.getTestA().getTestString());
        //Fetched null
        assertFalse(value3.isPresent());

        a.setTestString("TestString");
        Optional<String> value4 = NoNull.get(() -> testB.getTestA().getTestString());
        //Fetched the value
        assertTrue(value4.isPresent());
        assertEquals("TestString", value4.get());
    }

    @Test
    public void testIncludingList() {
        ArrayList<TestA> list = new ArrayList<>();
        Optional<String> s = NoNull.get(() -> list.get(0).getTestString());
        //Fetched from empty list
        assertFalse(s.isPresent());

        TestA obj = new TestA();
        String testString = "Hepp!";
        obj.setTestString(testString);
        list.add(obj);
        s = NoNull.get(() -> list.get(0).getTestString());
        //Fetched correct object
        assertTrue(s.isPresent());
        assertEquals(testString, s.get());

        s = NoNull.get(() -> list.get(1).getTestString());
        //Fetch out of list bounds
        assertFalse(s.isPresent());
    }

    private static final class TestA {
        String testString;

        String getTestString() {
            return testString;
        }

        void setTestString(String testString) {
            this.testString = testString;
        }
    }

    private static final class TestB {
        TestA testA;

        TestA getTestA() {
            return testA;
        }

        void setTestA(TestA testA) {
            this.testA = testA;
        }
    }

}