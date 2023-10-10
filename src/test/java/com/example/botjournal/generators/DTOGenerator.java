package com.example.botjournal.generators;

import com.example.botjournal.dto.DataForUserResponse;

public class DTOGenerator {

    public static DataForUserResponse getUserResponse() {
        return DataForUserResponse.builder()
                .firstname("Mark")
                .surname("Berg")
                .username("Markus")
                .registrationsDate("09.10.2023 15:50:39")
                .build();
    }
}
