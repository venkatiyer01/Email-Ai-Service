package io.emailassistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmailGenerateRequest {
    @NotBlank(message = "Email content cannot be empty")
    @Size(max = 5000, message = "Email content too long")
    private String emailContent;

    @NotBlank(message = "Tone must be provided")
    private String tone;

    public String getEmailContent() {
        return emailContent;
    }
    
    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getTone(){
        return tone;
    }

    public void setTone(String tone){
        this.tone = tone;
    }
}
