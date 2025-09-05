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
import org.jboss.resteasy.reactive.MultipartForm;
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
    public Response analyzeContent(@MultipartForm AiAnalysisRequest request) {
        try {
            Object result = aiAnalysisService.processAnalysis(request);
            
            if (request.getResponseFormat() == 1) {
                return Response.ok(result, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.ok(result, MediaType.TEXT_PLAIN).build();
            }
            
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
        Map<String, String> health = Map.of(
            "status", "UP",
            "service", "AI Analysis API",
            "version", "1.0.0"
        );
        return Response.ok(health).build();
    }
}