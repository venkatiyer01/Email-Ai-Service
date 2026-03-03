package io.emailassistant.ai.port;

import io.emailassistant.dto.EmailGenerateRequest;
import io.emailassistant.dto.EmailGenerateResponse;

/**
 * AI Port (Hexagonal Architecture)
 *
 * This defines the contract for any AI provider.
 * The Service layer depends only on this interface,
 * not on any specific AI implementation.
 */

public interface AiEmailGeneratorPort {
    EmailGenerateResponse generate(EmailGenerateRequest request);
}
