package se.cultofpink.pinkUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetryTest {

    @Test
    void testSimpleGet() throws InterruptedException {
        String result = new Retry(1, 10000).get(() -> "Hello world!");
        assertEquals("Hello world!", result);
    }

    @Test
    void testZeroGet() throws InterruptedException {
        //Should be allowed and still work, if confused with number or retries...
        String result = new Retry(0, 1000).get(() -> "Hello world!");
        assertEquals("Hello world!", result);
    }

    @Test
    void testGetWithSingleFailure() throws InterruptedException {
        //Should be allowed and still work, if confused with number or retries...
        Tester tester = new Tester(1);
        String result = new Retry(2, 100).get(() -> tester.getString("Hello world!"));
        assertEquals("Hello world!", result);
        assertEquals(2, tester.getAttempts());
    }

    @Test
    void testFailSingleAttempt() throws InterruptedException {
        //Should be allowed and still work, if confused with number or retries...
        Tester tester = new Tester(1);
        assertThrows(RuntimeException.class, () -> new Retry(1, 100).get(() -> tester.getString("Hello world!")));
        assertEquals(1, tester.getAttempts());
    }

    @Test
    void testGetWithMultipleFailure() throws InterruptedException {
        //Should be allowed and still work, if confused with number or retries...
        Tester tester = new Tester(5);
        String result = new Retry(10, 100).get(() -> tester.getString("Hello world!"));
        assertEquals("Hello world!", result);
        assertEquals(6, tester.getAttempts());
    }

    @Test
    void testFailAfterMultipleAttempt() throws InterruptedException {
        //Should be allowed and still work, if confused with number or retries...
        Tester tester = new Tester(5);
        assertThrows(RuntimeException.class, () -> new Retry(5, 100).get(() -> tester.getString("Hello world!")));
        assertEquals(5, tester.getAttempts());
    }

    @Test
    void testInterrupt() throws InterruptedException {
        Tester tester = new Tester(47);
        Thread runner = new Thread(() -> assertThrows(InterruptedException.class, () -> new Retry(50, 100).get(() -> tester.getString("Shouldn't happen..."))));
        runner.start();
        Thread.sleep(500);
        runner.interrupt();
        runner.join();
        assertTrue(40 > tester.getAttempts());
        assertTrue(1 < tester.getAttempts());
    }

    private static class Tester {
        int attempts = 0;
        final int timesToThrow;

        private Tester(int timesToThrow) {
            this.timesToThrow = timesToThrow;
        }

        public String getString(String s) {
            attempts++;
            if (attempts > timesToThrow) {
                return s;
            }
            throw new RuntimeException("Computer says no.");
        }

        public int getAttempts() {
            return attempts;
        }
    }

}
