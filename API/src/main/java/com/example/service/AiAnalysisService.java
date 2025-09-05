package com.example.service;

import com.example.model.AiAnalysisRequest;
import com.example.model.AiAnalysisResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.tika.exception.TikaException;
import java.io.IOException;
import java.util.Objects;

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
     * Process AI analysis request with enhanced error handling and modern patterns
     */
    public Object processAnalysis(AiAnalysisRequest request) throws IOException, TikaException {
        Objects.requireNonNull(request, "Analysis request cannot be null");
        
        // Note: Validation is already done in the Record's compact constructor
        
        // Get system message using pattern matching
        var systemMessage = getSystemMessage(request);
        
        // Get user content using enhanced method
        var userContent = getUserContent(request);
        var inputType = request.getInputType();
        
        // Create embedding for the content with improved error handling
        tryCreateEmbedding(userContent);
        
        // Generate AI response
        var aiResponse = azureAiService.generateResponse(systemMessage, userContent);
        
        // Create response object using modern factory method and Record accessors
        var response = AiAnalysisResponse.of(
            aiResponse,
            inputType,
            request.getSystemMessageSource(),
            request.isJsonResponse() ? "json" : "string"
        );
        
        // Return response in requested format using switch expression
        return switch (request.getResponseFormat()) {
            case 1 -> response; // Will be serialized to JSON by JAX-RS
            default -> aiResponse; // Return as plain string
        };
    }
    
    /**
     * Try to create embedding with improved error handling
     */
    private void tryCreateEmbedding(String content) {
        try {
            azureAiService.createEmbedding(content);
        } catch (Exception e) {
            // Log warning but continue - embedding is not critical for the main functionality
            System.err.println("Warning: Failed to create embedding: " + e.getMessage());
        }
    }
    
    /**
     * Get system message from request using modern pattern matching
     */
    private String getSystemMessage(AiAnalysisRequest request) {
        return switch (request) {
            case AiAnalysisRequest r when r.customSystemMessage() != null && !r.customSystemMessage().trim().isEmpty() -> 
                r.customSystemMessage();
            case AiAnalysisRequest r when r.systemMessageId() != null && !r.systemMessageId().trim().isEmpty() -> {
                var systemMessage = systemMessageService.getSystemMessage(r.systemMessageId());
                if (systemMessage == null) {
                    throw new IllegalArgumentException("Invalid system message ID: " + r.systemMessageId());
                }
                yield systemMessage;
            }
            default -> throw new IllegalArgumentException("No valid system message provided");
        };
    }
    
    /**
     * Get user content from request using enhanced pattern matching
     */
    private String getUserContent(AiAnalysisRequest request) throws IOException, TikaException {
        return switch (request) {
            case AiAnalysisRequest r when r.text() != null && !r.text().trim().isEmpty() -> 
                r.text();
            case AiAnalysisRequest r when r.file() != null -> 
                fileParsingService.parseFile(r.file());
            case AiAnalysisRequest r when r.webUrl() != null && !r.webUrl().trim().isEmpty() -> {
                if (!webScrapingService.isValidUrl(r.webUrl())) {
                    throw new IllegalArgumentException("Invalid URL format: " + r.webUrl());
                }
                yield webScrapingService.scrapeWebContent(r.webUrl());
            }
            default -> throw new IllegalArgumentException("No valid user content provided");
        };
    }
}