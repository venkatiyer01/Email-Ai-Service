package io.emailassistant.api;

import java.time.Instant;

public class ApiResponse<T> {
    private final String requestId;
    private final Instant timestamp;
    private final T data;

    public ApiResponse(String requstId, Instant timeStamp, T data){
        this.requestId = requstId;
        this.timestamp = timeStamp;
        this.data = data;
    }

    public String getRequestId() {return requestId;}
    public Instant getTimeInstant() { return timestamp;}
    public T getData() {return data;}


}
