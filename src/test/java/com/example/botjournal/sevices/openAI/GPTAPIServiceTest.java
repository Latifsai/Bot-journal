package com.example.botjournal.sevices.openAI;

import com.example.botjournal.config.BotConfig;
import io.restassured.http.ContentType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GPTAPIServiceTest {

    @Autowired
    BotConfig botConfig;

    @Test
    void getAnswerFromChatGPT() throws Exception {
        String text = "text";
        baseURI = "https://api.openai.com/v1/chat/completions";

        String model = "gpt-3.5-turbo";
        String prompt = "[" +
                "{" +
                "\"role\": \"user\", " +
                "\"content\": \"analyze the text and give me a clearly structured one ready for publication: " + text.trim() + "\"" +
                "}" +
                "]";

        int maxTokens = 4000;

        String body = "{\"model\": \"" + model + "\", \"messages\":" + prompt + ", \"max_tokens\": " + maxTokens + "}";

        System.out.println("Body:" + body);
        System.out.println(botConfig.getApiKey());

        String response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + botConfig.getApiKey())
                .body(body)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .asString();

        assertFalse(response.isEmpty());
    }

}