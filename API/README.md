# Quarkus AI Analysis API

A production-ready, cloud-native REST API for intelligent content analysis powered by Azure AI Foundry and built on Quarkus. This enterprise-grade application provides comprehensive AI analysis capabilities with support for multiple content formats and configurable analysis workflows.

## 🚀 Features

### Content Analysis Capabilities
- **📄 Multi-format File Support**: Process `.java`, `.txt`, `.pdf`, `.docx`, `.pptx` files with automatic content extraction
- **🌐 Web Content Analysis**: Intelligent scraping and analysis of web pages and online content
- **📝 Direct Text Analysis**: Real-time analysis of text content via API
- **🎯 Predefined Analysis Types**: Built-in system messages for SEO optimization and Java code review
- **🔧 Custom Analysis Workflows**: Support for user-defined system messages and analysis parameters
- **📊 Flexible Output Formats**: Response formatting in plain text or structured JSON

### Enterprise Features
- **🤖 Azure AI Integration**: Seamless integration with Azure AI Foundry for advanced language models
- **🔐 Secure File Handling**: Comprehensive validation, size limits, and type checking
- **📚 Interactive Documentation**: OpenAPI/Swagger integration with live API testing
- **🏥 Health Monitoring**: Built-in health checks and system status endpoints
- **⚡ High Performance**: Optimized for fast processing and low latency

## 📋 API Endpoints

### Core Analysis Endpoint

#### POST `/api/ai/analyze`
Performs AI-powered content analysis with comprehensive input validation and flexible configuration options.

**Content-Type**: `multipart/form-data`

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `text` | string | conditional* | Direct text content for analysis |
| `file` | file | conditional* | File upload (max 50MB, supported: .java, .txt, .pdf, .docx, .pptx) |
| `webUrl` | string | conditional* | URL for web content scraping and analysis |
| `systemMessageId` | string | conditional** | ID of predefined system message (`SystemMessage1` or `SystemMessage2`) |
| `customSystemMessage` | string | conditional** | Custom analysis instructions |
| `responseFormat` | integer | optional | Response format: `0` for text (default), `1` for JSON |

**Validation Rules**:
- *Exactly one content source (`text`, `file`, or `webUrl`) must be provided
- **Either `systemMessageId` or `customSystemMessage` must be provided
- File size limit: 50MB maximum
- Supported file types validated by content and extension
- URLs validated for proper format and accessibility

**Response Format**:
```json
{
  "result": "Analysis results...",
  "timestamp": "2024-01-01T12:00:00Z",
  "format": "text|json",
  "contentType": "detected content type",
  "processingTimeMs": 1250
}
```

### System Information Endpoints

#### GET `/api/ai/system-messages`
Retrieves all available predefined system messages with descriptions and use cases.

**Response**:
```json
{
  "SystemMessage1": {
    "name": "SEO Analysis",
    "description": "Comprehensive SEO analysis with actionable recommendations",
    "language": "Finnish",
    "useCase": "Blog posts, web content, marketing materials"
  },
  "SystemMessage2": {
    "name": "Java Code Review",
    "description": "Detailed Java code analysis with best practices and JDK recommendations", 
    "language": "Finnish",
    "useCase": "Java source files, code snippets, technical documentation"
  }
}
```

#### GET `/api/ai/health`
Application health check endpoint for monitoring and load balancer integration.

**Response**:
```json
{
  "status": "UP",
  "timestamp": "2024-01-01T12:00:00Z",
  "version": "1.0.0-SNAPSHOT",
  "uptime": "PT2H30M15S",
  "dependencies": {
    "azureAi": "UP",
    "fileSystem": "UP"
  }
}
```

## Predefined System Messages

**SystemMessage1**: SEO Analysis
```
Analysoi annettu teksti ja tee siitä hakukoneoptimointi sekä Google hakukonerobotiikan että tekoäly-robottien suhteen. Anna lopuksi viisi ehdotusta kuinka voisi vielä parantaa
```

**SystemMessage2**: Java Code Review
```
Suorita annetulle java -koodille code review. Jos teksti ei ole java-koodia, älä analysoi vaan kerro siitä. Tunnista käytetty jdk-taso (esim jdk 8, 17, 21). Jos kooditaso on alle 17, kerro että suosittelet uudemman jdk-tason käyttöä. Jos näet yleisiä parannusehdotuksia koodissa, lisää ne loppuun
```

## 🔧 Configuration

### Environment Variables
Configure the application using the following environment variables:

#### Azure AI Foundry Configuration
```bash
# Required: Azure AI Foundry endpoint
export AZURE_AI_FOUNDRY_ENDPOINT="https://your-foundry-endpoint.azure.com"

# Required: Azure AI Foundry API key  
export AZURE_AI_FOUNDRY_API_KEY="your-api-key"

# Required: Model deployment name
export AZURE_AI_FOUNDRY_DEPLOYMENT="gpt-4"

# Optional: Request timeout (default: 30s)
export AZURE_AI_FOUNDRY_TIMEOUT="30s"

# Optional: Maximum retry attempts (default: 3)
export AZURE_AI_FOUNDRY_MAX_RETRIES="3"
```

#### Azure Embedding Configuration
```bash
# Required: Azure embedding service endpoint
export AZURE_EMBEDDING_ENDPOINT="https://your-embedding-endpoint.azure.com"

# Required: Azure embedding API key
export AZURE_EMBEDDING_API_KEY="your-embedding-api-key"

# Required: Embedding model deployment
export AZURE_EMBEDDING_DEPLOYMENT="text-embedding-ada-002"
```

#### Application Configuration
```bash
# Optional: Server port (default: 8080)
export QUARKUS_HTTP_PORT="8080"

# Optional: Log level (default: INFO)
export QUARKUS_LOG_LEVEL="INFO"

# Optional: Maximum file upload size (default: 50MB)
export QUARKUS_HTTP_LIMITS_MAX_BODY_SIZE="50M"
```

### Configuration Validation
The application validates all configuration on startup and provides clear error messages for missing or invalid settings:

```bash
# Validate configuration
curl http://localhost:8080/api/ai/health

# Check specific configuration
curl http://localhost:8080/q/health/ready
```

## 🚀 Running the Application

### Prerequisites
- **Java 21** (recommended) or **Java 17** (minimum)
- **Maven 3.8+**
- **Azure AI Foundry account** with API access

### Development Mode
```bash
# Navigate to API directory
cd API

# Start development server with hot reload
mvn quarkus:dev

# Alternative: Start with custom JVM arguments
mvn quarkus:dev -Dquarkus.args="-Xmx2g"

# Start with debug logging
mvn quarkus:dev -Dquarkus.log.level=DEBUG
```

**Development Features**:
- 🔄 **Hot Reload**: Automatic code reloading on changes
- 🔧 **Dev UI**: Access development tools at http://localhost:8080/q/dev/
- 📊 **Live Metrics**: Real-time application metrics
- 🐛 **Debug Mode**: Enhanced logging and error reporting

### Production Deployment
```bash
# Build the application
mvn clean package

# Run the packaged application
java -jar target/quarkus-app/quarkus-run.jar

# Run with production optimizations
java -XX:+UseG1GC -Xmx1g -jar target/quarkus-app/quarkus-run.jar
```

### Docker Deployment
```bash
# Build Docker image
docker build -f src/main/docker/Dockerfile.jvm -t ai-analysis-api .

# Run container
docker run -p 8080:8080 \
  -e AZURE_AI_FOUNDRY_ENDPOINT="https://your-endpoint.azure.com" \
  -e AZURE_AI_FOUNDRY_API_KEY="your-api-key" \
  -e AZURE_AI_FOUNDRY_DEPLOYMENT="gpt-4" \
  ai-analysis-api
```

### Java Version Compatibility
```bash
# Check current Java version
java -version

# Build with specific Java version (if needed)
mvn clean compile -Dmaven.compiler.release=17

# Verify compatibility
mvn test -Dmaven.compiler.release=17
```

## 💡 Example Usage

### Basic Text Analysis
```bash
# SEO analysis with predefined system message
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "text=This is a sample blog post about cloud computing and microservices architecture." \
  -F "systemMessageId=SystemMessage1" \
  -F "responseFormat=0"
```

### Java Code Review
```bash
# Analyze Java source file with structured JSON response
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "file=@src/main/java/com/example/MyService.java" \
  -F "systemMessageId=SystemMessage2" \
  -F "responseFormat=1"
```

### Web Content Analysis
```bash
# Analyze web page content with custom analysis instructions
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "webUrl=https://quarkus.io/guides/getting-started" \
  -F "customSystemMessage=Analyze this technical documentation for clarity, completeness, and beginner-friendliness. Provide specific recommendations for improvement." \
  -F "responseFormat=1"
```

### Advanced Custom Analysis
```bash
# Multi-criteria analysis with detailed instructions
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "text=Your technical content here..." \
  -F "customSystemMessage=Perform a comprehensive analysis focusing on: 1) Technical accuracy, 2) Writing clarity, 3) Target audience appropriateness, 4) Actionability of recommendations, 5) Missing information or gaps" \
  -F "responseFormat=1"
```

### Bulk File Processing
```bash
# Process multiple files in sequence
for file in *.java; do
  echo "Analyzing $file..."
  curl -X POST http://localhost:8080/api/ai/analyze \
    -F "file=@$file" \
    -F "systemMessageId=SystemMessage2" \
    -F "responseFormat=1" \
    -H "Accept: application/json"
  echo "Completed $file"
done
```

### Health and Status Checks
```bash
# Check application health
curl http://localhost:8080/api/ai/health

# Get available system messages
curl http://localhost:8080/api/ai/system-messages

# Check OpenAPI specification
curl http://localhost:8080/q/openapi
```

## 🔍 Troubleshooting

### Common Issues and Solutions

#### Build and Compilation Issues
```bash
# Issue: Java version compatibility error
# Error: "release version 21 not supported"
# Solution: Use Java 17 override or install Java 21
mvn clean compile -Dmaven.compiler.release=17

# Issue: Dependencies not found
# Solution: Clean install dependencies
mvn clean install -U
```

#### Runtime Configuration Issues
```bash
# Issue: Azure AI connection failures
# Check: Verify environment variables
echo $AZURE_AI_FOUNDRY_ENDPOINT
echo $AZURE_AI_FOUNDRY_API_KEY

# Solution: Test configuration
curl -X GET http://localhost:8080/api/ai/health

# Issue: File upload failures
# Check: File size and type restrictions
# - Maximum file size: 50MB
# - Allowed types: .java, .txt, .pdf, .docx, .pptx
```

#### Performance Issues
```bash
# Issue: Slow analysis responses
# Solution: Increase JVM memory and timeouts
export MAVEN_OPTS="-Xmx2g -XX:+UseG1GC"
export AZURE_AI_FOUNDRY_TIMEOUT="60s"

# Issue: High memory usage
# Solution: Monitor and tune garbage collection
java -XX:+UseG1GC -XX:+PrintGC -jar target/quarkus-app/quarkus-run.jar
```

### Debug Mode
```bash
# Enable comprehensive debug logging
mvn quarkus:dev -Dquarkus.log.level=DEBUG

# Enable specific category logging
mvn quarkus:dev -Dquarkus.log.category."com.example".level=DEBUG

# Monitor HTTP requests
mvn quarkus:dev -Dquarkus.log.category."io.quarkus.http".level=DEBUG
```

### Health Monitoring
```bash
# Application health check
curl http://localhost:8080/api/ai/health

# Quarkus health endpoints
curl http://localhost:8080/q/health/live    # Liveness probe
curl http://localhost:8080/q/health/ready   # Readiness probe

# Application metrics
curl http://localhost:8080/q/metrics
```

## 📈 Recent Updates

### Version 1.0.0-SNAPSHOT (Latest)
**🚀 Major Improvements:**
- **✅ Quarkus 3.26.2 Upgrade**: Latest LTS version with enhanced security and performance
- **✅ Java 21 Support**: Full support for modern Java features with backward compatibility to Java 17
- **✅ Modern API Design**: Migrated from deprecated `@MultipartForm` to `@RestForm` annotations
- **✅ Enhanced Documentation**: Comprehensive API documentation with interactive examples
- **✅ Improved Configuration**: Streamlined environment variable configuration with validation

**🔧 Technical Enhancements:**
- **Dependency Updates**: LangChain4j 0.25.0, Apache Tika 2.9.1, JSoup 1.16.2
- **Configuration Cleanup**: Removed invalid Quarkus properties for better compatibility
- **Enhanced Testing**: Comprehensive validation tests for all input parameters
- **Code Quality**: Added comprehensive coding standards and development guidelines

**🐛 Bug Fixes:**
- Fixed deprecated API usage warnings
- Improved error handling for file upload validation
- Enhanced timeout handling for long-running analysis operations

## 📚 Additional Resources

### Documentation Links
- **📖 [Main Project README](../README.md)**: Comprehensive project overview and setup
- **👨‍💻 [Coding Instructions](../CODING_INSTRUCTIONS.md)**: Development standards and best practices
- **🚀 [JDK 24 Support](./README-JDK24.md)**: Future Java version compatibility notes

### Development Tools
- **🔧 Swagger UI**: http://localhost:8080/q/swagger-ui/ (when running)
- **📊 Development UI**: http://localhost:8080/q/dev/ (development mode only)
- **📈 Metrics**: http://localhost:8080/q/metrics
- **🏥 Health Checks**: http://localhost:8080/q/health

### External Resources
- **[Quarkus Documentation](https://quarkus.io/guides/)**
- **[LangChain4j Documentation](https://docs.langchain4j.dev/)**
- **[Azure AI Foundry Documentation](https://docs.microsoft.com/en-us/azure/ai-services/)**

---

**⚡ Built with Quarkus for lightning-fast startup and optimal resource utilization**