package com.example.botjournal.sevices;

import com.example.botjournal.dto.DataForUserResponse;
import com.example.botjournal.generators.DTOGenerator;
import com.example.botjournal.sevices.openAI.GPTAPIService;
import com.example.botjournal.sevices.openAI.WhisperService;
import com.example.botjournal.utiles.Messages;
import com.example.botjournal.utiles.VoiceConverter;
import com.example.botjournal.utiles.VoiceUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class MessageHandlerTest {

    @Mock
    GPTAPIService gptService;

    @Mock
    UserService userService;

    @Mock
    WhisperService whisperService;

    @Mock
    VoiceConverter converter;

    @Mock
    VoiceUtil voiceUtil;

    @InjectMocks
    MessageHandler messageHandler;

    String text;
    long chatID = 0L;
    Update update;
    Message message;
    DataForUserResponse userResponse = DTOGenerator.getUserResponse();

    @BeforeEach
    public void setUp() {
        message = new Message();
        Chat chat = new Chat();

        update = new Update();
        update.setMessage(message);

        message.setChat(chat);

        chat.setId(chatID);
        chat.setFirstName("Latif");
    }

    @Test
    @DisplayName("Check case message to GPT chat")
    void handeMessageOpenAI() {
        text = "Presentation about something";
        message.setText(text);

        when(gptService.getAnswerFromChatGPT(text)).thenReturn("Presentation");

        assertEquals("Presentation", messageHandler.handeMessage(update));
    }

    @Test
    @DisplayName("Check case message start command")
    void handeMessageStandardCommand() {
        text = "/start";
        message.setText(text);
        String answer = String.format(Messages.START_MESSAGE, "Latif");

        when(userService.recordUser(message)).thenReturn(userResponse);

        assertEquals(answer, messageHandler.handeMessage(update));
        verify(userService, times(1)).recordUser(message);
    }

    @Test
    @DisplayName("Check case message /help command")
    void handeMessageStandardCommandHelp() {
        text = "/help";
        message.setText(text);
        String answer = Messages.HELP_MESSAGE;

        assertEquals(answer, messageHandler.handeMessage(update));
    }

    @Test
    @DisplayName("Check case message help command")
    void handeMessageStandardCommandHelpButton() {
        text = "help";
        message.setText(text);
        String answer = Messages.HELP_MESSAGE;

        assertEquals(answer, messageHandler.handeMessage(update));
    }

    @Test
    @DisplayName("Check case message /mydata command")
    void handeMessageStandardCommandData() {
        text = "/mydata";
        message.setText(text);
        String answer = String.format(Messages.CHECK_DATA, userResponse.getUsername(), userResponse.getFirstname(),
                userResponse.getSurname(), userResponse.getRegistrationsDate());

        when(userService.getInfoAboutUser(chatID)).thenReturn(userResponse);

        assertEquals(answer, messageHandler.handeMessage(update));
        verify(userService, times(1)).getInfoAboutUser(chatID);
    }

    @Test
    @DisplayName("Check case message check data button")
    void handeMessageStandardCommandDataButton() {
        text = "check data";
        message.setText(text);
        String answer = String.format(Messages.CHECK_DATA, userResponse.getUsername(), userResponse.getFirstname(),
                userResponse.getSurname(), userResponse.getRegistrationsDate());

        when(userService.getInfoAboutUser(chatID)).thenReturn(userResponse);

        assertEquals(answer, messageHandler.handeMessage(update));
        verify(userService, times(1)).getInfoAboutUser(chatID);
    }

    @Test
    @DisplayName("Check case message check delete data button")
    void handeMessageStandardCommandDeleteData() {
        text = "delete data";
        message.setText(text);

        when(userService.deleteData(chatID)).thenReturn("Latif");

        String answer = String.format(Messages.DELETE_DATA_MESSAGE, "Latif");

        assertEquals(answer, messageHandler.handeMessage(update));
        verify(userService, times(1)).deleteData(chatID);
    }

    @Test
    @DisplayName("Check case message check unknown command")
    void handeMessageUnknownCommand() {
        text = "/smfd";
        message.setText(text);
        String answer = String.format(Messages.BAD_COMMAND);

        assertEquals(answer, messageHandler.handeMessage(update));
    }

    @Test
    @DisplayName("Check case message check voice message")
    void handeMessageVoice() throws IOException {
        message.setVoice(new Voice());
        String text = "presentation about my bot with something";

        when(voiceUtil.getVoiceFile(update.getMessage().getVoice().getFileId())).thenReturn(new File("path"));
        doNothing().when(converter).convertOggToMp3(anyString(), anyString());
        when(whisperService.transcribe(anyString())).thenReturn(text);
        when(gptService.getAnswerFromChatGPT(text)).thenReturn("Present");

        assertEquals("Present", messageHandler.handeMessage(update));

    }

}