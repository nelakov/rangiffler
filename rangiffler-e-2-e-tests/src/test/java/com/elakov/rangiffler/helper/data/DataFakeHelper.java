package com.elakov.rangiffler.helper.data;

import net.datafaker.Faker;

import java.util.UUID;

public class DataFakeHelper {


    private static final Faker FAKER = new Faker();

    public static String generateRandomUsername() {
        return FAKER.name().username();
    }

    public static String generateName51Symbols() {
        return FAKER.lorem().sentence(51);
    }

    public static String generateRandomFunnyUsername() {
        // Append a unique suffix: the funnyName pool is small, so parallel tests
        // collide on the unique users.username column under load.
        return FAKER.funnyName().name() + "-" + UUID.randomUUID().toString().substring(0, 8);
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
