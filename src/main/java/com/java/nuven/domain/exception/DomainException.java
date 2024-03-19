package com.java.nuven.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

    private ErrorCode errorCode;
    private HttpStatus status;

    public DomainException(ErrorCode errorCode, HttpStatus status) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.status = status;
    }
}