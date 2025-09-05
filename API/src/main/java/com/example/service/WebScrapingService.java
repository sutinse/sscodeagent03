package com.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

/**
 * Service for scraping web content
 */
@ApplicationScoped
public class WebScrapingService {
    
    private final Client client;
    
    public WebScrapingService() {
        this.client = ClientBuilder.newClient();
    }
    
    /**
     * Scrape text content from a web URL
     */
    public String scrapeWebContent(String url) throws IOException {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        
        try {
            // Use Jsoup to parse HTML and extract text
            Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000)
                .get();
            
            // Extract text content, removing script and style elements
            doc.select("script").remove();
            doc.select("style").remove();
            
            return doc.body().text();
            
        } catch (Exception e) {
            throw new IOException("Failed to scrape content from URL: " + url, e);
        }
    }
    
    /**
     * Validate URL format using modern URI API (fixes Java 20+ deprecation)
     */
    public boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Modern approach: Use URI first, then convert to URL if needed
            var uri = java.net.URI.create(url);
            
            // Validate that it's a valid URI with scheme and host
            if (uri.getScheme() == null || uri.getHost() == null) {
                return false;
            }
            
            // Check that it's HTTP or HTTPS
            var scheme = uri.getScheme().toLowerCase();
            return "http".equals(scheme) || "https".equals(scheme);
            
        } catch (Exception e) {
            return false;
        }
    }
}