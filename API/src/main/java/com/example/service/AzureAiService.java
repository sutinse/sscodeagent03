package com.example.service;

import com.example.config.AzureAiConfig;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.azure.AzureOpenAiEmbeddingModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.embedding.Embedding;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service for Azure AI integration using LangChain4j with modern Java 21 patterns
 */
@ApplicationScoped
public class AzureAiService {
    
    private static final Duration DEFAULT_CHAT_TIMEOUT = Duration.ofMinutes(2);
    private static final Duration DEFAULT_EMBEDDING_TIMEOUT = Duration.ofMinutes(1);
    private static final int DEFAULT_MAX_RETRIES = 3;
    
    @Inject
    AzureAiConfig config;
    
    private ChatLanguageModel chatModel;
    private EmbeddingModel embeddingModel;
    
    /**
     * Initialize chat model lazily with enhanced error handling
     */
    private ChatLanguageModel getChatModel() {
        if (chatModel == null) {
            try {
                chatModel = AzureOpenAiChatModel.builder()
                    .endpoint(config.getFoundryEndpoint())
                    .apiKey(config.getFoundryApiKey())
                    .deploymentName(config.getFoundryDeploymentName())
                    .timeout(DEFAULT_CHAT_TIMEOUT)
                    .maxRetries(DEFAULT_MAX_RETRIES)
                    .build();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize Azure OpenAI Chat Model: " + e.getMessage(), e);
            }
        }
        return chatModel;
    }
    
    /**
     * Initialize embedding model lazily with enhanced error handling
     */
    private EmbeddingModel getEmbeddingModel() {
        if (embeddingModel == null) {
            try {
                embeddingModel = AzureOpenAiEmbeddingModel.builder()
                    .endpoint(config.getEmbeddingEndpoint())
                    .apiKey(config.getEmbeddingApiKey())
                    .deploymentName(config.getEmbeddingDeploymentName())
                    .timeout(DEFAULT_EMBEDDING_TIMEOUT)
                    .maxRetries(DEFAULT_MAX_RETRIES)
                    .build();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize Azure OpenAI Embedding Model: " + e.getMessage(), e);
            }
        }
        return embeddingModel;
    }
    
    /**
     * Generate AI response using modern validation and error handling
     */
    public String generateResponse(String systemMessageText, String userContent) {
        Objects.requireNonNull(systemMessageText, "System message cannot be null");
        Objects.requireNonNull(userContent, "User content cannot be null");
        
        if (systemMessageText.trim().isEmpty()) {
            throw new IllegalArgumentException("System message cannot be empty");
        }
        
        if (userContent.trim().isEmpty()) {
            throw new IllegalArgumentException("User content cannot be empty");
        }
        
        try {
            var systemMessage = SystemMessage.from(systemMessageText);
            var userMessage = UserMessage.from(userContent);
            var messages = List.of(systemMessage, userMessage);
            
            var response = getChatModel().generate(messages).content();
            var responseText = response.text();
            
            return Optional.ofNullable(responseText)
                .filter(text -> !text.trim().isEmpty())
                .orElseThrow(() -> new RuntimeException("AI service returned empty response"));
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate AI response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create embedding for text with enhanced validation
     */
    public Embedding createEmbedding(String text) {
        Objects.requireNonNull(text, "Text for embedding cannot be null");
        
        if (text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text for embedding cannot be empty");
        }
        
        if (text.length() > 50_000) {
            throw new IllegalArgumentException("Text exceeds maximum length for embedding (50,000 characters)");
        }
        
        try {
            return getEmbeddingModel().embed(text).content();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create embedding: " + e.getMessage(), e);
        }
    }
    
    /**
     * Test connection to Azure AI services with better error reporting
     */
    public boolean testConnection() {
        try {
            var testSystemMessage = "You are a helpful assistant performing a connection test.";
            var testUserMessage = "Hello, this is a connection test. Please respond with 'Connection successful'.";
            
            var testResponse = generateResponse(testSystemMessage, testUserMessage);
            return testResponse != null && 
                   !testResponse.trim().isEmpty() && 
                   testResponse.toLowerCase().contains("connection");
                   
        } catch (Exception e) {
            // Log the error but don't expose internal details
            System.err.println("Azure AI connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get service health information
     */
    public ServiceHealth getServiceHealth() {
        var isHealthy = testConnection();
        var status = isHealthy ? "UP" : "DOWN";
        var details = isHealthy ? 
            "Azure AI services are responding normally" : 
            "Azure AI services are not responding";
            
        return new ServiceHealth(status, details);
    }
    
    /**
     * Record representing service health status
     */
    public record ServiceHealth(String status, String details) {
        public ServiceHealth {
            Objects.requireNonNull(status, "Status cannot be null");
            Objects.requireNonNull(details, "Details cannot be null");
        }
        
        public boolean isHealthy() {
            return "UP".equals(status);
        }
    }
}