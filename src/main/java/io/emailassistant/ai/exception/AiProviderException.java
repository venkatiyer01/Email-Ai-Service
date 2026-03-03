package io.emailassistant.ai.exception;

/**
 * Custom exception to wrap all AI provider related failures.
 *
 * This prevents leaking raw provider exceptions to upper layers.
*/

public class AiProviderException extends RuntimeException{
    public AiProviderException(String message) {
        super(message);
    }
    public AiProviderException(String message,Throwable cause){
        super(message,cause);
    }
    
}
