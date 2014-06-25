package pl.schibsted.chat.exception;

/**
 * @author krzysztof.kosobudzki
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String msg) {
        super(msg);
    }
}
