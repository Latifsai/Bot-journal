package com.example.botjournal.sevices;

import com.example.botjournal.dto.DataForUserResponse;
import com.example.botjournal.entity.User;
import com.example.botjournal.generators.DTOGenerator;
import com.example.botjournal.generators.EntityGenerator;
import com.example.botjournal.repository.UserRepository;
import com.example.botjournal.utiles.UserUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserUtil util;

    @InjectMocks
    private UserService userService;

    private static final long chatID = 0L;

    private static final User user = EntityGenerator.getUser();

    private static final DataForUserResponse response = DTOGenerator.getUserResponse();

    @Test
    @DisplayName("recordUser test method if chatID not exists")
    void recordUser() {
        Message message = mock(Message.class);

        when(repository.findById(chatID)).thenReturn(Optional.empty());
        when(util.createUser(message)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(util.convertToResponse(user)).thenReturn(response);

        assertEquals(response, userService.recordUser(message));

        verify(repository,times(1)).findById(chatID);
        verify(util, times(1)).createUser(message);
        verify(repository, times(1)).save(user);
        verify(util, times(1)).convertToResponse(user);
    }


    @Test
    @DisplayName("recordUser test method if chatID already exists")
    void recordUserFoundedUser() {
        Message message = mock(Message.class);
        when(repository.findById(chatID)).thenReturn(Optional.of(user));

        assertNull(userService.recordUser(message));
        verify(repository,times(1)).findById(chatID);
    }
    @Test
    @DisplayName("getInfoAboutUser test method if chatID exists")
    void getInfoAboutUser() {
        when(repository.findById(chatID)).thenReturn(Optional.of(user));
        when(util.convertToResponse(user)).thenReturn(response);

        assertEquals(response, userService.getInfoAboutUser(chatID));

        verify(repository, times(1)).findById(chatID);
        verify(util, times(1)).convertToResponse(user);
    }


    @Test
    @DisplayName("getInfoAboutUser test method if chatID not exists")
    void getInfoAboutUserNotExists() {
        when(repository.findById(chatID)).thenReturn(Optional.empty());

        assertNull(userService.getInfoAboutUser(chatID));

        verify(repository, times(1)).findById(chatID);
    }

    @Test
    @DisplayName("deleteData test method if chatID exists")
    void deleteData() {
        String username = user.getUsername();

        when(repository.findById(chatID)).thenReturn(Optional.of(user));
        doNothing().when(repository).delete(user);

        assertEquals(username,userService.deleteData(chatID));
    }

    @Test
    @DisplayName("deleteData test method if chatID not exists")
    void deleteDataIfNotExists() {
        when(repository.findById(chatID)).thenReturn(Optional.empty());
        assertEquals("",userService.deleteData(chatID));
    }
    @Test
    @DisplayName("findAllUsers test method")
    void findAllUsers() {
        when(repository.findAll()).thenReturn(List.of(user));
        assertEquals(List.of(user), userService.findAllUsers());
    }
}