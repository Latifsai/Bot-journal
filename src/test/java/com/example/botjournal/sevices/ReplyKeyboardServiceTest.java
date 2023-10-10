package com.example.botjournal.sevices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ReplyKeyboardServiceTest {

    @InjectMocks
    private ReplyKeyboardService replyKeyboardService;

    @Test
    @DisplayName("createKeyboard test method")
    public void testCreateKeyboard() {
        long chatId = 1232;
        SendMessage result = replyKeyboardService.createKeyboard(chatId);

        assertSendMessageObject(result, chatId);
    }

    private void assertSendMessageObject(SendMessage sendMessage, long chatId) {
        assert sendMessage.getChatId().equals(String.valueOf(chatId));

        assert sendMessage.getText().equals("Just send me a message or audio recording");

        ReplyKeyboardMarkup markup = (ReplyKeyboardMarkup) sendMessage.getReplyMarkup();
        assert markup != null;

        List<KeyboardRow> rows = markup.getKeyboard();
        assert rows.size() == 1;

        List<KeyboardButton> buttons = rows.get(0);
        assert buttons.size() == 3;

        assert buttons.get(0).getText().equals("help");
        assert buttons.get(1).getText().equals("check data");
        assert buttons.get(2).getText().equals("delete data");
    }
}