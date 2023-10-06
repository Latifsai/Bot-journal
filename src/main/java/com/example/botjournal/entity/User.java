package com.example.botjournal.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.glassfish.grizzly.http.util.TimeStamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "users")
@Getter
@Setter
@ToString

public class User {
    @Id
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}

