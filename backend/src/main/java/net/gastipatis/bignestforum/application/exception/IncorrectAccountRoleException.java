package net.gastipatis.bignestforum.application.exception;

public class IncorrectAccountRoleException extends RuntimeException {

    public IncorrectAccountRoleException() {
        super();
    }

    public IncorrectAccountRoleException(String message) {
        super(message);
    }

    public IncorrectAccountRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectAccountRoleException(Throwable cause) {
        super(cause);
    }
}
