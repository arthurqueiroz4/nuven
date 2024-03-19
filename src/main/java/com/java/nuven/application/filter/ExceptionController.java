package com.java.nuven.application.filter;

import com.java.nuven.application.response.ErrorValidation;
import com.java.nuven.application.response.ProblemDetail;
import com.java.nuven.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Objects;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomainException(DomainException e) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title(e.getErrorCode().name())
                .status(e.getStatus().toString())
                .detail(e.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(e.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodValidationException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Validation Error")
                .status(HttpStatus.BAD_REQUEST.toString())
                .detail( e.getBindingResult().getFieldErrors().stream().map(
                        error -> new ErrorValidation(error.getField(), error.getDefaultMessage())
                ))
                .build();
        return ResponseEntity.status(400).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Parse Error")
                .status(HttpStatus.BAD_REQUEST.toString())
                .detail("The parameter " + e.getName() + " must be of type " + Objects.requireNonNull(e.getRequiredType()).getSimpleName())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(400).body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Parse Error")
                .status(HttpStatus.BAD_REQUEST.toString())
                .detail("The request body is invalid")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(400).body(problemDetail);
    }
}
