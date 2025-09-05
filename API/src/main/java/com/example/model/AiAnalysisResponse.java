package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for AI analysis using Record for immutability
 */
public record AiAnalysisResponse(
    @JsonProperty("result") String result,
    @JsonProperty("input_type") String inputType,
    @JsonProperty("system_message_used") String systemMessageUsed,
    @JsonProperty("response_format") String responseFormat
) {
    
    /**
     * Create response with result and metadata
     */
    public static AiAnalysisResponse of(String result, String inputType, String systemMessageUsed, String responseFormat) {
        return new AiAnalysisResponse(result, inputType, systemMessageUsed, responseFormat);
    }
    
    /**
     * Create response with just result and input type
     */
    public static AiAnalysisResponse of(String result, String inputType) {
        return new AiAnalysisResponse(result, inputType, null, "text");
    }
}