CREATE DATABASE IF NOT EXISTS `rangiffler-auth`;
USE `rangiffler-auth`;

CREATE TABLE IF NOT EXISTS users
(
    id                      BINARY(16)  unique not null default (UUID_TO_BIN(UUID(), TRUE)),
    username                varchar(50) unique not null,
    password                varchar(255)       not null,
    enabled                 boolean            not null,
    account_non_expired     boolean            not null,
    account_non_locked      boolean            not null,
    credentials_non_expired boolean            not null,
    primary key (id, username)
    );

create table if not exists authorities
(
    id        BINARY(16) unique not null default (UUID_TO_BIN(UUID(), TRUE)) primary key,
    user_id   BINARY(16)  not null,
    authority varchar(50) not null,
    foreign key (user_id) references users (id)
    );

-- CREATE UNIQUE INDEX  ix_auth_username ON authorities (user_id, authority);