package com.java.nuven.application.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProblemDetail {
    private String title;
    private String status;
    private Object detail;
    private Instant timestamp;
}
