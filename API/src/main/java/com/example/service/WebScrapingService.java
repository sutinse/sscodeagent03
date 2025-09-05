package com.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Service for scraping web content using modern Java 21 patterns
 */
@ApplicationScoped
public class WebScrapingService {
    
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final int MAX_CONTENT_LENGTH = 1_000_000; // 1MB max
    private static final Set<String> ALLOWED_PROTOCOLS = Set.of("http", "https");
    
    private static final String USER_AGENT = """
        Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 \
        (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\
        """;
    
    private final Client client;
    
    public WebScrapingService() {
        this.client = ClientBuilder.newClient();
    }
    
    /**
     * Scrape text content from a web URL with enhanced validation and error handling
     */
    public String scrapeWebContent(String url) throws IOException {
        Objects.requireNonNull(url, "URL cannot be null");
        
        var trimmedUrl = url.trim();
        if (trimmedUrl.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        
        validateUrl(trimmedUrl)
            .orElseThrow(() -> new IllegalArgumentException("Invalid URL format: " + trimmedUrl));
        
        try {
            var document = Jsoup.connect(trimmedUrl)
                .userAgent(USER_AGENT.strip()) // Remove line breaks from text block
                .timeout((int) DEFAULT_TIMEOUT.toMillis())
                .maxBodySize(MAX_CONTENT_LENGTH)
                .get();
            
            return extractTextContent(document);
            
        } catch (Exception e) {
            throw new IOException("Failed to scrape content from URL: " + trimmedUrl, e);
        }
    }
    
    /**
     * Extract clean text content from HTML document using modern patterns
     */
    private String extractTextContent(Document document) {
        Objects.requireNonNull(document, "Document cannot be null");
        
        // Remove unwanted elements using modern chaining
        document.select("script, style, nav, footer, header, aside")
            .forEach(element -> element.remove());
        
        var bodyText = Optional.ofNullable(document.body())
            .map(body -> body.text())
            .orElse("");
        
        if (bodyText.length() > MAX_CONTENT_LENGTH) {
            return bodyText.substring(0, MAX_CONTENT_LENGTH) + "... [content truncated]";
        }
        
        return bodyText;
    }
    
    /**
     * Validate URL format using modern URI API with enhanced checks
     */
    public Optional<URI> validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            var uri = URI.create(url.trim());
            
            // Enhanced validation using pattern matching concepts
            return switch (uri.getScheme()) {
                case String scheme when ALLOWED_PROTOCOLS.contains(scheme.toLowerCase()) -> {
                    if (uri.getHost() != null && !uri.getHost().trim().isEmpty()) {
                        yield Optional.of(uri);
                    }
                    yield Optional.empty();
                }
                case null -> Optional.empty();
                default -> Optional.empty();
            };
            
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Check if URL is valid (legacy method for backward compatibility)
     */
    public boolean isValidUrl(String url) {
        return validateUrl(url).isPresent();
    }
    
    /**
     * Get URL information with enhanced details
     */
    public Optional<UrlInfo> getUrlInfo(String url) {
        return validateUrl(url)
            .map(uri -> new UrlInfo(
                uri.toString(),
                uri.getScheme(),
                uri.getHost(),
                uri.getPath(),
                isSecureUrl(uri)
            ));
    }
    
    /**
     * Check if URL uses secure protocol
     */
    private boolean isSecureUrl(URI uri) {
        return "https".equals(uri.getScheme());
    }
    
    /**
     * Test URL accessibility without full content scraping
     */
    public boolean isUrlAccessible(String url) {
        return validateUrl(url)
            .map(uri -> {
                try {
                    var document = Jsoup.connect(uri.toString())
                        .userAgent(USER_AGENT.strip())
                        .timeout(5000)
                        .execute();
                    return document.statusCode() == 200;
                } catch (Exception e) {
                    return false;
                }
            })
            .orElse(false);
    }
    
    /**
     * Record representing URL information
     */
    public record UrlInfo(
        String url,
        String scheme,
        String host,
        String path,
        boolean isSecure
    ) {
        public UrlInfo {
            Objects.requireNonNull(url, "URL cannot be null");
            Objects.requireNonNull(scheme, "Scheme cannot be null");
            Objects.requireNonNull(host, "Host cannot be null");
            // Path can be null
        }
        
        public String getDomain() {
            return host;
        }
        
        public boolean isHttps() {
            return isSecure;
        }
        
        public String getDisplayUrl() {
            return "%s://%s%s".formatted(scheme, host, path != null ? path : "");
        }
    }
}