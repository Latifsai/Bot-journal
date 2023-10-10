package com.example.botjournal.generators;

import com.example.botjournal.entity.User;

import java.time.Instant;

public class EntityGenerator {

    public static User getUser() {
        return new User(
                0L,
                "Mark",
                "ABSENT.",
                "Markus",
                Instant.now()
        );
    }

}
