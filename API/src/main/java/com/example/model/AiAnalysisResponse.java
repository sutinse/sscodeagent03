package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for AI analysis
 */
public class AiAnalysisResponse {
    
    @JsonProperty("result")
    private String result;
    
    @JsonProperty("input_type")
    private String inputType;
    
    @JsonProperty("system_message_used")
    private String systemMessageUsed;
    
    @JsonProperty("response_format")
    private String responseFormat;
    
    public AiAnalysisResponse() {}
    
    public AiAnalysisResponse(String result, String inputType, String systemMessageUsed, String responseFormat) {
        this.result = result;
        this.inputType = inputType;
        this.systemMessageUsed = systemMessageUsed;
        this.responseFormat = responseFormat;
    }
    
    // Getters and setters
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getInputType() {
        return inputType;
    }
    
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
    
    public String getSystemMessageUsed() {
        return systemMessageUsed;
    }
    
    public void setSystemMessageUsed(String systemMessageUsed) {
        this.systemMessageUsed = systemMessageUsed;
    }
    
    public String getResponseFormat() {
        return responseFormat;
    }
    
    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }
}