package com.example.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Configuration for Azure AI services
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
    
    // Getters
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