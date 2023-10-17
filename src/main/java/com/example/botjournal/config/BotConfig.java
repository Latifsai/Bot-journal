package com.example.botjournal.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotConfig {
    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

    @Value("${bot.owner}")
    Long botOwnerID;

    @Value("${api-key}")
    String apiKey;

    @Value("${gpt-model}")
    String gptModel;

    @Value("${audio-model}")
    String audioModel;

    @Value("${base-url}")
    String baseURl;

    @Value("${chat-url}")
    String chatURl;

    @Value("${transcription-url}")
    String transcriptionURl;

}
