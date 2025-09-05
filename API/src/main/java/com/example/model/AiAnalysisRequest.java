package com.example.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.multipart.FileUpload;

/**
 * Request model for AI analysis
 */
public class AiAnalysisRequest {
    
    @FormParam("text")
    @PartType(MediaType.TEXT_PLAIN)
    private String text;
    
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private FileUpload file;
    
    @FormParam("webUrl")
    @PartType(MediaType.TEXT_PLAIN)
    private String webUrl;
    
    @FormParam("systemMessageId")
    @PartType(MediaType.TEXT_PLAIN)
    private String systemMessageId;
    
    @FormParam("customSystemMessage")
    @PartType(MediaType.TEXT_PLAIN)
    private String customSystemMessage;
    
    @FormParam("responseFormat")
    @PartType(MediaType.TEXT_PLAIN)
    @Min(0)
    @Max(1)
    private Integer responseFormat = 0; // 0 = String, 1 = JSON
    
    public AiAnalysisRequest() {}
    
    // Getters and setters
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public FileUpload getFile() {
        return file;
    }
    
    public void setFile(FileUpload file) {
        this.file = file;
    }
    
    public String getWebUrl() {
        return webUrl;
    }
    
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
    
    public String getSystemMessageId() {
        return systemMessageId;
    }
    
    public void setSystemMessageId(String systemMessageId) {
        this.systemMessageId = systemMessageId;
    }
    
    public String getCustomSystemMessage() {
        return customSystemMessage;
    }
    
    public void setCustomSystemMessage(String customSystemMessage) {
        this.customSystemMessage = customSystemMessage;
    }
    
    public Integer getResponseFormat() {
        return responseFormat != null ? responseFormat : 0;
    }
    
    public void setResponseFormat(Integer responseFormat) {
        this.responseFormat = responseFormat;
    }
    
    /**
     * Validates that exactly one input source is provided
     */
    public boolean hasValidInput() {
        int inputCount = 0;
        if (text != null && !text.trim().isEmpty()) inputCount++;
        if (file != null) inputCount++;
        if (webUrl != null && !webUrl.trim().isEmpty()) inputCount++;
        return inputCount == 1;
    }
    
    /**
     * Validates that system message is provided
     */
    public boolean hasValidSystemMessage() {
        return (systemMessageId != null && !systemMessageId.trim().isEmpty()) ||
               (customSystemMessage != null && !customSystemMessage.trim().isEmpty());
    }
}