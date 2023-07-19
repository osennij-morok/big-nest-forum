package net.gastipatis.bignestforum.application.exception;

public class InvalidCaptchaTokenException extends RuntimeException {

    public InvalidCaptchaTokenException() {
    }

    public InvalidCaptchaTokenException(String message) {
        super(message);
    }

    public InvalidCaptchaTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCaptchaTokenException(Throwable cause) {
        super(cause);
    }

    public InvalidCaptchaTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
