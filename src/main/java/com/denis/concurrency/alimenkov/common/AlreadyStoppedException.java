package com.denis.concurrency.alimenkov.common;

public class AlreadyStoppedException extends RuntimeException {

    public AlreadyStoppedException(String message) {
        super(message);
    }
}
