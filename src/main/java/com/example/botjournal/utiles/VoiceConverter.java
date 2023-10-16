package com.example.botjournal.utiles;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class VoiceConverter {

    private final FFmpeg fFmpeg;

    public VoiceConverter(@Value("${ffmpeg.path}") String ffmpegPath) throws IOException {
        this.fFmpeg = new FFmpeg(new File(ffmpegPath).getPath());
    }

    public void convertOggToMp3(String inputPath, String targetPath) throws IOException {
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .overrideOutputFiles(true)
                .addOutput(targetPath)
                .setAudioCodec("libmp3lame")
                .setAudioBitRate(32768)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(this.fFmpeg);
        executor.createJob(builder).run();

        try {
            executor.createTwoPassJob(builder).run();
        }catch (IllegalArgumentException ignore) {
            // ignore cause
        }
    }

}
