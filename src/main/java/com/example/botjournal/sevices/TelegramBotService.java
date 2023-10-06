package com.example.botjournal.sevices;

import com.example.botjournal.config.BotConfig;
import com.example.botjournal.entity.User;
import com.example.botjournal.sevices.util.Messages;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final RegistrationService registrationService;
    private final ReplyKeyboardService replyKeyboardService;
    private final UserService userService;

    public TelegramBotService(BotConfig botConfig, RegistrationService registrationService, ReplyKeyboardService replyKeyboardService, UserService userService) {
        this.botConfig = botConfig;
        this.registrationService = registrationService;
        this.replyKeyboardService = replyKeyboardService;
        this.userService = userService;
        createCommandMenu();

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();

            if (messageText.contains("/send") && botConfig.getBotOwnerID() == chatID) {
                String messageToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
                List<User> users = userService.findAllUsers();
                for (User user : users) {
                    sendMessage(user.getId(), messageToSend);
                }

            } else {

                switch (messageText) {

                    case "/start":
                        startCommandReceived(chatID, update.getMessage().getChat().getFirstName());
                        userService.recordUser(update.getMessage());
                        getKeyBoard(chatID);
                        break;

                    case "/help":
                        sendMessage(chatID, Messages.HELP_MESSAGE);
                        break;

                    case "/register":
                        register(chatID);
                        break;

                    default:
                        sendMessage(chatID, "This command is not recognized!");
                }
            }

        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText editMessageText = new EditMessageText();
            String text = "";


            if (callBackData.equals("YES_BUTTON")) {
                text = "You pressed YES button";

            } else if (callBackData.equals("NO_BUTTON")) {
                text = "You pressed NO button";
            }

            editMessageText.setMessageId((int) messageId);
            editMessageText.setChatId(String.valueOf(chatId));
            editMessageText.setText(text);
            try {
                execute(editMessageText);
            } catch (TelegramApiException e) {
                log.error(e.getLocalizedMessage());
            }

        }
    }

    private void register(long chatID) {
        SendMessage sendMessage = new SendMessage();
        registrationService.register(chatID, sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private void startCommandReceived(long chatID, String userName) {
        String answer = EmojiParser.parseToUnicode("Hello, " + userName + " and welcome to the Journal bot!" + "\uD83D\uDC4B");
        sendMessage(chatID, answer);
        log.info("Replied to user: " + userName);
    }

    private void sendMessage(long chatID, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatID));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private void getKeyBoard(long chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        replyKeyboardService.createKeyboard(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage());
        }

    }
    private void createCommandMenu() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommands.add(new BotCommand("/help", "info how to use the bot"));
        listOfCommands.add(new BotCommand("/settings", "set your presences"));
        listOfCommands.add(new BotCommand("/send", "send a message to all users"));
        listOfCommands.add(new BotCommand("/register", "register user in bot"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot`s commands list: " + e.getMessage());
        }
    }
}
