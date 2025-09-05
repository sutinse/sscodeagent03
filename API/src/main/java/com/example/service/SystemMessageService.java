package com.example.service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service for managing predefined system messages using modern Java 21 features
 */
@ApplicationScoped
public class SystemMessageService {
    
    // Using text blocks for improved readability of multi-line system messages
    private static final String SEO_ANALYSIS_MESSAGE = """
        Analysoi annettu teksti ja tee siitä hakukoneoptimointi sekä Google hakukonerobotiikan 
        että tekoäly-robottien suhteen. 
        
        Anna lopuksi viisi ehdotusta kuinka voisi vielä parantaa:
        1. Sisällön optimointi
        2. Tekninen SEO
        3. Käyttäjäkokemus
        4. Semanttinen rakenne
        5. Suorituskyky
        
        Vastaa suomeksi ja anna konkreettiset, toimivat ehdotukset.
        """;
    
    private static final String CODE_REVIEW_MESSAGE = """
        Suorita annetulle Java-koodille perusteellinen code review. 
        
        Jos teksti ei ole Java-koodia, älä analysoi vaan kerro siitä selkeästi.
        
        Analysoi seuraavat asiat järjestelmällisesti:
        - Tunnista käytetty JDK-taso (esim JDK 8, 11, 17, 21)
        - Jos kooditaso on alle 17, kerro että suosittelet uudemman JDK-tason käyttöä
        - Arvioi koodin turvallisuus ja mahdolliset haavoittuvuudet
        - Tarkista suorituskyky ja optimointimahdollisuudet
        - Analysoi nimeämiskäytännöt ja koodin luettavuus
        - Tarkista error handling ja exception management
        - Ehdota konkreettisia parannuksia Java 21 best practices mukaisesti
        
        Anna vastaus suomeksi ja ole konstruktiivinen kritiikissäsi.
        """;
    
    private static final String CONTENT_ANALYSIS_MESSAGE = """
        Analysoi annettu sisältö monipuolisesti seuraavista näkökulmista:
        
        1. Sisällön rakenne ja organisointi
        2. Kielellinen laatu ja selkeys
        3. Kohderyhmän sopivuus
        4. Informaation arvo ja relevanssi
        5. Mahdolliset parannusehdotukset
        
        Anna rakentavaa palautetta ja konkreettisia ehdotuksia sisällön parantamiseksi.
        Vastaa suomeksi.
        """;
    
    // Modern immutable Map initialization with multiple entries
    private static final Map<String, String> SYSTEM_MESSAGES = Map.of(
        "SystemMessage1", SEO_ANALYSIS_MESSAGE,
        "SystemMessage2", CODE_REVIEW_MESSAGE,
        "SystemMessage3", CONTENT_ANALYSIS_MESSAGE
    );
    
    // Immutable Map for message descriptions
    private static final Map<String, String> MESSAGE_DESCRIPTIONS = Map.of(
        "SystemMessage1", "SEO ja hakukoneoptimointi analyysi",
        "SystemMessage2", "Java-koodin code review ja analyysi",
        "SystemMessage3", "Yleinen sisällön analyysi ja arviointi"
    );
    
    /**
     * Get system message by ID with modern Optional handling
     */
    public Optional<String> getSystemMessageOptional(String messageId) {
        Objects.requireNonNull(messageId, "Message ID cannot be null");
        return Optional.ofNullable(SYSTEM_MESSAGES.get(messageId.trim()));
    }
    
    /**
     * Get system message by ID (legacy method for backward compatibility)
     */
    public String getSystemMessage(String messageId) {
        return getSystemMessageOptional(messageId).orElse(null);
    }
    
    /**
     * Check if system message ID exists using modern patterns
     */
    public boolean hasSystemMessage(String messageId) {
        return messageId != null && 
               !messageId.trim().isEmpty() && 
               SYSTEM_MESSAGES.containsKey(messageId.trim());
    }
    
    /**
     * Get all available system messages as immutable map
     */
    public Map<String, String> getAllSystemMessages() {
        return SYSTEM_MESSAGES; // Already immutable
    }
    
    /**
     * Get system message with description
     */
    public Optional<SystemMessageInfo> getSystemMessageInfo(String messageId) {
        Objects.requireNonNull(messageId, "Message ID cannot be null");
        
        return getSystemMessageOptional(messageId)
            .map(message -> new SystemMessageInfo(
                messageId.trim(),
                message,
                MESSAGE_DESCRIPTIONS.get(messageId.trim())
            ));
    }
    
    /**
     * Get all system message IDs
     */
    public java.util.Set<String> getAvailableMessageIds() {
        return SYSTEM_MESSAGES.keySet(); // Already immutable
    }
    
    /**
     * Get message descriptions
     */
    public Map<String, String> getMessageDescriptions() {
        return MESSAGE_DESCRIPTIONS; // Already immutable
    }
    
    /**
     * Validate system message ID format
     */
    public boolean isValidMessageId(String messageId) {
        return messageId != null && 
               messageId.matches("SystemMessage[1-9][0-9]*") &&
               hasSystemMessage(messageId);
    }
    
    /**
     * Record representing system message information
     */
    public record SystemMessageInfo(
        String id,
        String content,
        String description
    ) {
        public SystemMessageInfo {
            Objects.requireNonNull(id, "ID cannot be null");
            Objects.requireNonNull(content, "Content cannot be null");
            // Description can be null
        }
        
        public boolean hasDescription() {
            return description != null && !description.trim().isEmpty();
        }
        
        public int getContentLength() {
            return content.length();
        }
    }
}