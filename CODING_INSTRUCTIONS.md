# Coding Instructions for SSCodeAgent03

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [Java Coding Standards](#java-coding-standards)
5. [Quarkus-Specific Guidelines](#quarkus-specific-guidelines)
6. [Testing Standards](#testing-standards)
7. [Documentation Requirements](#documentation-requirements)
8. [Security Guidelines](#security-guidelines)
9. [Performance Guidelines](#performance-guidelines)
10. [Code Review Process](#code-review-process)
11. [Build and Deployment](#build-and-deployment)
12. [Migration and Modernization](#migration-and-modernization)

## Project Overview

SSCodeAgent03 is a production-ready, cloud-native AI analysis API built with Quarkus that provides intelligent content analysis using Azure AI Foundry. The application follows modern Java development practices and enterprise-grade architecture patterns.

### Core Mission
Deliver high-performance, scalable AI content analysis with support for multiple input formats (text, files, web content) while maintaining enterprise security standards and developer experience excellence.

### Key Features
- **Multi-format Analysis**: Support for text, file uploads (.java, .txt, .pdf, .docx, .pptx), and web content scraping
- **Azure AI Integration**: Seamless integration with Azure AI Foundry via LangChain4j framework
- **Configurable Workflows**: Predefined system messages for SEO analysis and Java code review
- **Enterprise Security**: Comprehensive input validation, file type restrictions, and secure configuration
- **Developer Experience**: OpenAPI/Swagger documentation, hot reload development, comprehensive testing

## Technology Stack

### Core Framework & Runtime
- **Framework**: Quarkus 3.26.2 (Latest LTS with enhanced cloud-native features)
- **Java Version**: 21 LTS (with Java 17 compatibility for broader deployment options)
- **Build Tool**: Maven 3.9+ (with modern plugin ecosystem)
- **Runtime**: JVM mode (with native image capability for production)

### AI and Machine Learning
- **AI Platform**: Azure AI Foundry (Enterprise-grade AI service)
- **AI Framework**: LangChain4j 0.25.0 (Java-first AI application framework)
- **Embedding Generation**: Azure OpenAI Service (Text embeddings for semantic analysis)
- **Model Support**: GPT-4, GPT-3.5-turbo (configurable deployment)

### Data Processing & Integration
- **File Processing**: Apache Tika 2.9.1 (Content detection and extraction)
- **Web Scraping**: JSoup 1.16.2 (HTML parsing and content extraction)
- **HTTP Client**: Quarkus REST Client (Reactive HTTP communication)
- **Validation**: Hibernate Validator (Bean validation framework)

### Development & Testing
- **Testing Framework**: JUnit 5 (Modern testing with parameterized and nested tests)
- **API Testing**: REST Assured (Fluent API testing for REST endpoints)
- **Documentation**: OpenAPI 3 + Swagger UI (Interactive API documentation)
- **Development Mode**: Quarkus Dev Mode (Hot reload and debugging)

### Production & Deployment
- **Containerization**: Docker-ready with Quarkus optimizations
- **Cloud-Native**: Kubernetes-ready with health checks and metrics
- **Configuration**: Externalized via environment variables and ConfigMaps
- **Monitoring**: Built-in health checks and metrics endpoints

## Project Structure

```
sscodeagent03/
├── README.md
├── CODING_INSTRUCTIONS.md
└── API/
    ├── pom.xml
    ├── src/
    │   ├── main/
    │   │   ├── java/com/example/
    │   │   │   ├── config/          # Configuration classes
    │   │   │   ├── model/           # Data transfer objects
    │   │   │   ├── resource/        # REST endpoints (controllers)
    │   │   │   └── service/         # Business logic
    │   │   └── resources/
    │   │       └── application.properties
    │   └── test/
    │       └── java/com/example/
    └── target/                      # Build artifacts (gitignored)
```

### Package Organization
- `com.example.config` - Configuration and setup classes
- `com.example.model` - DTOs, request/response objects, entities
- `com.example.resource` - JAX-RS resources (REST controllers)
- `com.example.service` - Business logic and service layer
- `com.example.exception` - Custom exceptions (when needed)

## Java Coding Standards

### Java 21 Language Features
This project fully embraces modern Java 21 features for enhanced code quality, performance, and maintainability:

#### Record Classes (Preferred for DTOs)
```java
// ✅ Modern approach - Use records for immutable data carriers
public record AiAnalysisRequest(
    String content,
    String systemMessage,
    int responseFormat,
    Set<String> tags
) {
    // Compact constructor for validation
    public AiAnalysisRequest {
        Objects.requireNonNull(content, "Content cannot be null");
        if (content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be blank");
        }
        responseFormat = Math.max(0, Math.min(responseFormat, 1));
        tags = tags == null ? Set.of() : Set.copyOf(tags);
    }
}

// ❌ Legacy approach - Avoid traditional DTOs for simple data carriers
public class LegacyAnalysisRequest {
    private String content;
    private String systemMessage;
    // ... getters, setters, equals, hashCode, toString
}
```

#### Pattern Matching and Switch Expressions
```java
// ✅ Modern pattern matching with switch expressions
public String processAnalysisResult(AnalysisResult result) {
    return switch (result) {
        case TextResult(var content) -> formatTextResult(content);
        case JsonResult(var data) -> formatJsonResult(data);
        case ErrorResult(var error, var code) when code > 500 -> 
            "Server error: " + error;
        case ErrorResult(var error, var code) -> 
            "Client error: " + error;
        case null -> "No result available";
        default -> throw new IllegalArgumentException("Unknown result type");
    };
}

// ✅ Pattern matching with instanceof
public void processFileUpload(Object upload) {
    if (upload instanceof FileUpload file && file.size() > 0) {
        var content = extractContent(file);
        analyzeContent(content);
    }
}
```

#### Text Blocks for Multi-line Content
```java
// ✅ Use text blocks for readable multi-line strings
private static final String SEO_ANALYSIS_PROMPT = """
    Analyze the provided content for search engine optimization:
    
    1. Keyword density and distribution
    2. Content structure and headings
    3. Meta description optimization
    4. Readability and user engagement
    5. Technical SEO recommendations
    
    Provide specific, actionable recommendations.
    """;

// ✅ Text blocks for JSON templates
private static final String ERROR_RESPONSE_TEMPLATE = """
    {
        "error": "%s",
        "timestamp": "%s",
        "path": "%s",
        "status": %d
    }
    """;
```

#### Sealed Classes for Controlled Hierarchies
```java
// ✅ Sealed classes for controlled type hierarchies
public sealed interface AnalysisResult 
    permits TextResult, JsonResult, ErrorResult {
    
    String getId();
    Instant getTimestamp();
}

public record TextResult(String id, Instant timestamp, String content) 
    implements AnalysisResult {}

public record JsonResult(String id, Instant timestamp, Map<String, Object> data) 
    implements AnalysisResult {}

public record ErrorResult(String id, Instant timestamp, String error, int code) 
    implements AnalysisResult {}
```

#### Enhanced Type Inference with var
```java
// ✅ Use var for complex types and improved readability
public void processAnalysisRequests(List<AiAnalysisRequest> requests) {
    var validRequests = requests.stream()
        .filter(this::isValidRequest)
        .collect(Collectors.toList());
    
    var analysisResults = validRequests.parallelStream()
        .map(this::performAnalysis)
        .collect(Collectors.toMap(
            AnalysisResult::getId,
            Function.identity()
        ));
    
    var successCount = analysisResults.values().stream()
        .mapToInt(result -> result instanceof ErrorResult ? 0 : 1)
        .sum();
    
    log.info("Processed {} requests, {} successful", requests.size(), successCount);
}
```

### Naming Conventions
- **Classes**: PascalCase (e.g., `AiAnalysisService`)
- **Methods**: camelCase (e.g., `processAnalysis`)
- **Variables**: camelCase (e.g., `systemMessage`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_FILE_SIZE`)
- **Packages**: lowercase with dots (e.g., `com.example.service`)

### Code Formatting
- **Line Length**: Maximum 120 characters
- **Indentation**: 4 spaces (no tabs)
- **Braces**: Opening brace on same line
- **Imports**: Group and order imports logically
  1. Java standard libraries
  2. Third-party libraries
  3. Project-specific imports

### Example Format:
```java
package com.example.service;

import java.util.Map;
import java.util.Set;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.example.model.AiAnalysisRequest;

@ApplicationScoped
public class ExampleService {
    
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final Set<String> ALLOWED_FORMATS = Set.of("json", "text", "markdown");
    
    @Inject
    AnotherService anotherService;
    
    public Map<String, String> processData(AiAnalysisRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        // Use switch expression for response format handling
        var responseType = switch (request.getResponseFormat()) {
            case 0 -> "text";
            case 1 -> "json";
            default -> throw new IllegalArgumentException("Invalid response format");
        };
        
        return Map.of("result", "processed", "format", responseType);
    }
    
    // Use text blocks for multi-line content
    private static final String DEFAULT_PROMPT = """
        You are an AI assistant that analyzes content.
        Please provide a detailed analysis of the following content:
        - Structure and organization
        - Key themes and topics
        - Recommendations for improvement
        """;
}
```

### Enhanced Class Design Principles

#### Modern Service Design
```java
// ✅ Modern service with comprehensive error handling
@ApplicationScoped
public class AiAnalysisService {
    
    private static final Logger log = LoggerFactory.getLogger(AiAnalysisService.class);
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final Duration ANALYSIS_TIMEOUT = Duration.ofMinutes(5);
    
    @Inject
    AzureAiService azureAiService;
    
    @Inject
    SystemMessageService systemMessageService;
    
    /**
     * Performs AI analysis with comprehensive error handling and retry logic.
     */
    public CompletionStage<AnalysisResult> analyzeContent(AiAnalysisRequest request) {
        return CompletableFuture
            .supplyAsync(() -> validateAndPreprocess(request))
            .thenCompose(this::performAnalysisWithRetry)
            .orTimeout(ANALYSIS_TIMEOUT.toSeconds(), TimeUnit.SECONDS)
            .handle(this::handleAnalysisResult);
    }
    
    private AnalysisResult validateAndPreprocess(AiAnalysisRequest request) {
        // Validation and preprocessing logic
        if (request.content().length() > 100_000) {
            throw new IllegalArgumentException("Content exceeds maximum length");
        }
        return new TextResult(
            UUID.randomUUID().toString(),
            Instant.now(),
            request.content()
        );
    }
    
    private CompletionStage<AnalysisResult> performAnalysisWithRetry(AnalysisResult input) {
        return RetryUtils.withExponentialBackoff(
            () -> azureAiService.analyze(input),
            MAX_RETRY_ATTEMPTS,
            Duration.ofSeconds(1)
        );
    }
    
    private AnalysisResult handleAnalysisResult(AnalysisResult result, Throwable throwable) {
        if (throwable != null) {
            log.error("Analysis failed", throwable);
            return new ErrorResult(
                UUID.randomUUID().toString(),
                Instant.now(),
                throwable.getMessage(),
                500
            );
        }
        return result;
    }
}
```

#### Configuration Management
```java
// ✅ Type-safe configuration with records
@ConfigMapping(prefix = "azure.ai")
public interface AzureAiConfig {
    
    @WithDefault("https://api.openai.azure.com")
    String endpoint();
    
    @WithName("api-key")
    String apiKey();
    
    @WithDefault("gpt-4")
    String deployment();
    
    @WithDefault("30s")
    Duration timeout();
    
    @WithDefault("3")
    int maxRetries();
    
    // Nested configuration
    Embedding embedding();
    
    interface Embedding {
        String endpoint();
        
        @WithName("api-key")
        String apiKey();
        
        @WithDefault("text-embedding-ada-002")
        String deployment();
    }
}

// ✅ Using configuration in services
@ApplicationScoped
public class AzureAiService {
    
    @Inject
    AzureAiConfig config;
    
    @PostConstruct
    void initialize() {
        log.info("Initializing Azure AI service with endpoint: {}", 
                 config.endpoint());
        
        // Validate critical configuration
        if (config.apiKey() == null || config.apiKey().isBlank()) {
            throw new ConfigurationException("Azure AI API key is required");
        }
    }
}
```

## Quarkus-Specific Guidelines

### CDI and Dependency Injection
```java
@ApplicationScoped  // For stateless services
@RequestScoped      // For request-specific data
@Singleton          // For expensive-to-create services
```

### Configuration
- Use `@ConfigProperty` for external configuration
- Provide sensible defaults where possible
- Use environment-specific property files

```java
@ConfigProperty(name = "azure.ai.foundry.endpoint", defaultValue = "https://default-endpoint.azure.com")
String foundryEndpoint;
```

### REST Resources
```java
@Path("/api/ai")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiAnalysisResource {
    
    @POST
    @Path("/analyze")
    @Operation(summary = "Analyze content", description = "Detailed description")
    public Response analyzeContent(@Valid AiAnalysisRequest request) {
        // Implementation
    }
}
```

### File Upload Handling
**Current approach**: Uses modern Quarkus multipart handling with `@RestForm`:

```java
// Current recommended approach
@POST
@Consumes(MediaType.MULTIPART_FORM_DATA)
public Response uploadFile(@RestForm("file") FileUpload file,
                          @RestForm("text") String text) {
    // Implementation
}
```

### Modern Java Features in Quarkus
```java
// Use records for configuration classes
public record AiConfig(String endpoint, String apiKey, String deploymentName) {}

// Use sealed classes for controlled hierarchies
public sealed interface AnalysisResult permits TextResult, JsonResult, ErrorResult {}

// Pattern matching with switch expressions
public String formatResult(AnalysisResult result) {
    return switch (result) {
        case TextResult(var content) -> content;
        case JsonResult(var data) -> data.toString();
        case ErrorResult(var error) -> "Error: " + error;
    };
}
```

## Testing Standards

### Modern Testing Architecture
Our testing strategy follows a pyramid approach with comprehensive coverage at all levels:

```
    ┌─────────────┐
    │   E2E Tests │  ← Integration tests with external systems
    │     (5%)    │
    ├─────────────┤
    │ API Tests   │  ← REST endpoint testing
    │    (15%)    │
    ├─────────────┤
    │ Unit Tests  │  ← Business logic and service testing  
    │    (80%)    │
    └─────────────┘
```

### Unit Testing with Modern Java Features

#### Service Layer Testing
```java
@QuarkusTest
class AiAnalysisServiceTest {
    
    @Inject
    AiAnalysisService analysisService;
    
    @InjectMock
    AzureAiService mockAzureAiService;
    
    @Test
    @DisplayName("Should successfully analyze valid text content")
    void shouldAnalyzeValidTextContent() {
        // Given
        var request = new AiAnalysisRequest(
            "Sample content for analysis",
            "SystemMessage1",
            0,
            Set.of("seo", "analysis")
        );
        
        var expectedResult = new TextResult(
            "test-id",
            Instant.now(),
            "Analysis complete"
        );
        
        when(mockAzureAiService.analyze(any()))
            .thenReturn(CompletableFuture.completedFuture(expectedResult));
        
        // When
        var result = analysisService.analyzeContent(request)
            .toCompletableFuture()
            .join();
        
        // Then
        assertThat(result)
            .isInstanceOf(TextResult.class)
            .extracting("content")
            .isEqualTo("Analysis complete");
        
        verify(mockAzureAiService).analyze(any());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\n\t"})
    @DisplayName("Should reject blank content")
    void shouldRejectBlankContent(String blankContent) {
        // Given
        var request = new AiAnalysisRequest(
            blankContent,
            "SystemMessage1",
            0,
            Set.of()
        );
        
        // When & Then
        assertThatThrownBy(() -> analysisService.analyzeContent(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Content cannot be blank");
    }
    
    @Test
    @DisplayName("Should handle analysis timeout gracefully")
    void shouldHandleAnalysisTimeout() {
        // Given
        var request = new AiAnalysisRequest(
            "Content that takes too long to analyze",
            "SystemMessage1",
            0,
            Set.of()
        );
        
        when(mockAzureAiService.analyze(any()))
            .thenReturn(CompletableFuture.failedFuture(
                new TimeoutException("Analysis timed out")));
        
        // When
        var result = analysisService.analyzeContent(request)
            .toCompletableFuture()
            .join();
        
        // Then
        assertThat(result)
            .isInstanceOf(ErrorResult.class)
            .satisfies(error -> {
                var errorResult = (ErrorResult) error;
                assertThat(errorResult.error()).contains("timed out");
                assertThat(errorResult.code()).isEqualTo(500);
            });
    }
}
```

#### REST Endpoint Testing
```java
@QuarkusTest
class AiAnalysisResourceTest {
    
    @Test
    @DisplayName("POST /api/ai/analyze should accept valid multipart request")
    void shouldAcceptValidMultipartRequest() {
        given()
            .multiPart("text", "Sample text for analysis")
            .multiPart("systemMessageId", "SystemMessage1")
            .multiPart("responseFormat", "0")
        .when()
            .post("/api/ai/analyze")
        .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON)
            .body("result", notNullValue())
            .body("timestamp", notNullValue());
    }
    
    @Test
    @DisplayName("Should validate that exactly one content source is provided")
    void shouldValidateExactlyOneContentSource() {
        given()
            .multiPart("text", "Sample text")
            .multiPart("webUrl", "https://example.com")
            .multiPart("systemMessageId", "SystemMessage1")
        .when()
            .post("/api/ai/analyze")
        .then()
            .statusCode(400)
            .body("error", containsString("exactly one content source"));
    }
    
    @Test
    @DisplayName("Should handle file upload with size validation")
    void shouldHandleFileUploadWithSizeValidation() {
        var largeBytesArray = new byte[51 * 1024 * 1024]; // 51MB - exceeds limit
        
        given()
            .multiPart("file", "large-file.txt", largeBytesArray, "text/plain")
            .multiPart("systemMessageId", "SystemMessage2")
        .when()
            .post("/api/ai/analyze")
        .then()
            .statusCode(413) // Payload Too Large
            .body("error", containsString("file size exceeds"));
    }
}
```

### Integration Testing

#### Configuration Testing
```java
@QuarkusTest
@TestProfile(AzureConfigTestProfile.class)
class AzureAiConfigurationTest {
    
    @Inject
    AzureAiConfig config;
    
    @Test
    @DisplayName("Should load Azure AI configuration from environment")
    void shouldLoadAzureConfiguration() {
        assertThat(config.endpoint()).isEqualTo("https://test-endpoint.azure.com");
        assertThat(config.deployment()).isEqualTo("gpt-4-test");
        assertThat(config.timeout()).isEqualTo(Duration.ofSeconds(30));
    }
    
    public static class AzureConfigTestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of(
                "azure.ai.endpoint", "https://test-endpoint.azure.com",
                "azure.ai.deployment", "gpt-4-test",
                "azure.ai.timeout", "30s"
            );
        }
    }
}
```

### Test Data Management

#### Test Data Builders
```java
public class TestDataBuilders {
    
    public static AiAnalysisRequestBuilder analysisRequest() {
        return new AiAnalysisRequestBuilder();
    }
    
    public static class AiAnalysisRequestBuilder {
        private String content = "Default test content";
        private String systemMessage = "SystemMessage1";
        private int responseFormat = 0;
        private Set<String> tags = Set.of();
        
        public AiAnalysisRequestBuilder withContent(String content) {
            this.content = content;
            return this;
        }
        
        public AiAnalysisRequestBuilder withSystemMessage(String systemMessage) {
            this.systemMessage = systemMessage;
            return this;
        }
        
        public AiAnalysisRequestBuilder withResponseFormat(int format) {
            this.responseFormat = format;
            return this;
        }
        
        public AiAnalysisRequestBuilder withTags(String... tags) {
            this.tags = Set.of(tags);
            return this;
        }
        
        public AiAnalysisRequest build() {
            return new AiAnalysisRequest(content, systemMessage, responseFormat, tags);
        }
    }
}

// Usage in tests
@Test
void shouldProcessSeoAnalysisRequest() {
    var request = TestDataBuilders.analysisRequest()
        .withContent("Blog post about cloud computing")
        .withSystemMessage("SystemMessage1")
        .withTags("seo", "cloud", "blog")
        .build();
    
    // Test implementation
}
```

### Coverage and Quality Requirements
- **Minimum Coverage**: 85% line coverage for service layer
- **Critical Path Coverage**: 100% coverage for security-critical code
- **Mutation Testing**: Periodic mutation testing to validate test quality
- **Performance Testing**: Load testing for critical endpoints

## Documentation Requirements

### Code Documentation
- **JavaDoc**: Required for all public methods and classes
- **Inline Comments**: Use sparingly, prefer self-documenting code
- **OpenAPI**: Use annotations for REST endpoints

```java
/**
 * Analyzes content using AI with configurable system messages.
 * 
 * @param request The analysis request containing content and configuration
 * @return Response containing analysis results
 * @throws IllegalArgumentException when request validation fails
 */
@Operation(
    summary = "Analyze content using AI",
    description = "Analyzes text, file, or web content using Azure AI Foundry"
)
public Response analyzeContent(@Valid AiAnalysisRequest request) {
    // Implementation
}
```

### README Updates
- Keep API documentation current
- Include setup instructions
- Provide example usage
- Document configuration requirements

## Security Guidelines

### Input Validation
- Validate all inputs at the resource layer
- Use Bean Validation annotations (`@Valid`, `@NotNull`, `@Size`)
- Sanitize user inputs before processing

### File Upload Security
```java
// Validate file types
private static final Set<String> ALLOWED_EXTENSIONS = 
    Set.of(".java", ".txt", ".pdf", ".docx", ".pptx");

// Check file size limits
@Size(max = 50_000_000) // 50MB limit
private FileUpload file;
```

### Configuration Security
- Never commit API keys or secrets
- Use environment variables for sensitive configuration
- Implement proper error handling that doesn't leak sensitive information

### Web Scraping Security
- Validate URLs before scraping
- Implement timeouts and limits
- Handle malicious content appropriately

## Performance Guidelines

### Resource Management
- Use try-with-resources for file operations
- Close streams and connections properly
- Implement appropriate timeouts

### Caching Strategy
- Cache system messages and configuration
- Use Quarkus caching annotations where appropriate
- Consider embedding generation caching for repeated content

### Async Processing
- Use async processing for long-running operations
- Implement proper timeout handling
- Consider using Quarkus messaging for decoupled processing

## Code Review Process

### Before Submitting
1. Run `mvn clean compile` to ensure compilation
2. Run `mvn test` to ensure all tests pass
3. Check for deprecated API usage
4. Verify proper error handling
5. Ensure documentation is updated

### Review Checklist
- [ ] Code follows naming conventions
- [ ] Proper error handling implemented
- [ ] Tests added for new functionality
- [ ] Documentation updated
- [ ] No hardcoded values or secrets
- [ ] Performance considerations addressed
- [ ] Security validations in place

### Common Issues to Avoid
- Using deprecated APIs (check Quarkus migration guides)
- Missing input validation
- Inadequate error handling
- Hardcoded configuration values
- Missing tests for critical paths

## Build and Deployment

### Development Workflow
```bash
# Clean build
mvn clean compile

# Run tests
mvn test

# Development mode with hot reload
mvn quarkus:dev

# Package application
mvn package
```

### Configuration Management
- Use `application.properties` for defaults
- Override with environment variables in production
- Document all configuration properties

### Environment-Specific Settings
```properties
# Development
%dev.azure.ai.foundry.endpoint=https://dev-endpoint.azure.com

# Test
%test.azure.ai.foundry.endpoint=https://test-endpoint.azure.com

# Production (use environment variables)
azure.ai.foundry.endpoint=${AZURE_AI_FOUNDRY_ENDPOINT}
```

## Migration and Modernization

### Current Status (✅ Completed)
1. **✅ Quarkus 3.26.2 Upgrade**: Successfully updated to latest LTS with security patches
2. **✅ Java 21 Language Support**: Full support for modern Java features with Java 17 compatibility
3. **✅ Modern Multipart Handling**: Migrated from deprecated `@MultipartForm` to `@RestForm`
4. **✅ Dependency Updates**: LangChain4j 0.25.0, Apache Tika 2.9.1, JSoup 1.16.2
5. **✅ Configuration Cleanup**: Removed invalid Quarkus properties, streamlined configuration

### Priority Technical Debt (🔧 In Progress)

#### URL Constructor Deprecation
```java
// ❌ Current deprecated usage in WebScrapingService
URL url = new URL(urlString); // Deprecated in Java 20+

// ✅ Modern approach
URI uri = URI.create(urlString);
URL url = uri.toURL();

// ✅ Or use URI directly where possible
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create(urlString))
    .build();
```

#### Legacy DTO Conversion to Records
```java
// ❌ Legacy data transfer objects
public class AiAnalysisResponse {
    private String result;
    private String format;
    private Instant timestamp;
    // ... getters, setters, equals, hashCode
}

// ✅ Modern record-based approach
public record AiAnalysisResponse(
    String result,
    String format,
    Instant timestamp,
    Optional<String> errorMessage
) {
    // Compact constructor for validation
    public AiAnalysisResponse {
        Objects.requireNonNull(result, "Result cannot be null");
        Objects.requireNonNull(format, "Format cannot be null");
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        errorMessage = errorMessage == null ? Optional.empty() : errorMessage;
    }
    
    // Static factory methods for common cases
    public static AiAnalysisResponse success(String result, String format) {
        return new AiAnalysisResponse(result, format, Instant.now(), Optional.empty());
    }
    
    public static AiAnalysisResponse error(String errorMessage) {
        return new AiAnalysisResponse("", "text", Instant.now(), Optional.of(errorMessage));
    }
}
```

### Modernization Roadmap (📋 Planned)

#### Phase 1: Language Modernization (Current)
- [ ] Convert remaining DTOs to Records
- [ ] Implement pattern matching in service layer
- [ ] Replace traditional switch statements with switch expressions
- [ ] Adopt text blocks for multi-line strings
- [ ] Fix URL constructor deprecation warnings

#### Phase 2: Architecture Enhancements
- [ ] Implement reactive programming patterns with Mutiny
- [ ] Add comprehensive metrics and observability
- [ ] Introduce circuit breaker patterns for external API calls
- [ ] Implement caching strategies for system messages
- [ ] Add distributed tracing support

#### Phase 3: Advanced Features
- [ ] Native image compilation optimization
- [ ] Kubernetes-native deployment configurations
- [ ] Advanced security features (OAuth 2.0, JWT)
- [ ] Multi-tenant support
- [ ] GraphQL API alternative

### Migration Guidelines

#### Safe Refactoring Practices
1. **Incremental Changes**: Make small, testable changes
2. **Backwards Compatibility**: Maintain API compatibility during transitions
3. **Comprehensive Testing**: Ensure full test coverage before refactoring
4. **Feature Flags**: Use configuration to toggle new implementations
5. **Monitoring**: Track performance and error rates during migration

#### Code Modernization Checklist
- [ ] Replace deprecated API usage
- [ ] Convert classes to records where appropriate
- [ ] Implement pattern matching for type checking
- [ ] Use text blocks for readable multi-line strings
- [ ] Apply switch expressions for cleaner branching logic
- [ ] Leverage var for improved readability
- [ ] Add comprehensive validation using modern Java features

### Performance Optimization Targets

#### Current Baseline
- **Startup Time**: < 3 seconds in JVM mode
- **Memory Usage**: < 200MB heap for basic operations
- **Response Time**: < 500ms for text analysis (< 10KB content)
- **Throughput**: > 100 requests/second for concurrent text analysis

#### Optimization Goals
- **Startup Time**: < 1 second in JVM mode, < 100ms in native mode
- **Memory Usage**: < 150MB heap, < 50MB in native mode
- **Response Time**: < 300ms for text analysis
- **Throughput**: > 500 requests/second with proper scaling

### Future Technology Considerations

#### Java Ecosystem Evolution
- **Virtual Threads**: Evaluate Project Loom integration for improved concurrency
- **Vector API**: Consider vector operations for embedding processing
- **Foreign Function Interface**: Explore native library integration opportunities

#### AI/ML Framework Evolution
- **LangChain4j Updates**: Stay current with framework improvements
- **Azure AI Enhancements**: Adopt new Azure AI capabilities as available
- **Model Optimization**: Implement model caching and optimization strategies

---

## Conclusion

These comprehensive coding instructions serve as the foundation for maintaining high-quality, modern Java code in the SSCodeAgent03 project. They emphasize:

1. **Modern Java Practices**: Leveraging Java 21 features for cleaner, more maintainable code
2. **Enterprise Standards**: Security, performance, and scalability considerations
3. **Developer Experience**: Clear guidelines, comprehensive testing, and excellent tooling
4. **Continuous Improvement**: Regular updates to adopt new technologies and practices

All contributors should familiarize themselves with these guidelines and actively participate in keeping them current. For questions, suggestions, or clarifications about these guidelines, please create an issue in the project repository.

**Remember**: Code is written once but read many times. Prioritize clarity, maintainability, and adherence to these established patterns to ensure long-term project success.