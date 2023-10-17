package com.example.botjournal.utiles;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Objects;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VoiceUtil {

    String URL;
    String botToken;
    RestTemplate restTemplate;

    public VoiceUtil(@Value("${api-url}") String url,
                     @Value("${bot.token}") String botToken) {
        URL = url;
        this.botToken = botToken;
        this.restTemplate = new RestTemplate();
    }

    public File getVoiceFile(String fileID) {

        String dir = "D:/AudioTeg/temp";
        File directory = new File(dir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File ret = new File(dir, "temp" + IDGeneratorFotVoiceMessages.getInstance().generate(5) + ".ogg");

        try {
            return restTemplate.execute(
                    Objects.requireNonNull(getVoiceTelegramFileURL(fileID)),
                    HttpMethod.GET,
                    null,
                    clientHttpResponse -> {
                        StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                        return ret;
                    });
        } catch (Exception e) {
            log.error("Error occurred in method getVoiceFile: " + e.getMessage());
            throw new RuntimeException(); // remake new special exception
        }

    }

    private String getVoiceTelegramFileURL(String fileID) {

        try {
            ResponseEntity<ApiResponse<org.telegram.telegrambots.meta.api.objects.File>> response = restTemplate.exchange(
                    MessageFormat.format("https://api.telegram.org/bot{0}/getFile?file_id={1}", botToken, fileID),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<org.telegram.telegrambots.meta.api.objects.File>>() {
                    }
            );

            return Objects.requireNonNull(response.getBody()).getResult().getFileUrl(this.botToken);

        } catch (Exception e) {
            log.error("Error occurred by method getVoiceTelegramFileURL: " + e.getMessage());
            throw new RuntimeException(); // remake for new Telegram exception
        }
    }

}
