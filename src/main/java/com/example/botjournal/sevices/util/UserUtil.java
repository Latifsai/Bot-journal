package com.example.botjournal.sevices.util;

import com.example.botjournal.entity.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;

@Service
public class UserUtil {
    public User createUser(Message message) {
        Long chatID = message.getChatId();
        Chat chat = message.getChat();
        User user = new User();

        user.setId(chatID);
        user.setUsername(chat.getUserName());
        user.setFirstname(chat.getFirstName());
        user.setLastname(chat.getLastName());
        user.setRegistrationDate(LocalDateTime.now());
        return user;
    }
}
