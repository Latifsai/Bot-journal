create table if not exists users(
    chat_id BIGSERIAL PRIMARY KEY UNIQUE,
    firstname VARCHAR(60) DEFAULT 'was not specified',
    lastname VARCHAR(60) DEFAULT 'was not specified',
    username VARCHAR(100) NOT NULL UNIQUE,
    registration_date timestamp(6) NOT NULL
);