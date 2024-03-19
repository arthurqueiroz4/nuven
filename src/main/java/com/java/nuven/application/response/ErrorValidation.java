package com.java.nuven.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorValidation {
    private String field;
    private String message;
}
