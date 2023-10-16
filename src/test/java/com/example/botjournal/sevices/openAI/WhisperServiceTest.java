package com.example.botjournal.sevices.openAI;

import com.example.botjournal.config.BotConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class WhisperServiceTest {

    @Autowired
    BotConfig botConfig;

    @Test
    @DisplayName("Test transcribe method")
    void transcribe() {
        String directory = "D:/IdeaProjects/bot-journal/output/";
        String fileName = "output!B8YX.mp3";
        String path = directory + File.separator + fileName;

        File file = new File(path);

        String prompt = "words";

        baseURI = "https://api.openai.com/v1/audio/transcriptions";

        given()
                .contentType(ContentType.MULTIPART)
                .header("Authorization", "Bearer " + botConfig.getApiKey())
                .multiPart("file", file)
                .param("model", botConfig.getAudioModel())
                .param("response_format", "text")
                .param("language", "en")
                .param("prompt", prompt)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .extract()
                .asString();
    }

}