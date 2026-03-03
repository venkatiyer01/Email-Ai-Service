package io.emailassistant.ai.adapter;

import io.emailassistant.ai.exception.AiProviderException;
import io.emailassistant.ai.port.AiEmailGeneratorPort;
import io.emailassistant.dto.EmailGenerateRequest;
import io.emailassistant.dto.EmailGenerateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(
        name = "ai.provider",
        havingValue = "openai"
)
public class OpenAiEmailGeneratorAdapter implements AiEmailGeneratorPort {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    public OpenAiEmailGeneratorAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public EmailGenerateResponse generate(EmailGenerateRequest request) {

        try {

            System.out.println(">>> Using OpenAI URL: " + apiUrl);
            System.out.println(">>> Using Model: " + model);

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of(
                                    "role", "system",
                                    "content", "You are an AI assistant that generates professional email replies."
                            ),
                            Map.of(
                                    "role", "user",
                                    "content", "Tone: " + request.getTone() + "\n\n" +
                                            request.getEmailContent()
                            )
                    )
            );

            Map<String, Object> response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // remove manual timeout for now

            if (response == null || !response.containsKey("choices")) {
                throw new AiProviderException("Invalid response from OpenAI");
            }

            List<?> choices = (List<?>) response.get("choices");
            Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");

            String reply = message.get("content").toString();

            return new EmailGenerateResponse(
                    "AI_GENERATED",
                    reply,
                    0.99
            );

        } catch (WebClientResponseException ex) {

            System.out.println("OpenAI HTTP Error: " + ex.getResponseBodyAsString());

            throw new AiProviderException(
                    "OpenAI HTTP Error: " + ex.getStatusCode(),
                    ex
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            throw new AiProviderException(
                    "OpenAI API call failed: " + ex.getMessage(),
                    ex
            );
        }
    }
}