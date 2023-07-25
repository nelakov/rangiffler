CREATE DATABASE IF NOT EXISTS `rangiffler-gateway`;
USE `rangiffler-gateway`;

CREATE TABLE IF NOT EXISTS categories
(
    id          CHAR(36) not null primary key,
    description varchar(255) not null
    );

CREATE TABLE IF NOT EXISTS spends
(
    id          BINARY(16) PRIMARY KEY UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), TRUE)),
    username    VARCHAR(50) UNIQUE            NOT NULL,
    spend_date  date         not null,
    currency    varchar(50)  not null,
    amount      float        not null,
    description varchar(255) not null,
    category_id CHAR(36)     not null,
    foreign key (category_id) references categories (id)
    );

-- delete from categories;
-- insert into categories (id, description) values (uuid(), 'Рестораны');
-- insert into categories (id, description) values (uuid(), 'Продуктовые магазины');
-- insert into categories (id, description) values (uuid(), 'Обучение в QA.GURU ADVANCED');

-- insert into spends (id, username, spend_date, currency, amount, description, category_id)
-- values (uuid(), 'dima', '2023-02-15', 'RUB', 100.0, 'Радостная покупка', (select id from categories where description = 'Обучение в QA.GURU ADVANCED'));
-- insert into spends (id, username, spend_date, currency, amount, description, category_id)
-- values (uuid(), 'dima', '2023-02-15', 'RUB', 500.0, 'Радостная покупка', (select id from categories where description = 'Рестораны'));
-- insert into spends (id, username, spend_date, currency, amount, description, category_id)
-- values (uuid(), 'dima', '2023-02-15', 'RUB', 10000.0, 'Радостная покупка', (select id from categories where description = 'Продуктовые магазины'));