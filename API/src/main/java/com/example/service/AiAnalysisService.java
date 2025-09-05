package com.example.service;

import com.example.model.AiAnalysisRequest;
import com.example.model.AiAnalysisResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.tika.exception.TikaException;
import java.io.IOException;

/**
 * Main service orchestrating AI analysis workflow
 */
@ApplicationScoped
public class AiAnalysisService {
    
    @Inject
    SystemMessageService systemMessageService;
    
    @Inject
    FileParsingService fileParsingService;
    
    @Inject
    WebScrapingService webScrapingService;
    
    @Inject
    AzureAiService azureAiService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Process AI analysis request
     */
    public Object processAnalysis(AiAnalysisRequest request) throws IOException, TikaException {
        // Validate input
        if (!request.hasValidInput()) {
            throw new IllegalArgumentException("Exactly one of text, file, or webUrl must be provided");
        }
        
        if (!request.hasValidSystemMessage()) {
            throw new IllegalArgumentException("Either systemMessageId or customSystemMessage must be provided");
        }
        
        // Get system message
        String systemMessage = getSystemMessage(request);
        
        // Get user content
        String userContent = getUserContent(request);
        String inputType = getInputType(request);
        
        // Create embedding for the content
        try {
            azureAiService.createEmbedding(userContent);
        } catch (Exception e) {
            // Log warning but continue - embedding is not critical for the main functionality
            System.err.println("Warning: Failed to create embedding: " + e.getMessage());
        }
        
        // Generate AI response
        String aiResponse = azureAiService.generateResponse(systemMessage, userContent);
        
        // Create response object
        AiAnalysisResponse response = new AiAnalysisResponse(
            aiResponse,
            inputType,
            request.getCustomSystemMessage() != null ? "custom" : request.getSystemMessageId(),
            request.getResponseFormat() == 1 ? "json" : "string"
        );
        
        // Return response in requested format
        if (request.getResponseFormat() == 1) {
            return response; // Will be serialized to JSON by JAX-RS
        } else {
            return aiResponse; // Return as plain string
        }
    }
    
    /**
     * Get system message from request
     */
    private String getSystemMessage(AiAnalysisRequest request) {
        if (request.getCustomSystemMessage() != null && !request.getCustomSystemMessage().trim().isEmpty()) {
            return request.getCustomSystemMessage();
        }
        
        if (request.getSystemMessageId() != null && !request.getSystemMessageId().trim().isEmpty()) {
            String systemMessage = systemMessageService.getSystemMessage(request.getSystemMessageId());
            if (systemMessage == null) {
                throw new IllegalArgumentException("Invalid system message ID: " + request.getSystemMessageId());
            }
            return systemMessage;
        }
        
        throw new IllegalArgumentException("No valid system message provided");
    }
    
    /**
     * Get user content from request
     */
    private String getUserContent(AiAnalysisRequest request) throws IOException, TikaException {
        if (request.getText() != null && !request.getText().trim().isEmpty()) {
            return request.getText();
        }
        
        if (request.getFile() != null) {
            return fileParsingService.parseFile(request.getFile());
        }
        
        if (request.getWebUrl() != null && !request.getWebUrl().trim().isEmpty()) {
            if (!webScrapingService.isValidUrl(request.getWebUrl())) {
                throw new IllegalArgumentException("Invalid URL format: " + request.getWebUrl());
            }
            return webScrapingService.scrapeWebContent(request.getWebUrl());
        }
        
        throw new IllegalArgumentException("No valid user content provided");
    }
    
    /**
     * Determine input type for response metadata
     */
    private String getInputType(AiAnalysisRequest request) {
        if (request.getText() != null && !request.getText().trim().isEmpty()) {
            return "text";
        }
        if (request.getFile() != null) {
            return "file";
        }
        if (request.getWebUrl() != null && !request.getWebUrl().trim().isEmpty()) {
            return "web_url";
        }
        return "unknown";
    }
}