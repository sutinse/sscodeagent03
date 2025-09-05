package com.example.model;

import java.time.Instant;
import java.util.Map;

/**
 * Sealed interface for analysis results providing type safety with Java 21 features
 */
public sealed interface AnalysisResult 
    permits AnalysisResult.Success, AnalysisResult.Error {
    
    String getId();
    Instant getTimestamp();
    String getInputType();
    
    /**
     * Successful analysis result
     */
    record Success(
        String id,
        Instant timestamp,
        String inputType,
        String content,
        String systemMessageUsed,
        String responseFormat
    ) implements AnalysisResult {
        
        public Success {
            // Validation in compact constructor
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("ID cannot be null or blank");
            }
            if (content == null) {
                throw new IllegalArgumentException("Content cannot be null");
            }
            if (timestamp == null) {
                timestamp = Instant.now();
            }
            if (inputType == null) {
                inputType = "unknown";
            }
        }
        
        // Implement interface methods
        @Override
        public String getId() { return id; }
        
        @Override
        public Instant getTimestamp() { return timestamp; }
        
        @Override
        public String getInputType() { return inputType; }
        
        public static Success of(String content, String inputType, String systemMessageUsed, String responseFormat) {
            return new Success(
                java.util.UUID.randomUUID().toString(),
                Instant.now(),
                inputType,
                content,
                systemMessageUsed,
                responseFormat
            );
        }
    }
    
    /**
     * Error analysis result
     */
    record Error(
        String id,
        Instant timestamp,
        String inputType,
        String message,
        String errorCode,
        Throwable cause
    ) implements AnalysisResult {
        
        public Error {
            // Validation in compact constructor
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("ID cannot be null or blank");
            }
            if (message == null || message.isBlank()) {
                throw new IllegalArgumentException("Error message cannot be null or blank");
            }
            if (timestamp == null) {
                timestamp = Instant.now();
            }
            if (inputType == null) {
                inputType = "unknown";
            }
            if (errorCode == null) {
                errorCode = "UNKNOWN_ERROR";
            }
        }
        
        // Implement interface methods
        @Override
        public String getId() { return id; }
        
        @Override
        public Instant getTimestamp() { return timestamp; }
        
        @Override
        public String getInputType() { return inputType; }
        
        public static Error of(String message, String inputType) {
            return new Error(
                java.util.UUID.randomUUID().toString(),
                Instant.now(),
                inputType,
                message,
                "PROCESSING_ERROR",
                null
            );
        }
        
        public static Error of(String message, String inputType, Throwable cause) {
            return new Error(
                java.util.UUID.randomUUID().toString(),
                Instant.now(),
                inputType,
                message,
                "PROCESSING_ERROR",
                cause
            );
        }
    }
    
    /**
     * Pattern matching helper for processing results
     */
    default Object processResult() {
        return switch (this) {
            case Success(var id, var timestamp, var inputType, var content, var systemMessage, var format) -> 
                new AiAnalysisResponse(content, inputType, systemMessage, format);
            case Error(var id, var timestamp, var inputType, var message, var errorCode, var cause) -> 
                Map.of(
                    "error", message,
                    "errorCode", errorCode,
                    "timestamp", timestamp.toString(),
                    "inputType", inputType
                );
        };
    }
}