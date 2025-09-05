package com.example.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * Service for parsing files using Apache Tika
 */
@ApplicationScoped
public class FileParsingService {
    
    private final Tika tika;
    private final List<String> allowedExtensions = Arrays.asList(".java", ".txt", ".pdf", ".docx", ".pptx");
    
    public FileParsingService() {
        this.tika = new Tika();
    }
    
    /**
     * Parse file content from FileUpload
     */
    public String parseFile(FileUpload fileUpload) throws IOException, TikaException {
        if (fileUpload == null) {
            throw new IllegalArgumentException("File upload cannot be null");
        }
        
        String fileName = fileUpload.fileName();
        if (!isAllowedFileType(fileName)) {
            throw new IllegalArgumentException("File type not allowed. Supported types: " + allowedExtensions);
        }
        
        try (InputStream inputStream = Files.newInputStream(fileUpload.uploadedFile())) {
            return tika.parseToString(inputStream);
        }
    }
    
    /**
     * Check if file type is allowed
     */
    public boolean isAllowedFileType(String fileName) {
        if (fileName == null) {
            return false;
        }
        
        String lowerCaseFileName = fileName.toLowerCase();
        return allowedExtensions.stream()
            .anyMatch(lowerCaseFileName::endsWith);
    }
    
    /**
     * Get file extension from filename
     */
    public String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    /**
     * Get supported file extensions
     */
    public List<String> getSupportedExtensions() {
        return allowedExtensions;
    }
}