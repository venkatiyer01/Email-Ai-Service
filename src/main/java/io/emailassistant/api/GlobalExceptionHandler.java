package io.emailassistant.api;

import io.emailassistant.ai.exception.AiProviderException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Global Exception Handler
 *
 * Handles all application-wide exceptions in a structured manner.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors (DTO validation failures)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String requestId = request.getHeader("X-Request-Id");

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ApiResponse<>(
                requestId,
                Instant.now(),
                "VALIDATION_ERROR: " + errorMessage
        );
    }

    /**
     * Handle missing or invalid JSON body
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleUnreadableMessage(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {

        String requestId = request.getHeader("X-Request-Id");

        return new ApiResponse<>(
                requestId,
                Instant.now(),
                "VALIDATION_ERROR: Request body is missing or invalid JSON"
        );
    }

    /**
     * Handle AI Provider failures
     */
    @ExceptionHandler(AiProviderException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResponse<String> handleAiProviderException(
            AiProviderException ex,
            HttpServletRequest request
    ) {

        String requestId = request.getHeader("X-Request-Id");

        return new ApiResponse<>(
                requestId,
                Instant.now(),
                "AI_PROVIDER_ERROR: AI service temporarily unavailable"
        );
    }

    /**
     * Handle all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {

        String requestId = request.getHeader("X-Request-Id");

        return new ApiResponse<>(
                requestId,
                Instant.now(),
                "INTERNAL_SERVER_ERROR: Unexpected error occurred"
        );
    }
}