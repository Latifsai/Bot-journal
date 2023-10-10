package com.example.botjournal.sevices;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Service
public class ReplyKeyboardService {

    private static final KeyboardButton HELP_BUTTON = new KeyboardButton("help");
    private static final KeyboardButton CHECK_DATA_BUTTON = new KeyboardButton("check data");
    private static final KeyboardButton DELETE_DATA_BUTTON = new KeyboardButton("delete data");

    public SendMessage createKeyboard(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Just send me a message or audio recording");

        List<KeyboardButton> rowsLine = List.of(HELP_BUTTON, CHECK_DATA_BUTTON, DELETE_DATA_BUTTON);
        List<KeyboardRow> rows = List.of(new KeyboardRow(rowsLine));

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(rows);

        sendMessage.setReplyMarkup(markup);

        return sendMessage;
    }
}
