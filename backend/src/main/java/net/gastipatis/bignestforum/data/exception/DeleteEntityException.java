package net.gastipatis.bignestforum.data.exception;

public class DeleteEntityException extends RuntimeException {

    public DeleteEntityException() {

    }

    public DeleteEntityException(String message) {
        super(message);
    }

    public DeleteEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
