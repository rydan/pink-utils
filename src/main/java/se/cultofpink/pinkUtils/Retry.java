package se.cultofpink.pinkUtils;

import java.util.function.Supplier;

/**
 * Class to be able to call a method several times with delay, if method throws RuntimeException.
 */
public class Retry {

    private final static Logger logger = Logger.getLogger(Retry.class.getName());

    private final int retryDelay;
    private final int maxNrOfAttempts;

    public Retry(int maxNrOfAttempts, int retryDelay) {
        this.retryDelay = retryDelay;
        this.maxNrOfAttempts = maxNrOfAttempts;
    }

    public <T> T get(Supplier<T> function) throws InterruptedException {
        int attempt = 1;
        while(true) {
            try {
                return function.get();
            } catch (Exception e) {
                logger.warn("Exception while processing MQ queue: " + e);
                if (attempt >= maxNrOfAttempts) {
                    logger.warn("Tried " + maxNrOfAttempts + " times. Throwing exception...", e);
                    throw e;
                }
                attempt++;
            }
            try {
                Thread.sleep(retryDelay);
            } catch (InterruptedException e) {
                throw e;
            }
        }
    }

}
