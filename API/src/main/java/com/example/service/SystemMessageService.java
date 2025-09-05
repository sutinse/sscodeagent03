package com.example.service;

import java.util.HashMap;
import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service for managing predefined system messages
 */
@ApplicationScoped
public class SystemMessageService {
    
    private final Map<String, String> systemMessages;
    
    public SystemMessageService() {
        systemMessages = new HashMap<>();
        initializeSystemMessages();
    }
    
    private void initializeSystemMessages() {
        systemMessages.put("SystemMessage1", 
            "Analysoi annettu teksti ja tee siitä hakukoneoptimointi sekä Google hakukonerobotiikan että tekoäly-robottien suhteen. Anna lopuksi viisi ehdotusta kuinka voisi vielä parantaa");
        
        systemMessages.put("SystemMessage2", 
            "Suorita annetulle java -koodille code review. Jos teksti ei ole java-koodia, älä analysoi vaan kerro siitä. Tunnista käytetty jdk-taso (esim jdk 8, 17, 21). Jos kooditaso on alle 17, kerro että suosittelet uudemman jdk-tason käyttöä. Jos näet yleisiä parannusehdotuksia koodissa, lisää ne loppuun");
    }
    
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
        return new HashMap<>(systemMessages);
    }
}