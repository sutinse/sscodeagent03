package com.example.service;

import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service for managing predefined system messages using modern Java features
 */
@ApplicationScoped
public class SystemMessageService {
    
    private static final String SEO_ANALYSIS_MESSAGE = """
        Analysoi annettu teksti ja tee siitä hakukoneoptimointi sekä Google hakukonerobotiikan 
        että tekoäly-robottien suhteen. 
        
        Anna lopuksi viisi ehdotusta kuinka voisi vielä parantaa:
        1. Sisällön optimointi
        2. Tekninen SEO
        3. Käyttäjäkokemus
        4. Semanttinen rakenne
        5. Suorituskyky
        """;
    
    private static final String CODE_REVIEW_MESSAGE = """
        Suorita annetulle Java-koodille code review. 
        
        Jos teksti ei ole Java-koodia, älä analysoi vaan kerro siitä.
        
        Analysoi seuraavat asiat:
        - Tunnista käytetty JDK-taso (esim JDK 8, 17, 21)
        - Jos kooditaso on alle 17, kerro että suosittelet uudemman JDK-tason käyttöä
        - Jos näet yleisiä parannusehdotuksia koodissa, lisää ne loppuun
        - Arvioi koodin turvallisuus ja suorituskyky
        - Tarkista nimeämiskäytännöt ja koodin luettavuus
        """;
    
    // Modern Map initialization with immutable map
    private final Map<String, String> systemMessages = Map.of(
        "SystemMessage1", SEO_ANALYSIS_MESSAGE,
        "SystemMessage2", CODE_REVIEW_MESSAGE
    );
    
    /**
     * Get system message by ID
     */
    public String getSystemMessage(String messageId) {
        return systemMessages.get(messageId);
    }
    
    /**
     * Check if system message ID exists
     */
    public boolean hasSystemMessage(String messageId) {
        return systemMessages.containsKey(messageId);
    }
    
    /**
     * Get all available system message IDs
     */
    public Map<String, String> getAllSystemMessages() {
        return Map.copyOf(systemMessages); // Return immutable copy
    }
}