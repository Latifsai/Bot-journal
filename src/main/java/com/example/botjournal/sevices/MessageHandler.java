package com.example.botjournal.sevices;

import com.example.botjournal.dto.DataForUserResponse;
import com.example.botjournal.sevices.openAI.GPTAPIService;
import com.example.botjournal.sevices.openAI.WhisperService;
import com.example.botjournal.utiles.IDGeneratorFotVoiceMessages;
import com.example.botjournal.utiles.Messages;
import com.example.botjournal.utiles.VoiceConverter;
import com.example.botjournal.utiles.VoiceUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageHandler {

    GPTAPIService gptService;

    UserService userService;

    WhisperService whisperService;

    VoiceConverter converter;

    VoiceUtil voiceUtil;

    public String handeMessage(Update update) {
        String answer = "";
        Message message = update.getMessage();
        long chatID;
        String messageText;

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatID = message.getChatId();
            messageText = message.getText();

            if (messageText.charAt(0) != '/'
                    && !messageText.equals("help")
                    && !messageText.equals("check data")
                    && !messageText.equals("delete data")) {

                answer = gptService.getAnswerFromChatGPT(messageText);
            } else {
                answer = getBotAnswerLines(messageText, chatID, message);
            }

        } else if (update.hasCallbackQuery()) {
            chatID = update.getCallbackQuery().getMessage().getChatId();
            messageText = update.getCallbackQuery().getMessage().getText();
            answer = getBotAnswerLines(messageText, chatID, message);
        } else if (update.getMessage().hasVoice()) {
            messageText = sendConvertedVoiceMessage(message);
            answer = gptService.getAnswerFromChatGPT(messageText);
        }
        return answer;
    }

    private String getBotAnswerLines(String messageText, long chatID, Message message) {
        String answer;

        switch (messageText) {
            case "/start" -> {
                answer = startCommandReceived(message.getChat().getFirstName());
                userService.recordUser(message);
            }
            case "/help", "help" -> answer = Messages.HELP_MESSAGE;
            case "check data", "/mydata" -> answer = checkData(chatID);
            case "delete data" -> answer = delete(chatID);
            default -> answer = Messages.BAD_COMMAND;
        }
        return answer;
    }


    private String delete(long chatID) {
        return String.format(Messages.DELETE_DATA_MESSAGE, userService.deleteData(chatID));
    }

    private String checkData(long chatID) {
        DataForUserResponse response = userService.getInfoAboutUser(chatID);
        return String.format(Messages.CHECK_DATA, response.getUsername(), response.getFirstname(),
                response.getSurname(), response.getRegistrationsDate());
    }

    private String startCommandReceived(String userName) {
        String answer = EmojiParser.parseToUnicode(Messages.START_MESSAGE);
        String str = String.format(answer, userName);
        log.info("Replied to user: " + userName);
        return str;
    }

    private File convertVoice(Message message) {
        Voice voice = message.getVoice();

        try {
            File source = voiceUtil.getVoiceFile(voice.getFileId());
            File target = new File("D:/IdeaProjects/bot-journal/output", "output" + IDGeneratorFotVoiceMessages.getInstance().generate(5) + ".mp3");

            converter.convertOggToMp3(source.getPath(), target.getPath());

            return target;
        } catch (Exception e) {
            log.error("Error occurred in method convertVoice: " + e.getMessage());
            throw new RuntimeException();// remake new special Exception
        }
    }

    private String sendConvertedVoiceMessage(Message message) {
        File file = convertVoice(message);
        return whisperService.transcribe(file.getName());
    }



}

