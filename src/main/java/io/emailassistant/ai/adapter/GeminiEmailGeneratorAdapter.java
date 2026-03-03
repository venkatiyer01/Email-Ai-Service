package io.emailassistant.ai.adapter;

import io.emailassistant.ai.exception.AiProviderException;
import io.emailassistant.ai.port.AiEmailGeneratorPort;
import io.emailassistant.dto.EmailGenerateRequest;
import io.emailassistant.dto.EmailGenerateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "gemini")
public class GeminiEmailGeneratorAdapter implements AiEmailGeneratorPort {

    private static final Logger log =
            LoggerFactory.getLogger(GeminiEmailGeneratorAdapter.class);

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String baseUrl;

    @Value("${gemini.model}")
    private String model;

    public GeminiEmailGeneratorAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public EmailGenerateResponse generate(EmailGenerateRequest request) {

        log.debug("Gemini adapter executing with model={}", model);

        try {

            String prompt =
                    "You are an assistant that writes high-quality email replies.\n" +
                    "Write a reply email body ONLY.\n" +
                    "Rules:\n" +
                    "1) Do NOT add a subject line.\n" +
                    "2) Do NOT use markdown.\n" +
                    "3) Keep it concise (4-8 sentences).\n" +
                    "4) Use the requested tone.\n" +
                    "5) Address the sender by name ONLY if explicitly present.\n" +
                    "6) If sender name is unclear, do NOT guess.\n" +
                    "7) If meeting time/date is mentioned, confirm it.\n\n" +
                    "Tone: " + request.getTone() + "\n" +
                    "Email to reply to:\n" +
                    request.getEmailContent();

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of(
                                    "role", "user",
                                    "parts", List.of(Map.of("text", prompt))
                            )
                    )
            );

            String url = baseUrl + "/models/" + model + ":generateContent?key=" + apiKey;

            Map<?, ?> response = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(8));   // ✅ Timeout protection

            if (response == null) {
                throw new AiProviderException("Empty response from Gemini");
            }

            List<?> candidates = (List<?>) response.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                throw new AiProviderException("No candidates returned from Gemini");
            }

            Map<?, ?> c0 = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) c0.get("content");
            List<?> parts = (List<?>) content.get("parts");

            StringBuilder sb = new StringBuilder();
            for (Object partObj : parts) {
                Map<?, ?> part = (Map<?, ?>) partObj;
                Object t = part.get("text");
                if (t != null) {
                    sb.append(t.toString());
                }
            }

            String text = sb.toString()
                    .replaceAll("\\r?\\n{2,}", "\n")
                    .replaceAll("\\s{2,}", " ")
                    .trim();

            if (text.isBlank()) {
                throw new AiProviderException("Gemini returned empty text");
            }

            log.info("Gemini generation successful. chars={}", text.length());

            return new EmailGenerateResponse("AI_GENERATED", text, 0.99);

        } catch (WebClientResponseException ex) {

            log.error("Gemini HTTP error status={} body={}",
                    ex.getStatusCode(),
                    ex.getResponseBodyAsString());

            throw new AiProviderException(
                    "Gemini HTTP Error: " + ex.getStatusCode(),
                    ex
            );

        } catch (Exception ex) {

            // Detect timeout from block(Duration)
            if (ex.getMessage() != null &&
                ex.getMessage().contains("Timeout on blocking read")) {

                log.error("Gemini request timed out after 8 seconds");

                throw new AiProviderException(
                        "Gemini request timed out after 8 seconds",
                        ex
                );
            }

            log.error("Gemini API call failed", ex);

            throw new AiProviderException(
                    "Gemini API call failed: " + ex.getMessage(),
                    ex
            );
        }
    }
}