package com.example.botjournal.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.ws.rs.DefaultValue;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "chat_id", unique = true)
    private Long chatID;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname")
    @ColumnDefault("'was not specified'")
    private String lastname;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "registration_date", nullable = false)
    private Instant registrationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(chatID, user.chatID) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatID, username);
    }
}

