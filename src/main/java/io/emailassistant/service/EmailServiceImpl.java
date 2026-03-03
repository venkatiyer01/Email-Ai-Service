package io.emailassistant.service;

import io.emailassistant.ai.port.AiEmailGeneratorPort;
import io.emailassistant.dto.EmailGenerateRequest;
import io.emailassistant.dto.EmailGenerateResponse;
import io.emailassistant.entity.EmailRequestLog;
import io.emailassistant.repository.EmailRequestLogRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRequestLogRepository repository;
    private final AiEmailGeneratorPort aiEmailGeneratorPort;

    public EmailServiceImpl(
        EmailRequestLogRepository repository,
        AiEmailGeneratorPort aiEmailGeneratorPort
    ) {
        this.repository = repository;
        this.aiEmailGeneratorPort = aiEmailGeneratorPort;
    }
    @Override
    public EmailGenerateResponse generateEmail(EmailGenerateRequest request) {

        //Proof which adapter is being used
        System.out.println(">>> AiEmailGeneratorPort impl: " + aiEmailGeneratorPort.getClass().getName());

        long startTime = System.currentTimeMillis();

        try {

            // Dummy AI response for now
            EmailGenerateResponse response = aiEmailGeneratorPort.generate(request);

            long responseTime = System.currentTimeMillis() - startTime;

            // Save metadata to DB
            EmailRequestLog log = new EmailRequestLog();
            log.setEmailLength(request.getEmailContent().length());
            log.setTone(request.getTone());
            log.setDetectedIntent(response.getDetectedIntent());
            log.setConfidence(response.getConfidence());
            log.setResponseTimeMs(responseTime);
            log.setStatus("SUCCESS");

            repository.save(log);

            return response;

        } catch (Exception ex) {

            EmailRequestLog log = new EmailRequestLog();
            log.setEmailLength(request.getEmailContent().length());
            log.setTone(request.getTone());
            log.setStatus("FAILED");

            repository.save(log);

            throw ex;
        }
    }
}
