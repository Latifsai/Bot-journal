package com.example.botjournal.utiles;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class FileUtils {
    public static void writeTextToFile(String textData, String fileName) {
        Path directory = Paths.get("scr/main/resources/text");
        Path filePath = directory.resolve(fileName);

        try {
            Files.createDirectories(directory);
            Files.deleteIfExists(filePath);
            Files.writeString(filePath, textData , StandardOpenOption.CREATE_NEW);
            log.info(String.format("Saved %s to scr/main/resources/text%n", fileName));
        } catch (IOException e) {
            log.error("Error writing text to file", e);
        }
    }
}
