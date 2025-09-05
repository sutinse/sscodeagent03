package com.example.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

/**
 * Service for parsing files using Apache Tika with modern Java 21 patterns
 */
@ApplicationScoped
public class FileParsingService {
    
    private static final long MAX_FILE_SIZE = 50_000_000L; // 50MB
    
    // Modern immutable List initialization
    private static final List<String> ALLOWED_EXTENSIONS = List.of(
        ".java", ".txt", ".pdf", ".docx", ".pptx"
    );
    
    private final Tika tika;
    
    public FileParsingService() {
        this.tika = new Tika();
    }
    
    /**
     * Parse file content from FileUpload with enhanced validation
     */
    public String parseFile(FileUpload fileUpload) throws IOException, TikaException {
        Objects.requireNonNull(fileUpload, "File upload cannot be null");
        
        var fileName = fileUpload.fileName();
        validateFile(fileName, fileUpload.size());
        
        try (InputStream inputStream = Files.newInputStream(fileUpload.uploadedFile())) {
            return tika.parseToString(inputStream);
        }
    }
    
    /**
     * Comprehensive file validation using modern patterns
     */
    private void validateFile(String fileName, long fileSize) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                "File size (%d bytes) exceeds maximum allowed size (%d bytes)"
                .formatted(fileSize, MAX_FILE_SIZE)
            );
        }
        
        if (!isAllowedFileType(fileName)) {
            throw new IllegalArgumentException(
                "File type not allowed. Supported extensions: %s"
                .formatted(String.join(", ", ALLOWED_EXTENSIONS))
            );
        }
    }
    
    /**
     * Check if file type is allowed using modern stream operations
     */
    public boolean isAllowedFileType(String fileName) {
        return fileName != null && 
               ALLOWED_EXTENSIONS.stream()
                   .anyMatch(ext -> fileName.toLowerCase().endsWith(ext));
    }
    
    /**
     * Get file extension from filename using modern string operations
     */
    public String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }
    
    /**
     * Get supported file extensions as immutable list
     */
    public List<String> getSupportedExtensions() {
        return ALLOWED_EXTENSIONS; // Already immutable
    }
    
    /**
     * Check if file size is within limits
     */
    public boolean isFileSizeValid(long fileSize) {
        return fileSize > 0 && fileSize <= MAX_FILE_SIZE;
    }
    
    /**
     * Get maximum allowed file size
     */
    public long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }
}