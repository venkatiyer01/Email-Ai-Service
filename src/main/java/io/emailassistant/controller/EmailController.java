package io.emailassistant.controller;

import io.emailassistant.api.ApiResponse;
import io.emailassistant.dto.EmailGenerateRequest;
import io.emailassistant.dto.EmailGenerateResponse;
import io.emailassistant.infra.RequestCorrelationFilter;
import io.emailassistant.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/generate")
    public ApiResponse<EmailGenerateResponse> generateEmail(
            @Valid @RequestBody EmailGenerateRequest body,
            HttpServletRequest request
    ) {
        EmailGenerateResponse result = emailService.generateEmail(body);

        String requestId = request.getHeader(RequestCorrelationFilter.HEADER_REQUEST_ID);

        return new ApiResponse<>(
                requestId,
                Instant.now(),
                result
        );
    }

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health(HttpServletRequest request) {
        String requestId = request.getHeader(RequestCorrelationFilter.HEADER_REQUEST_ID);

        return new ApiResponse<>(
                requestId,
                Instant.now(),
                Map.of("status", "UP")
        );
    }
}