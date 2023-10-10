package com.example.botjournal.sevices.util;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> COMMAND_LIST = List.of(
        new BotCommand("/start", "get a welcome message"),
        new BotCommand("/mydata", "get your data stored"),
        new BotCommand("/deletedata", "delete my data"),
        new BotCommand("/help", "info how to use the bot"),
        new BotCommand("/send", "send a message to all users")
    );


}
