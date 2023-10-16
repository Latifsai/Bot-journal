package com.example.botjournal.sevices.openAI;

import com.example.botjournal.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTAPIService {

    private final BotConfig botConfig;

    public String getAnswerFromChatGPT(String userText) {
        log.info("User`s message: " + userText);
        String url = botConfig.getBaseURl() + botConfig.getChatURl();

        StringBuffer response = new StringBuffer();

        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + botConfig.getApiKey());

            String model = "gpt-3.5-turbo";
            String prompt = getPrompt(userText);
            int maxTokens = 4000;

            connection.setDoOutput(true);
            String body = "{\"model\": \"" + model + "\", \"messages\":" + prompt + ", \"max_tokens\": " + maxTokens + "}";

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();

            log.info("Body of request: " + body);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (IOException e) {
            log.error("Error occurred in GPT service: " + e.getMessage());
        }
        return unpackAnswerFromResponse(response.toString());
    }

    private String unpackAnswerFromResponse(String response) {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }

    private String getPrompt(String userText) {
        String json = "[" +
                            "{" +
                                "\"role\": \"user\", " +
                                "\"content\": \"analyze the text and give me a clearly structured one ready for publication: " + userText.trim() + "\"" +
                            "}" +
                      "]";
        return json;
    }
}
