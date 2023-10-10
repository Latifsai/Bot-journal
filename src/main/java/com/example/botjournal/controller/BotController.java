package com.example.botjournal.controller;

import com.example.botjournal.config.BotConfig;
import com.example.botjournal.entity.User;
import com.example.botjournal.sevices.ReplyKeyboardService;
import com.example.botjournal.sevices.UserService;
import com.example.botjournal.sevices.util.BotCommands;
import com.example.botjournal.sevices.util.Messages;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
public class BotController extends TelegramLongPollingBot implements BotCommands {

    private final BotConfig botConfig;

    private final ReplyKeyboardService replyKeyboardService;

    private final UserService userService;

    public BotController(BotConfig botConfig, ReplyKeyboardService replyKeyboardService, UserService userService) {
        this.botConfig = botConfig;
        this.replyKeyboardService = replyKeyboardService;
        this.userService = userService;

        try {
            this.execute(new SetMyCommands(BotCommands.COMMAND_LIST, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatID = 0;
        String messageText = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            messageText = update.getMessage().getText();
            chatID = update.getMessage().getChatId();

            if (messageText.contains("/send") && botConfig.getBotOwnerID() == chatID) {
                String messageToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
                List<User> users = userService.findAllUsers();
                for (User user : users) {
                    sendMessage(user.getChatID(), messageToSend);
                }

            } else {
                getBotAnswerLines(messageText, chatID, update);
            }

        } else if (update.hasCallbackQuery()) {
            chatID = update.getCallbackQuery().getMessage().getChatId();
            messageText = update.getCallbackQuery().getMessage().getText();
            getBotAnswerLines(messageText, chatID, update);
        }
    }

    private void getBotAnswerLines(String messageText, long chatID, Update update) {
        switch (messageText) {

            case "/start":
                startCommandReceived(chatID, update.getMessage().getChat().getFirstName());
                userService.recordUser(update.getMessage());
                try {
                    execute(replyKeyboardService.createKeyboard(chatID));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "/help", "help":
                sendMessage(chatID, Messages.HELP_MESSAGE);
                break;

            case "check data":
                sendMessage(chatID, checkData(chatID));
                break;

            case "delete data":
                sendMessage(chatID, delete(chatID)); // do fully
                break;
            default:
                sendMessage(chatID, "This command is not recognized!");
        }
    }

    private String delete(long chatID) {
        return "User with username: " + userService.deleteData(chatID) + " was deleted";
    }

    private String checkData(long id) {
        var response = userService.getInfoAboutUser(id);
        return "USERNAME: " + response.getUsername() +
                "\nNAME: " + response.getFirstname() +
                "\nSURNAME: " + response.getSurname() +
                "\nREGISTRATION DATE: " + response.getRegistrationsDate();
    }

    private void startCommandReceived(long chatID, String userName) {
        String answer = EmojiParser.parseToUnicode("Hello, " + userName + " and welcome to the Journal bot!" + "\uD83D\uDC4B");
        sendMessage(chatID, answer);
        log.info("Replied to user: " + userName);
    }

    private void sendMessage(long chatID, String textToSend) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatID)
                    .text(textToSend)
                    .build());
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage());
        }
    }

}
