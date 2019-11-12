package com.learn.java.exception;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends Exception {

    private final ErrorCode errorCode;

    public ObjectNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
