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
        
        // Create response object using modern factory method
        var response = AiAnalysisResponse.of(
            aiResponse,
            inputType,
            request.customSystemMessage() != null ? "custom" : request.systemMessageId(),
            request.getResponseFormat() == 1 ? "json" : "string"
        );
        
        // Return response in requested format using switch expression
        return switch (request.getResponseFormat()) {
            case 1 -> response; // Will be serialized to JSON by JAX-RS
            default -> aiResponse; // Return as plain string
        };
    }
    
    /**
     * Get system message from request using pattern matching
     */
    private String getSystemMessage(AiAnalysisRequest request) {
        // Use pattern matching with conditional logic
        if (request.customSystemMessage() != null && !request.customSystemMessage().trim().isEmpty()) {
            return request.customSystemMessage();
        }
        
        if (request.systemMessageId() != null && !request.systemMessageId().trim().isEmpty()) {
            var systemMessage = systemMessageService.getSystemMessage(request.systemMessageId());
            if (systemMessage == null) {
                throw new IllegalArgumentException("Invalid system message ID: " + request.systemMessageId());
            }
            return systemMessage;
        }
        
        throw new IllegalArgumentException("No valid system message provided");
    }
    
    /**
     * Get user content from request using modern switch expression
     */
    private String getUserContent(AiAnalysisRequest request) throws IOException, TikaException {
        return switch (request.getInputType()) {
            case "text" -> request.text();
            case "file" -> fileParsingService.parseFile(request.file());
            case "web_url" -> {
                if (!webScrapingService.isValidUrl(request.webUrl())) {
                    throw new IllegalArgumentException("Invalid URL format: " + request.webUrl());
                }
                yield webScrapingService.scrapeWebContent(request.webUrl());
            }
            default -> throw new IllegalArgumentException("No valid user content provided");
        };
    }
    
    /**
     * Determine input type for response metadata
     */
    private String getInputType(AiAnalysisRequest request) {
        return request.getInputType();
    }
}