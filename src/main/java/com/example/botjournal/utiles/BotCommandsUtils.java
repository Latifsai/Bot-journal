package com.example.botjournal.utiles;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public class BotCommandsUtils {
    public static List<BotCommand> COMMAND_LIST = List.of(
            new BotCommand("/start", "get a welcome message"),
            new BotCommand("/mydata", "get your data stored"),
            new BotCommand("/deletedata", "delete my data"),
            new BotCommand("/help", "info how to use the bot")
    );


}
