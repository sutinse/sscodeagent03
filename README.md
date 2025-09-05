# SSCodeAgent03 - AI Content Analysis API

A modern, cloud-native AI analysis API built with Quarkus that provides intelligent content analysis using Azure AI Foundry. This enterprise-ready application offers comprehensive content analysis capabilities with support for multiple input formats, configurable analysis workflows, and seamless Azure AI integration.

## 🚀 Features

### Core Capabilities
- **📄 Multi-format Input Support**: Analyze text, files (.java, .txt, .pdf, .docx, .pptx), and web content
- **🤖 Azure AI Foundry Integration**: Leverages advanced AI models for content analysis and embedding generation
- **⚙️ Configurable Analysis Workflows**: Predefined system messages for SEO analysis and Java code review
- **🔧 Custom Analysis**: Support for custom system messages and analysis configurations
- **📊 Flexible Output Formats**: Response formatting in plain text or structured JSON

### Technical Features
- **🏗️ Modern Architecture**: Built with Quarkus 3.26.2 for cloud-native performance
- **☕ Java 21 Ready**: Leverages modern Java features for enhanced performance and developer experience
- **📚 Comprehensive API Documentation**: OpenAPI/Swagger integration with interactive documentation
- **🔐 Enterprise Security**: Input validation, file type restrictions, and secure configuration management
- **⚡ High Performance**: Optimized for fast startup times and low memory footprint

## 🏁 Quick Start

### Prerequisites
- **Java 21** (recommended) or Java 17 (compatible)
- **Maven 3.8+**
- **Azure AI Foundry Account** with API access

### Environment Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/sutinse/sscodeagent03.git
   cd sscodeagent03
   ```

2. **Configure Azure AI Foundry** (required for AI functionality):
   ```bash
   export AZURE_AI_FOUNDRY_ENDPOINT="https://your-foundry-endpoint.azure.com"
   export AZURE_AI_FOUNDRY_API_KEY="your-api-key"
   export AZURE_AI_FOUNDRY_DEPLOYMENT="gpt-4"
   export AZURE_EMBEDDING_ENDPOINT="https://your-embedding-endpoint.azure.com"
   export AZURE_EMBEDDING_API_KEY="your-embedding-api-key"
   export AZURE_EMBEDDING_DEPLOYMENT="text-embedding-ada-002"
   ```

3. **Start the development server**:
   ```bash
   cd API
   mvn quarkus:dev
   ```

4. **Access the application**:
   - **API**: http://localhost:8080/api/ai/analyze
   - **Swagger UI**: http://localhost:8080/q/swagger-ui/
   - **Health Check**: http://localhost:8080/api/ai/health

### Quick API Test
```bash
# Analyze text with predefined SEO analysis
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "text=Your content here" \
  -F "systemMessageId=SystemMessage1" \
  -F "responseFormat=0"
```

## 📋 Documentation

- [API Documentation](./API/README.md) - Comprehensive API usage, endpoints, and configuration
- [Coding Instructions](./CODING_INSTRUCTIONS.md) - Development standards, guidelines, and best practices
- [JDK 24 Support](./API/README-JDK24.md) - Future compatibility and configuration notes

## 🏗️ Architecture Overview

### System Components
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client App    │    │  Quarkus API     │    │  Azure AI       │
│                 │◄──►│                  │◄──►│  Foundry        │
│ Web/Mobile/CLI  │    │  - REST Endpoints│    │  - GPT Models   │
└─────────────────┘    │  - File Upload   │    │  - Embeddings   │
                       │  - Web Scraping  │    └─────────────────┘
┌─────────────────┐    │  - Validation    │    
│   File System   │◄──►│  - Processing    │    ┌─────────────────┐
│                 │    └──────────────────┘    │  External Web   │
│ .java,.txt,     │                            │  Content        │
│ .pdf,.docx      │                            │                 │
└─────────────────┘                            └─────────────────┘
```

### Technology Stack
- **Framework**: Quarkus 3.26.2 (Cloud-native Java framework)
- **Language**: Java 21 (with Java 17 compatibility)
- **AI Integration**: LangChain4j + Azure AI Foundry
- **File Processing**: Apache Tika
- **Web Scraping**: JSoup
- **API Documentation**: OpenAPI 3 + Swagger UI
- **Build Tool**: Maven 3.x
- **Testing**: JUnit 5 + REST Assured

### Key Design Patterns
- **Dependency Injection**: CDI (Contexts and Dependency Injection)
- **REST-first Design**: JAX-RS with JSON responses
- **Configuration-driven**: External configuration via environment variables
- **Microservice Ready**: Optimized for containerization and cloud deployment

## 💻 Development

This project follows strict coding standards and best practices to ensure high-quality, maintainable code. Please review the [Coding Instructions](./CODING_INSTRUCTIONS.md) before contributing.

### Development Workflow
1. **Setup Development Environment**:
   ```bash
   # Ensure Java 21 is installed (or Java 17 minimum)
   java -version
   
   # Clone and setup
   git clone https://github.com/sutinse/sscodeagent03.git
   cd sscodeagent03/API
   ```

2. **Development Mode** (with hot reload):
   ```bash
   mvn quarkus:dev
   ```
   
3. **Testing**:
   ```bash
   # Run all tests
   mvn test
   
   # Run with coverage
   mvn test -Pcoverage
   ```

4. **Build for Production**:
   ```bash
   mvn package
   java -jar target/quarkus-app/quarkus-run.jar
   ```

### Code Quality Standards
- **Java 21 Features**: Leverages records, pattern matching, text blocks, and switch expressions
- **Test Coverage**: Minimum 80% coverage for service layer
- **Code Style**: Enforced formatting and import organization
- **Documentation**: Comprehensive JavaDoc for public APIs
- **Security**: Input validation and secure configuration practices

### Key Technologies
- **Quarkus 3.26.2** - Cloud-native Java framework with fast startup
- **Java 21** - Modern Java with enhanced language features
- **Azure AI Foundry** - Advanced AI/ML capabilities and embedding generation
- **LangChain4j** - Java framework for AI application development
- **Apache Tika** - Content detection and extraction
- **JSoup** - HTML parsing and web scraping
- **Maven** - Build and dependency management

## 🔧 API Usage Examples

### Analyze Text Content
```bash
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "text=This is a blog post about cloud computing and microservices." \
  -F "systemMessageId=SystemMessage1" \
  -F "responseFormat=1"
```

### Analyze Java Code File
```bash
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "file=@src/main/java/com/example/MyClass.java" \
  -F "systemMessageId=SystemMessage2" \
  -F "responseFormat=1"
```

### Analyze Web Content
```bash
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "webUrl=https://quarkus.io/guides/getting-started" \
  -F "customSystemMessage=Analyze this technical documentation for clarity and completeness" \
  -F "responseFormat=0"
```

### Custom Analysis
```bash
curl -X POST http://localhost:8080/api/ai/analyze \
  -F "text=Your content here" \
  -F "customSystemMessage=Provide a detailed technical review focusing on architecture and scalability" \
  -F "responseFormat=1"
```

### Available System Messages
- **SystemMessage1**: SEO Analysis - Analyzes content for search engine optimization
- **SystemMessage2**: Java Code Review - Reviews Java code for best practices and JDK compatibility

## 🔍 Troubleshooting

### Common Issues

#### Build Failures
```bash
# Issue: Java version compatibility
# Solution: Use Java 17 minimum, Java 21 recommended
mvn clean compile -Dmaven.compiler.release=17

# Issue: Missing dependencies
# Solution: Clean install
mvn clean install
```

#### Runtime Configuration
```bash
# Issue: Azure AI connection failures
# Check: Environment variables are set
echo $AZURE_AI_FOUNDRY_ENDPOINT
echo $AZURE_AI_FOUNDRY_API_KEY

# Solution: Verify configuration
curl -X GET http://localhost:8080/api/ai/health
```

#### File Upload Issues
- **Max file size**: 50MB limit enforced
- **Supported formats**: .java, .txt, .pdf, .docx, .pptx
- **Validation**: Ensure file extension matches content type

### Debug Mode
```bash
# Enable debug logging
mvn quarkus:dev -Dquarkus.log.level=DEBUG

# Check application health
curl http://localhost:8080/api/ai/health
```

### Performance Tuning
```bash
# Increase memory for large files
export MAVEN_OPTS="-Xmx2g"
mvn quarkus:dev

# Monitor application metrics
curl http://localhost:8080/q/metrics
```

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### Getting Started
1. **Fork the repository** and create a feature branch
2. **Read the [Coding Instructions](./CODING_INSTRUCTIONS.md)** thoroughly
3. **Setup your development environment** following the steps above
4. **Run tests** to ensure everything works: `mvn test`

### Development Guidelines
- Follow Java 21 coding standards and conventions
- Write comprehensive tests for new features
- Update documentation for any API changes
- Ensure all tests pass before submitting PR
- Use meaningful commit messages

### Pull Request Process
1. Create a feature branch: `git checkout -b feature/your-feature-name`
2. Make your changes following coding standards
3. Add/update tests as needed
4. Update documentation if required
5. Run full test suite: `mvn clean test`
6. Submit PR with clear description of changes

### Code Review Checklist
- [ ] Code follows project conventions
- [ ] Tests added for new functionality
- [ ] Documentation updated
- [ ] No hardcoded values or secrets
- [ ] Performance considerations addressed
- [ ] Security validations in place

## 📄 License

This project is licensed under the MIT License. See LICENSE file for details.

## 📞 Support

- **Issues**: Report bugs and feature requests via [GitHub Issues](https://github.com/sutinse/sscodeagent03/issues)
- **Documentation**: Comprehensive guides in the [/API/README.md](./API/README.md)
- **Development**: Review [CODING_INSTRUCTIONS.md](./CODING_INSTRUCTIONS.md) for development guidelines

---

**Built with ❤️ using Quarkus, Java 21, and Azure AI Foundry**