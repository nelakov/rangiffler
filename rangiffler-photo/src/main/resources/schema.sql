CREATE DATABASE IF NOT EXISTS `rangiffler-photo`;
USE `rangiffler-photo`;

CREATE TABLE IF NOT EXISTS photos
(
    id           BINARY(16) PRIMARY KEY,
    username     VARCHAR(50) NOT NULL,
    country_code VARCHAR(50) NOT NULL,
    description  VARCHAR(255),
    photo        LONGBLOB    NOT NULL
);