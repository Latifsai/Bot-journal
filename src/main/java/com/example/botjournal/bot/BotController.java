package com.example.botjournal.bot;

import com.example.botjournal.config.BotConfig;
import com.example.botjournal.sevices.MessageHandler;
import com.example.botjournal.sevices.ReplyKeyboardService;
import com.example.botjournal.utiles.BotCommandsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class BotController extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final ReplyKeyboardService replyKeyboardService;

    private final MessageHandler messageHandler;

    public BotController(BotConfig botConfig, ReplyKeyboardService replyKeyboardService, MessageHandler messageHandler) {
        this.botConfig = botConfig;
        this.replyKeyboardService = replyKeyboardService;
        this.messageHandler = messageHandler;

        try {
            this.execute(new SetMyCommands(BotCommandsUtils.COMMAND_LIST, new BotCommandScopeDefault(), null));
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
        long chatID = update.getMessage().getChatId();
        sendMessage(chatID, messageHandler.handeMessage(update), replyKeyboardService.createKeyboard());
    }


    private void sendMessage(long chatID, String textToSend, ReplyKeyboardMarkup markup) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatID)
                    .text(textToSend)
                    .replyMarkup(markup)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error occurred by message sending: " + e.getLocalizedMessage());
        }
    }

}
