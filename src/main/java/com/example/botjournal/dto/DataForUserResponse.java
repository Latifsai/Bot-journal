package com.example.botjournal.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Value
@Builder
public class DataForUserResponse {
    String firstname;
    String surname;
    String username;
    String registrationsDate;
}
