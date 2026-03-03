package io.emailassistant.dto;

public class EmailGenerateResponse {

    private String detectedIntent;
    private String reply;
    private double confidence;

    public EmailGenerateResponse(String detectedIntent, String reply, double confidence) {
        this.detectedIntent = detectedIntent;
        this.reply = reply;
        this.confidence = confidence;
    }

    public String getDetectedIntent() {
        return detectedIntent;
    }

    public String getReply() {
        return reply;
    }

    public double getConfidence() {
        return confidence;
    }
}
