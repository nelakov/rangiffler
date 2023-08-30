package com.elakov.rangiffler.helper.data;

import com.github.javafaker.Faker;

public class DataFakeHelper {

    // Нельзя гибко настроить метод

    // Создать фабрику для генерации данных

    // Статичная фабрика на Enum (см Блоха)
    private static final Faker FAKER = new Faker();

    public static String generateRandomUsername() {
        return FAKER.name().username();
    }

    public static String generateRandomFunnyUsername() {
        return FAKER.funnyName().name();
    }

    public static String generateRandomPassword() {
        return FAKER.bothify("????####");
    }

    public static String generateRandomName() {
        return FAKER.name().firstName();
    }

    public static String generateRandomSurname() {
        return FAKER.name().lastName();
    }

    public static String generateRandomSentence(int wordsCount) {
        return FAKER.lorem().sentence(wordsCount);
    }

    public static String generateRandomDescription() {
        return FAKER.hobbit().location() + "\n"
                + FAKER.weather().description() + "\n"
                + FAKER.hobbit().quote();
    }

    public static String randomLastname() {
        return FAKER.name().lastName();
    }

    public static String randomLorem() {
        return FAKER.lorem().sentence();
    }
}
