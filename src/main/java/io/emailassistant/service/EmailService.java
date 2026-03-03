package io.emailassistant.service;

import io.emailassistant.dto.EmailGenerateRequest;
import io.emailassistant.dto.EmailGenerateResponse;

public interface EmailService {

    EmailGenerateResponse generateEmail(EmailGenerateRequest request);
}
