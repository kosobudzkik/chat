package pl.schibsted.chat.errorhandling;

/**
 * Wrapper class for critical exceptions
 */
public class ChatAppException extends RuntimeException {
    public ChatAppException(String message, Throwable inner) {
        super(message, inner);
    }
    public ChatAppException(Throwable inner) {
        super(inner);
    }
}
