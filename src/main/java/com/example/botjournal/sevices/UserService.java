package com.example.botjournal.sevices;

import com.example.botjournal.entity.User;
import com.example.botjournal.repository.UserRepository;
import com.example.botjournal.sevices.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserUtil util;
    public void recordUser(Message message) {
        if (repository.findById(message.getChatId()).isEmpty()) {
            User user = util.createUser(message);
            repository.save(user);
        }
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }
}
