# Quarkus AI Analysis API

This Quarkus application provides a REST API for analyzing content using Azure AI Foundry API with LangChain4j integration.

## Features

- Multi-part file upload support for `.java`, `.txt`, `.pdf`, `.docx`, `.pptx` files
- Web content scraping and analysis
- Text content analysis
- Predefined system messages for SEO analysis and Java code review
- Custom system messages support
- Azure AI Foundry integration using LangChain4j
- Azure embedding generation
- Response formatting (String or JSON)

## API Endpoints

### POST /api/ai/analyze
Analyze content using AI with multipart form data.

**Parameters:**
- `text` (optional): Text content to analyze
- `file` (optional): File to upload and analyze (supported: .java, .txt, .pdf, .docx, .pptx)
- `webUrl` (optional): Web URL to scrape and analyze
- `systemMessageId` (optional): ID of predefined system message (SystemMessage1 or SystemMessage2)
- `customSystemMessage` (optional): Custom system message text
- `responseFormat` (optional): 0 for String (default), 1 for JSON

**Validation Rules:**
- Exactly one of `text`, `file`, or `webUrl` must be provided
- Either `systemMessageId` or `customSystemMessage` must be provided

### GET /api/ai/system-messages
Get all available predefined system messages.

### GET /api/ai/health
Health check endpoint.

## Predefined System Messages

**SystemMessage1**: SEO Analysis
```
Analysoi annettu teksti ja tee siitä hakukoneoptimointi sekä Google hakukonerobotiikan että tekoäly-robottien suhteen. Anna lopuksi viisi ehdotusta kuinka voisi vielä parantaa
```

**SystemMessage2**: Java Code Review
```
Suorita annetulle java -koodille code review. Jos teksti ei ole java-koodia, älä analysoi vaan kerro siitä. Tunnista käytetty jdk-taso (esim jdk 8, 17, 21). Jos kooditaso on alle 17, kerro että suosittelet uudemman jdk-tason käyttöä. Jos näet yleisiä parannusehdotuksia koodissa, lisää ne loppuun
```

## Configuration

Set the following environment variables:

```properties
AZURE_AI_FOUNDRY_ENDPOINT=https://your-foundry-endpoint.azure.com
AZURE_AI_FOUNDRY_API_KEY=your-api-key
AZURE_AI_FOUNDRY_DEPLOYMENT=gpt-4
AZURE_EMBEDDING_ENDPOINT=https://your-embedding-endpoint.azure.com
AZURE_EMBEDDING_API_KEY=your-embedding-api-key
AZURE_EMBEDDING_DEPLOYMENT=text-embedding-ada-002
```

## Running the Application

```bash
# Development mode
./mvnw compile quarkus:dev

# Package and run
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

## Example Usage

### Analyze Text with SystemMessage1
```bash
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "text=This is a sample text for SEO analysis" \
  -F "systemMessageId=SystemMessage1" \
  -F "responseFormat=0"
```

### Analyze Java File with SystemMessage2
```bash
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "file=@MyClass.java" \
  -F "systemMessageId=SystemMessage2" \
  -F "responseFormat=1"
```

### Analyze Web Content
```bash
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "webUrl=https://example.com" \
  -F "customSystemMessage=Analyze this web content" \
  -F "responseFormat=0"
```

## Recent Updates

### Version 1.0.0-SNAPSHOT
- **Fixed deprecated API usage**: Replaced deprecated `@MultipartForm` with `@RestForm` annotations for better Quarkus 3.6+ compatibility
- **Configuration cleanup**: Removed invalid `quarkus.http.body.multipart.uploads-enabled` property
- **Enhanced testing**: Added validation tests for input parameters
- **Added coding standards**: Comprehensive coding instructions document added to ensure consistent development practices

## OpenAPI Documentation

When running in development mode, visit:
- Swagger UI: http://localhost:8080/q/swagger-ui/
- OpenAPI Spec: http://localhost:8080/q/openapi