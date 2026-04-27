package com.ecommerce.product_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.warn("Resource Not Found - Path {}, message: {}", request.getDescription(false), ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Resource not found");
        problemDetail.setType(URI.create("https://www.ecommerce.com/404"));
        Map<String, Object> map = Map.of("Timestamp", Instant.now(),
                "Resource", ex.getResourceName(),
                "Field", ex.getFieldName(),
                "Value", ex.getFieldValue());
        problemDetail.setProperties(map);

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "La valdiación falló en uno o más campos");

        problemDetail.setTitle("Failed validation");
        problemDetail.setType(URI.create("https://www.ecommerce.com/error-validation"));
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        problemDetail.setProperties(Map.of("Errors", errors));

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, WebRequest request) {
        log.error("Ocurrió un error inesperado {}: {}", request.getDescription(false), ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado");

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://www.ecommerce.com/internal-server-error"));

        problemDetail.setProperties(Map.of("Timestamp", Instant.now()));

        return problemDetail;
    }
}
