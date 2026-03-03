package io.emailassistant.api;

import java.time.Instant;
import java.util.Map;

public class ApiError {
    private final String requestId;
    private final Instant timeStamp;
    private final String errorCode;
    private final String message;
    private final Map<String,Object> details;
    
    public ApiError(String requestId,
                    Instant timestamp,
                    String errorCode,
                    String message,
                    Map<String,Object> details) {

            this.requestId = requestId;
            this.timeStamp = timestamp;
            this.errorCode = errorCode;
            this.message = message;
            this.details = details;            
        }

    public String getRequestId() { return requestId; }
    public Instant getTimeStamp() { return timeStamp; }
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public Map<String,Object> getDetails() { return details; }


}
