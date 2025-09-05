# sscodeagent03

A Quarkus-based AI analysis API that provides intelligent content analysis using Azure AI Foundry.

## Features

- **Multi-format Input**: Support for text, file uploads (.java, .txt, .pdf, .docx, .pptx), and web URLs
- **AI-Powered Analysis**: Integration with Azure AI Foundry for advanced content analysis
- **Configurable System Messages**: Predefined messages for SEO analysis and Java code review
- **RESTful API**: Clean, well-documented REST endpoints with OpenAPI/Swagger support
- **Modern Stack**: Built with Quarkus 3.26.2, Java 21, and modern cloud-native technologies

## Quick Start

```bash
cd API
mvn quarkus:dev
```

Visit http://localhost:8080/q/swagger-ui/ for API documentation.

## Documentation

- [API Documentation](./API/README.md) - Detailed API usage and configuration
- [Coding Instructions](./CODING_INSTRUCTIONS.md) - Development standards and guidelines

## Development

This project follows strict coding standards and best practices. Please review the [Coding Instructions](./CODING_INSTRUCTIONS.md) before contributing.

### Key Technologies
- Quarkus 3.26.2 (Cloud-native Java framework)
- Java 21 (Modern Java features and performance)
- Azure AI Foundry (AI/ML capabilities)
- LangChain4j (AI integration framework)
- Maven (Build and dependency management)