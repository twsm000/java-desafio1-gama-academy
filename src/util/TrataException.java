package util;

import java.util.Objects;

public class TrataException extends Exception {
    public TrataException(String message) {
        super(message);
    }

    public static void raiseExceptionIsNullOrBlank(String value, String message) throws TrataException {
        if (Objects.requireNonNullElse(value, "").isBlank()) {
            throw new TrataException(message);
        }
    }

    @FunctionalInterface
    public interface TrataExceptionConsumer {
        void execute() throws TrataException;
    }

    public static void tryAndDisplayError(TrataExceptionConsumer checker) {
        try {
            checker.execute();
        } catch (TrataException e) {
            System.err.println(e.getMessage());
        }
    }
}
