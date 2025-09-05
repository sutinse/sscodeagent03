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

/**
 * Service for Azure AI integration using LangChain4j
 */
@ApplicationScoped
public class AzureAiService {
    
    @Inject
    AzureAiConfig config;
    
    private ChatLanguageModel chatModel;
    private EmbeddingModel embeddingModel;
    
    /**
     * Initialize chat model lazily
     */
    private ChatLanguageModel getChatModel() {
        if (chatModel == null) {
            chatModel = AzureOpenAiChatModel.builder()
                .endpoint(config.getFoundryEndpoint())
                .apiKey(config.getFoundryApiKey())
                .deploymentName(config.getFoundryDeploymentName())
                .timeout(Duration.ofMinutes(2))
                .maxRetries(3)
                .build();
        }
        return chatModel;
    }
    
    /**
     * Initialize embedding model lazily
     */
    private EmbeddingModel getEmbeddingModel() {
        if (embeddingModel == null) {
            embeddingModel = AzureOpenAiEmbeddingModel.builder()
                .endpoint(config.getEmbeddingEndpoint())
                .apiKey(config.getEmbeddingApiKey())
                .deploymentName(config.getEmbeddingDeploymentName())
                .timeout(Duration.ofMinutes(1))
                .maxRetries(3)
                .build();
        }
        return embeddingModel;
    }
    
    /**
     * Generate AI response using system message and user content with enhanced error handling
     */
    public String generateResponse(String systemMessageText, String userContent) {
        try {
            var systemMessage = SystemMessage.from(systemMessageText);
            var userMessage = UserMessage.from(userContent);
            
            var messages = List.of(systemMessage, userMessage);
            var response = getChatModel().generate(messages).content();
            
            return response.text();
            
        } catch (Exception e) {
            // Enhanced error handling with specific exception types
            var errorMessage = switch (e) {
                case IllegalArgumentException iae -> 
                    "Invalid input parameters: " + iae.getMessage();
                case RuntimeException re when re.getCause() instanceof java.net.ConnectException -> 
                    "Failed to connect to Azure AI service: " + re.getMessage();
                case RuntimeException re when re.getMessage().contains("timeout") -> 
                    "Request timed out while communicating with Azure AI service";
                default -> 
                    "Failed to generate AI response: " + e.getMessage();
            };
            throw new RuntimeException(errorMessage, e);
        }
    }
    
    /**
     * Create embedding for text with enhanced error handling
     */
    public Embedding createEmbedding(String text) {
        try {
            return getEmbeddingModel().embed(text).content();
        } catch (Exception e) {
            var errorMessage = switch (e) {
                case IllegalArgumentException iae -> 
                    "Invalid text for embedding: " + iae.getMessage();
                case RuntimeException re when re.getCause() instanceof java.net.ConnectException -> 
                    "Failed to connect to Azure Embedding service: " + re.getMessage();
                default -> 
                    "Failed to create embedding: " + e.getMessage();
            };
            throw new RuntimeException(errorMessage, e);
        }
    }
    
    /**
     * Test connection to Azure AI services
     */
    public boolean testConnection() {
        try {
            String testResponse = generateResponse("You are a helpful assistant.", "Hello, this is a test.");
            return testResponse != null && !testResponse.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}