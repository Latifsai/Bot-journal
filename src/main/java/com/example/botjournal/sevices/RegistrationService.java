package com.example.botjournal.sevices;

import com.example.botjournal.entity.enums.Buttons;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    public void register(long chatID, SendMessage sendMessage) {
        sendMessage.setChatId(String.valueOf(chatID));
        sendMessage.setText("Do you want to register?");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> listsOfLists = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Yes");
        yesButton.setCallbackData(Buttons.YES_BUTTON.toString());

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText("No");
        noButton.setCallbackData(Buttons.NO_BUTTON.toString());

        buttons.add(yesButton);
        buttons.add(noButton);

        listsOfLists.add(buttons);

        markup.setKeyboard(listsOfLists);

        sendMessage.setReplyMarkup(markup);
    }
}
