package com.example.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Configuration for Azure AI services using modern Java patterns
 */
@ApplicationScoped
public class AzureAiConfig {
    
    @ConfigProperty(name = "azure.ai.foundry.endpoint")
    String foundryEndpoint;
    
    @ConfigProperty(name = "azure.ai.foundry.api-key")
    String foundryApiKey;
    
    @ConfigProperty(name = "azure.ai.foundry.deployment-name")
    String foundryDeploymentName;
    
    @ConfigProperty(name = "azure.ai.embedding.endpoint")
    String embeddingEndpoint;
    
    @ConfigProperty(name = "azure.ai.embedding.api-key")
    String embeddingApiKey;
    
    @ConfigProperty(name = "azure.ai.embedding.deployment-name")
    String embeddingDeploymentName;
    
    /**
     * Create immutable configuration record for Azure AI Foundry
     */
    public FoundryConfig getFoundryConfig() {
        return new FoundryConfig(foundryEndpoint, foundryApiKey, foundryDeploymentName);
    }
    
    /**
     * Create immutable configuration record for Azure Embedding
     */
    public EmbeddingConfig getEmbeddingConfig() {
        return new EmbeddingConfig(embeddingEndpoint, embeddingApiKey, embeddingDeploymentName);
    }
    
    /**
     * Immutable configuration record for Azure AI Foundry
     */
    public record FoundryConfig(String endpoint, String apiKey, String deploymentName) {
        public FoundryConfig {
            if (endpoint == null || endpoint.trim().isEmpty()) {
                throw new IllegalArgumentException("Foundry endpoint cannot be null or empty");
            }
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new IllegalArgumentException("Foundry API key cannot be null or empty");
            }
            if (deploymentName == null || deploymentName.trim().isEmpty()) {
                throw new IllegalArgumentException("Foundry deployment name cannot be null or empty");
            }
        }
    }
    
    /**
     * Immutable configuration record for Azure Embedding
     */
    public record EmbeddingConfig(String endpoint, String apiKey, String deploymentName) {
        public EmbeddingConfig {
            if (endpoint == null || endpoint.trim().isEmpty()) {
                throw new IllegalArgumentException("Embedding endpoint cannot be null or empty");
            }
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new IllegalArgumentException("Embedding API key cannot be null or empty");
            }
            if (deploymentName == null || deploymentName.trim().isEmpty()) {
                throw new IllegalArgumentException("Embedding deployment name cannot be null or empty");
            }
        }
    }
    
    // Legacy getters for backward compatibility
    public String getFoundryEndpoint() {
        return foundryEndpoint;
    }
    
    public String getFoundryApiKey() {
        return foundryApiKey;
    }
    
    public String getFoundryDeploymentName() {
        return foundryDeploymentName;
    }
    
    public String getEmbeddingEndpoint() {
        return embeddingEndpoint;
    }
    
    public String getEmbeddingApiKey() {
        return embeddingApiKey;
    }
    
    public String getEmbeddingDeploymentName() {
        return embeddingDeploymentName;
    }
}