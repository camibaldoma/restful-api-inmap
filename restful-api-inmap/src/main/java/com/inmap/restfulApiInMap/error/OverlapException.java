package com.inmap.restfulApiInMap.error;

public class OverlapException extends RuntimeException {
    public OverlapException(String message) {
        super(message);
    }
}
