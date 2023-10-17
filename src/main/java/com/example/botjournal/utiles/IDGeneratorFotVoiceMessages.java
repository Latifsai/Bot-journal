package com.example.botjournal.utiles;

import java.security.SecureRandom;
import java.util.Random;

public class IDGeneratorFotVoiceMessages {

    private static IDGeneratorFotVoiceMessages instance;

    private IDGeneratorFotVoiceMessages() {
    }

    public static IDGeneratorFotVoiceMessages getInstance() {
        if (instance == null) {
            instance = new IDGeneratorFotVoiceMessages();
        }
        return instance;
    }
    private static String MATERIALS = "QWE1RTY2UIO3PAS4DFG5HJK6LZX7CVB8NM9!@#$%^&*)([]";

    public String generate(int index) {
        Random random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < index; i++) {
            builder.append(MATERIALS.charAt(random.nextInt(MATERIALS.length())));
        }
        return builder.toString();
    }
}
