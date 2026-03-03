package io.emailassistant.ai.adapter;

import io.emailassistant.ai.port.AiEmailGeneratorPort;
import io.emailassistant.dto.EmailGenerateRequest;
import io.emailassistant.dto.EmailGenerateResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Dummy AI Adapter
 *
 * Loaded only when ai.provider=dummy
 */

@Component
@ConditionalOnProperty(
    name = "ai.provider",
    havingValue = "dummy",
    matchIfMissing = true
)


public class DummyAiEmailGeneratorAdapter implements AiEmailGeneratorPort{
    @Override
    public EmailGenerateResponse generate(EmailGenerateRequest request) {
        //Simulated AI processing logic
        return new EmailGenerateResponse(
            "GENERAL_REPLY",
            "This is a dummy AI response.",
            0.95
        );
    }
}
