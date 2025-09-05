package com.example.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import java.util.Objects;

/**
 * Modern record-based request model for AI analysis using Java 21 features
 */
public record AiAnalysisRequest(
    String text,
    FileUpload file,
    String webUrl,
    String systemMessageId,
    String customSystemMessage,
    @Min(0) @Max(1) Integer responseFormat
) {
    
    /**
     * Compact constructor with validation and normalization
     */
    public AiAnalysisRequest {
        // Normalize responseFormat to default value if null
        responseFormat = responseFormat != null ? responseFormat : 0;
        
        // Validate that exactly one input source is provided
        if (!hasValidInput(text, file, webUrl)) {
            throw new IllegalArgumentException("Exactly one of text, file, or webUrl must be provided");
        }
        
        // Validate that system message is provided
        if (!hasValidSystemMessage(systemMessageId, customSystemMessage)) {
            throw new IllegalArgumentException("Either systemMessageId or customSystemMessage must be provided");
        }
    }
    
    /**
     * Secondary constructor for form-based input (needed for JAX-RS)
     */
    public AiAnalysisRequest(String text, FileUpload file, String webUrl, 
                            String systemMessageId, String customSystemMessage) {
        this(text, file, webUrl, systemMessageId, customSystemMessage, 0);
    }
    
    /**
     * Validates that exactly one input source is provided
     */
    public boolean hasValidInput() {
        return hasValidInput(text, file, webUrl);
    }
    
    /**
     * Static validation method for input sources
     */
    private static boolean hasValidInput(String text, FileUpload file, String webUrl) {
        var inputCount = 0;
        if (text != null && !text.trim().isEmpty()) inputCount++;
        if (file != null) inputCount++;
        if (webUrl != null && !webUrl.trim().isEmpty()) inputCount++;
        return inputCount == 1;
    }
    
    /**
     * Validates that system message is provided
     */
    public boolean hasValidSystemMessage() {
        return hasValidSystemMessage(systemMessageId, customSystemMessage);
    }
    
    /**
     * Static validation method for system messages
     */
    private static boolean hasValidSystemMessage(String systemMessageId, String customSystemMessage) {
        return (systemMessageId != null && !systemMessageId.trim().isEmpty()) ||
               (customSystemMessage != null && !customSystemMessage.trim().isEmpty());
    }
    
    /**
     * Get response format with default fallback using modern patterns
     */
    public int getResponseFormat() {
        return Objects.requireNonNullElse(responseFormat, 0);
    }
    
    /**
     * Create a new request with different response format (immutable update pattern)
     */
    public AiAnalysisRequest withResponseFormat(int newFormat) {
        return new AiAnalysisRequest(text, file, webUrl, systemMessageId, customSystemMessage, newFormat);
    }
    
    /**
     * Get input type using switch expression
     */
    public String getInputType() {
        if (text != null && !text.trim().isEmpty()) return "text";
        if (file != null) return "file";  
        if (webUrl != null && !webUrl.trim().isEmpty()) return "web_url";
        return "unknown";
    }
}