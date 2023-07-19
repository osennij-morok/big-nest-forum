package net.gastipatis.bignestforum.controller;

import lombok.Getter;

@Getter
public class APIError {

    private boolean error = true;
    private int status;
    private String message;

    public APIError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
