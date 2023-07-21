package net.gastipatis.bignestforum.application.exception;

public class SubordinationException extends RuntimeException {

    public SubordinationException() {
        super();
    }

    public SubordinationException(String message) {
        super(message);
    }

    public SubordinationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubordinationException(Throwable cause) {
        super(cause);
    }
}
