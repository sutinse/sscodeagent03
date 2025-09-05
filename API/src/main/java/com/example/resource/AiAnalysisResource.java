package com.example.resource;

import com.example.model.AiAnalysisRequest;
import com.example.service.AiAnalysisService;
import com.example.service.SystemMessageService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.Map;

/**
 * REST API for AI analysis
 */
@Path("/api/ai")
@Tag(name = "AI Analysis", description = "AI analysis operations using Azure AI Foundry")
public class AiAnalysisResource {
    
    @Inject
    AiAnalysisService aiAnalysisService;
    
    @Inject
    SystemMessageService systemMessageService;
    
    @POST
    @Path("/analyze")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Analyze content using AI",
        description = "Analyze text, file, or web content using Azure AI Foundry API with predefined or custom system messages"
    )
    public Response analyzeContent(@RestForm("text") String text,
                                  @RestForm("file") FileUpload file,
                                  @RestForm("webUrl") String webUrl,
                                  @RestForm("systemMessageId") String systemMessageId,
                                  @RestForm("customSystemMessage") String customSystemMessage,
                                  @RestForm("responseFormat") @Min(0) @Max(1) Integer responseFormat) {
        try {
            // Create request object from form parameters
            AiAnalysisRequest request = new AiAnalysisRequest();
            request.setText(text);
            request.setFile(file);
            request.setWebUrl(webUrl);
            request.setSystemMessageId(systemMessageId);
            request.setCustomSystemMessage(customSystemMessage);
            request.setResponseFormat(responseFormat != null ? responseFormat : 0);
            
            var result = aiAnalysisService.processAnalysis(request);
            
            // Use switch expression for response format handling
            var mediaType = switch (request.getResponseFormat()) {
                case 1 -> MediaType.APPLICATION_JSON;
                default -> MediaType.TEXT_PLAIN;
            };
            
            return Response.ok(result, mediaType).build();
            
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid request: " + e.getMessage())
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Analysis failed: " + e.getMessage())
                .build();
        }
    }
    
    @GET
    @Path("/system-messages")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get available system messages",
        description = "Retrieve all predefined system messages"
    )
    public Response getSystemMessages() {
        Map<String, String> systemMessages = systemMessageService.getAllSystemMessages();
        return Response.ok(systemMessages).build();
    }
    
    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Health check",
        description = "Check the health status of the AI service"
    )
    public Response healthCheck() {
        var health = Map.of(
            "status", "UP",
            "service", "AI Analysis API",
            "version", "1.0.0",
            "java_version", System.getProperty("java.version"),
            "timestamp", java.time.Instant.now().toString()
        );
        return Response.ok(health).build();
    }
}