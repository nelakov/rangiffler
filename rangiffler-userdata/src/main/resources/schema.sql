CREATE DATABASE IF NOT EXISTS `rangiffler-userdata`;
USE `rangiffler-userdata`;

CREATE TABLE IF NOT EXISTS users
(
    id        BINARY(16) UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), TRUE)),
    username  VARCHAR(50) UNIQUE            NOT NULL,
    firstname VARCHAR(255),
    surname   VARCHAR(255),
    avatar    LONGBLOB,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS friends
(
    user_id   BINARY(16) NOT NULL,
    friend_id BINARY(16) NOT NULL,
    pending   BOOLEAN    NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id)
    );