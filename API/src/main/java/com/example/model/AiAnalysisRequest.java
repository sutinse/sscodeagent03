package com.example.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import java.util.Objects;

/**
 * Request model for AI analysis using modern Java 21 Record pattern.
 * Immutable by design with comprehensive validation.
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
     * Compact constructor with comprehensive validation using Java 21 features
     */
    public AiAnalysisRequest {
        // Normalize and validate responseFormat
        responseFormat = responseFormat != null ? responseFormat : 0;
        if (responseFormat < 0 || responseFormat > 1) {
            throw new IllegalArgumentException("Response format must be 0 (string) or 1 (JSON)");
        }
        
        // Validate exactly one input source is provided
        if (!hasValidInput(text, file, webUrl)) {
            throw new IllegalArgumentException("Exactly one of text, file, or webUrl must be provided");
        }
        
        // Validate system message is provided
        if (!hasValidSystemMessage(systemMessageId, customSystemMessage)) {
            throw new IllegalArgumentException("Either systemMessageId or customSystemMessage must be provided");
        }
        
        // Validate text content if provided
        if (text != null && text.length() > 100_000) {
            throw new IllegalArgumentException("Text content exceeds maximum length of 100,000 characters");
        }
        
        // Validate URL format if provided
        if (webUrl != null && !webUrl.trim().isEmpty() && !isValidUrlFormat(webUrl)) {
            throw new IllegalArgumentException("Invalid URL format: " + webUrl);
        }
    }
    
    /**
     * Static factory method for text-based analysis
     */
    public static AiAnalysisRequest forText(String text, String systemMessageId) {
        Objects.requireNonNull(text, "Text cannot be null");
        Objects.requireNonNull(systemMessageId, "System message ID cannot be null");
        return new AiAnalysisRequest(text, null, null, systemMessageId, null, 0);
    }
    
    /**
     * Static factory method for file-based analysis
     */
    public static AiAnalysisRequest forFile(FileUpload file, String systemMessageId) {
        Objects.requireNonNull(file, "File cannot be null");
        Objects.requireNonNull(systemMessageId, "System message ID cannot be null");
        return new AiAnalysisRequest(null, file, null, systemMessageId, null, 0);
    }
    
    /**
     * Static factory method for web URL analysis
     */
    public static AiAnalysisRequest forWebUrl(String webUrl, String systemMessageId) {
        Objects.requireNonNull(webUrl, "Web URL cannot be null");
        Objects.requireNonNull(systemMessageId, "System message ID cannot be null");
        return new AiAnalysisRequest(null, null, webUrl, systemMessageId, null, 0);
    }
    
    /**
     * Static factory method with custom system message
     */
    public static AiAnalysisRequest withCustomMessage(String text, String customSystemMessage) {
        Objects.requireNonNull(text, "Text cannot be null");
        Objects.requireNonNull(customSystemMessage, "Custom system message cannot be null");
        return new AiAnalysisRequest(text, null, null, null, customSystemMessage, 0);
    }
    
    /**
     * Validates that exactly one input source is provided using modern pattern matching
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
    private static boolean hasValidSystemMessage(String systemMessageId, String customSystemMessage) {
        return (systemMessageId != null && !systemMessageId.trim().isEmpty()) ||
               (customSystemMessage != null && !customSystemMessage.trim().isEmpty());
    }
    
    /**
     * Basic URL format validation
     */
    private static boolean isValidUrlFormat(String url) {
        try {
            var uri = java.net.URI.create(url.trim());
            var protocol = uri.getScheme();
            return "http".equals(protocol) || "https".equals(protocol);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get the effective response format, defaulting to 0 if null
     */
    public int getResponseFormat() {
        return responseFormat != null ? responseFormat : 0;
    }
    
    /**
     * Check if this request expects JSON response
     */
    public boolean isJsonResponse() {
        return getResponseFormat() == 1;
    }
    
    /**
     * Get the input type for response metadata using switch expression
     */
    public String getInputType() {
        return switch (this) {
            case AiAnalysisRequest r when r.text() != null && !r.text().trim().isEmpty() -> "text";
            case AiAnalysisRequest r when r.file() != null -> "file";
            case AiAnalysisRequest r when r.webUrl() != null && !r.webUrl().trim().isEmpty() -> "web_url";
            default -> "unknown";
        };
    }
    
    /**
     * Get the system message source for metadata
     */
    public String getSystemMessageSource() {
        return customSystemMessage != null && !customSystemMessage.trim().isEmpty() ? 
               "custom" : systemMessageId;
    }
}