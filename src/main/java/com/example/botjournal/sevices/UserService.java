package com.example.botjournal.sevices;

import com.example.botjournal.dto.DataForUserResponse;
import com.example.botjournal.entity.User;
import com.example.botjournal.repository.UserRepository;
import com.example.botjournal.utiles.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserUtil util;

    public DataForUserResponse recordUser(Message message) {
        if (repository.findById(message.getChatId()).isEmpty()) {
            User user = util.createUser(message);
            repository.save(user);
            return util.convertToResponse(user);
        }
        return null; //instead null will be added Exception
    }

    public DataForUserResponse getInfoAboutUser(long chatID) {
        Optional<User> userOptional = repository.findById(chatID);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return util.convertToResponse(user);
        }
        return null; //instead null will be added Exception
    }

    public String deleteData(long chatID) {
        String username = "";
        Optional<User> userOptional = repository.findById(chatID);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            username = user.getUsername();
            repository.delete(user);
        }
        return username;
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

}
