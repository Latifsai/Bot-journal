package com.example.botjournal.sevices.util;

import com.example.botjournal.dto.DataForUserResponse;
import com.example.botjournal.entity.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class UserUtil {

    public User createUser(Message message) {
        Long chatID = message.getChatId();
        Chat chat = message.getChat();
        User user = new User();

        user.setChatID(chatID);

        user.setFirstname(constraint(chat.getFirstName()) ? Messages.NOT_EXISTS_MESSAGE : chat.getFirstName());
        user.setUsername(constraint(chat.getUserName()) ? Messages.NOT_EXISTS_MESSAGE : chat.getUserName());
        user.setLastname(constraint(chat.getLastName()) ? Messages.NOT_EXISTS_MESSAGE : chat.getLastName());

        user.setRegistrationDate(Instant.now());
        return user;
    }

    private Boolean constraint(String param) {
        return param == null || param.isBlank();
    }

    public DataForUserResponse convertToResponse(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = (LocalDateTime.ofInstant(user.getRegistrationDate(), ZoneId.of("UTC")).format(formatter));
        return DataForUserResponse.builder()
                .firstname(user.getFirstname())
                .surname(user.getLastname())
                .registrationsDate(date)
                .username(user.getUsername())
                .build();
    }

}

