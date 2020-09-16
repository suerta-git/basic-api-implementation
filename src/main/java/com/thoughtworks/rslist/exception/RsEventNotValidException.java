package com.thoughtworks.rslist.exception;

public class RsEventNotValidException extends RuntimeException {
    private String message;

    public RsEventNotValidException(String message) {
        this.message = message;
    }

    public RsEventNotValidException() {
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
