package com.thoughtworks.rslist.exception;

import lombok.Data;

@Data
public class Error {
    private String error;

    public Error(String error) {
        this.error = error;
    }
}
