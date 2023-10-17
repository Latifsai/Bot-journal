package com.example.botjournal.sevices.openAI;

import com.example.botjournal.config.BotConfig;
import com.example.botjournal.utiles.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class WhisperService {

    private final BotConfig config;

    private final String URL = "https://api.openai.com/v1/audio/transcriptions";
    public static final int MAX_ALLOWED_SIZE = 25 * 1024 * 1024; //25 mg or >
    public static final int MAX_CHUNK_SIZE_BYTES = 20 * 1024 * 1024;
    private final String WORD_LIST = String.join(",",
            List.of("GPT", "chat", "API", "Java", "logical syntax sugar", "Spring Boot",
                    "Big Decimal"));

    private String transcribeChunk(String prompt, File chunkFile) {
        log.info(String.format("Transcription started file: %s%n", chunkFile.getName()));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(URL);
            httpPost.setHeader("Authorization", "Bearer " + config.getApiKey());

            File file = new File("D:/IdeaProjects/bot-journal/output/", chunkFile.getName());

            HttpEntity entity = MultipartEntityBuilder.create()
                    .setContentType(ContentType.MULTIPART_FORM_DATA)
                    .addPart("file", new FileBody(file, ContentType.DEFAULT_BINARY))
                    .addPart("model", new StringBody(config.getAudioModel(), ContentType.DEFAULT_TEXT))
                    .addPart("response_format", new StringBody("text", ContentType.DEFAULT_TEXT))
                    .addPart("language", new StringBody("en", ContentType.DEFAULT_TEXT))
                    .addPart("prompt", new StringBody(prompt, ContentType.DEFAULT_TEXT))
                    .build();
            httpPost.setEntity(entity);

            return client.execute(httpPost, response -> {
                log.info("Status: " + new StatusLine(response));
                return EntityUtils.toString(response.getEntity());
            });

        } catch (IOException e) {
            log.error("Error occurred in method 'transcribeChunk':" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String transcribe(String fileName) {
        File file = new File(fileName);
        List<String> transcriptions = new ArrayList<>();

        String prompt = WORD_LIST;

        if (file.length() <= MAX_ALLOWED_SIZE) {
            String transcription = transcribeChunk(prompt, file);
            transcriptions = List.of(transcription);
        }

        return getTranscription(fileName, transcriptions);
    }

    private String getTranscription(String fileName, List<String> list) {
        String trans = String.join(" ", list);
        String fileNameWithoutPath = fileName.substring(fileName.lastIndexOf("/") + 1);
        FileUtils.writeTextToFile(trans, fileNameWithoutPath.replace(".mp3", ".txt"));
        return trans;
    }
}
