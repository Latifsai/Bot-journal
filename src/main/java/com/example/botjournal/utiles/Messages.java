package com.example.botjournal.utiles;

public class Messages {

    public static final String HELP_MESSAGE = """
            This is created to demonstrate messages ability

            You can execute commands from the main menu or left or by typing a command:

            Type /start to see a welcome message

            Type /help to see this message help menu

            Type /mydata to see your data

            Type /deletedata to delete user data

            Type /send to send message to all users, this opportunity is allowed only to owner

            """;

    public static final String PARAMETER_NOT_EXISTS_MESSAGE = "ABSENT";

    public static final String START_MESSAGE = """
            Hello, %s  and welcome to the Journal bot! \uD83D\uDC4B
            To use it you need just to send a message or audio recording.
            """;

    public static final String CHECK_DATA = """
            USERNAME: " %s
            NAME: " %s
            SURNAME: " %s
            REGISTRATION DATE: " %s
            """;

    public static final String BAD_COMMAND = "No such options.";
    public static final String DELETE_DATA_MESSAGE = "User with username: %s was deleted.";




}
