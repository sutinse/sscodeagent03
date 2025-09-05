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

## Project Overview

This project is a Quarkus-based REST API that provides AI analysis capabilities using Azure AI Foundry. The API supports multiple input types (text, file uploads, web URLs) and provides AI-powered analysis with configurable system messages.

### Key Features
- Multi-format file upload support (.java, .txt, .pdf, .docx, .pptx)
- Web content scraping and analysis
- Azure AI Foundry integration via LangChain4j
- OpenAPI/Swagger documentation
- Configurable system messages for different analysis types

## Technology Stack

- **Framework**: Quarkus 3.6.4
- **Java Version**: 17 (minimum)
- **Build Tool**: Maven 3.x
- **Testing**: JUnit 5, Rest Assured
- **Documentation**: OpenAPI 3, Swagger UI
- **AI Integration**: LangChain4j, Azure AI Foundry
- **File Processing**: Apache Tika
- **Web Scraping**: JSoup

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
import java.util.HashMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.example.model.AiAnalysisRequest;

@ApplicationScoped
public class ExampleService {
    
    private static final int MAX_RETRY_ATTEMPTS = 3;
    
    @Inject
    AnotherService anotherService;
    
    public Map<String, String> processData(AiAnalysisRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        Map<String, String> result = new HashMap<>();
        // Implementation here
        return result;
    }
}
```

### Class Design Principles
- **Single Responsibility**: Each class should have one reason to change
- **Dependency Injection**: Use CDI annotations (`@Inject`, `@ApplicationScoped`)
- **Immutability**: Prefer immutable objects when possible
- **Null Safety**: Always validate inputs and handle null cases
- **Exception Handling**: Use appropriate exception types and meaningful messages

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
**Note**: Replace deprecated `@MultipartForm` with current Quarkus multipart handling:

```java
// Preferred approach
@POST
@Consumes(MediaType.MULTIPART_FORM_DATA)
public Response uploadFile(@RestForm("file") FileUpload file,
                          @RestForm("text") String text) {
    // Implementation
}
```

## Testing Standards

### Test Structure
- Use JUnit 5 for all tests
- Follow Given-When-Then pattern in test methods
- Use `@QuarkusTest` for integration tests
- Use `@QuarkusUnitTest` for unit tests with minimal context

### Test Naming
```java
@Test
void shouldReturnSuccessWhenValidRequestProvided() {
    // Given
    AiAnalysisRequest request = createValidRequest();
    
    // When
    Response response = resource.analyzeContent(request);
    
    // Then
    assertEquals(200, response.getStatus());
}
```

### Test Categories
1. **Unit Tests**: Test individual methods in isolation
2. **Integration Tests**: Test component interactions
3. **API Tests**: Test REST endpoints end-to-end

### Coverage Requirements
- Minimum 80% line coverage for service layer
- All public methods in services must have tests
- Critical business logic must have comprehensive test cases

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

## Migration Notes

### Current Technical Debt
1. **Deprecated MultipartForm**: Replace with `@RestForm` annotations
2. **Invalid Configuration**: Remove `quarkus.http.body.multipart.uploads-enabled`
3. **Missing Validation**: Add comprehensive input validation
4. **Limited Testing**: Expand test coverage

### Immediate Actions Required
1. Update multipart form handling
2. Fix configuration warnings
3. Add comprehensive input validation
4. Expand test coverage
5. Implement proper error handling patterns

---

## Conclusion

These coding instructions provide a foundation for maintaining high-quality, consistent code in the SSCodeAgent03 project. All contributors should familiarize themselves with these guidelines and ensure adherence during development.

For questions or suggestions about these guidelines, please create an issue in the project repository.