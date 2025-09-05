package com.example.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Set;

/**
 * Service for parsing files using Apache Tika with modern Java 21 features
 */
@ApplicationScoped
public class FileParsingService {
    
    private final Tika tika;
    
    // Modern Set initialization with immutable collections
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
        ".java", ".txt", ".pdf", ".docx", ".pptx"
    );
    
    // Maximum file size (50MB)
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024L;
    
    public FileParsingService() {
        this.tika = new Tika();
    }
    
    /**
     * Parse file content from FileUpload with enhanced validation
     */
    public String parseFile(FileUpload fileUpload) throws IOException, TikaException {
        validateFileUpload(fileUpload);
        
        try (InputStream inputStream = Files.newInputStream(fileUpload.uploadedFile())) {
            return tika.parseToString(inputStream);
        }
    }
    
    /**
     * Enhanced file validation using modern Java patterns
     */
    private void validateFileUpload(FileUpload fileUpload) {
        if (fileUpload == null) {
            throw new IllegalArgumentException("File upload cannot be null");
        }
        
        var fileName = fileUpload.fileName();
        if (!isAllowedFileType(fileName)) {
            throw new IllegalArgumentException(
                "File type not allowed. Supported types: %s".formatted(ALLOWED_EXTENSIONS)
            );
        }
        
        var fileSize = fileUpload.size();
        if (fileSize > MAX_FILE_SIZE) {
            var fileSizeMB = fileSize / (1024.0 * 1024.0);
            var maxSizeMB = MAX_FILE_SIZE / (1024.0 * 1024.0);
            throw new IllegalArgumentException(
                "File size (%.2f MB) exceeds maximum allowed size (%.2f MB)".formatted(fileSizeMB, maxSizeMB)
            );
        }
    }
    
    /**
     * Check if file type is allowed using modern stream operations
     */
    public boolean isAllowedFileType(String fileName) {
        if (fileName == null) {
            return false;
        }
        
        var lowerCaseFileName = fileName.toLowerCase();
        return ALLOWED_EXTENSIONS.stream()
            .anyMatch(lowerCaseFileName::endsWith);
    }
    
    /**
     * Get file extension using modern String methods
     */
    public String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    /**
     * Get supported file extensions (returns immutable copy)
     */
    public Set<String> getSupportedExtensions() {
        return ALLOWED_EXTENSIONS; // Already immutable
    }
    
    /**
     * Get maximum file size
     */
    public long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }
}