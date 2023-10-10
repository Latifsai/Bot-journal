package com.example.botjournal.sevices;

import com.example.botjournal.config.BotConfig;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TelegramBotServiceTest {

    @Autowired
    private BotConfig botConfig;

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    private final long chatId = 5100940292L;

    @Test
    @DisplayName("testSendMessage test method")
    public void testSendMessage() {
        String message = "Hello, Bot!";
        String url = "https://api.telegram.org/bot" + botConfig.getToken() + "/sendMessage?chat_id=" + chatId + "&text=" + message;
        String response = restTemplate.getForObject(url, String.class);

        Assertions.assertTrue(response.contains("Hello, Bot!"));
    }

    @Test
    @DisplayName("test successes send-message")
    public void testSucceedMessage() {
        RestAssured.baseURI = "https://api.telegram.org/bot" + botConfig.getToken();
        given()
                .param("text", "rest-assured_Text")
                .param("chat_id", chatId)
                .when()
                .get("/sendMessage")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("test unsuccessful send-message")
    public void testNotSucceedMessage() {
        RestAssured.baseURI = "https://api.telegram.org/bot" + botConfig.getToken();
        given()
                .param("text", "rest-assured_Text")
                .param("chat_id", chatId)
                .param("parse_mode", "Markdown")
                .when()
                .get("/sendMessage")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("test unsuccessful send-message")
    public void testNotFoundMessage() {
        RestAssured.baseURI = "https://api.telegram.org/bot//" + botConfig.getToken();
        given()
                .param("text", "rest-assured_Text")
                .param("chat_id", chatId)
                .when()
                .get("/sendMessage")
                .then()
                .statusCode(404);
    }

}


