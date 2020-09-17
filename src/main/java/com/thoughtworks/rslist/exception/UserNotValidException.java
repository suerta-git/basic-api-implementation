package com.thoughtworks.rslist.exception;

public class UserNotValidException extends RuntimeException {
    private String message;

    public UserNotValidException(String message) {
        this.message = message;
    }

    public UserNotValidException() {
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
