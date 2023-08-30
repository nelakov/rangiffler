package com.elakov.rangiffler.test;


//TODO: Realise Pattern Memento instead TestContext
public final class TestContext {

    private static final ThreadLocal<String> username = new ThreadLocal<>();
    private static final ThreadLocal<String> password = new ThreadLocal<>();
    private static final ThreadLocal<String> firstname = new ThreadLocal<>();
    private static final ThreadLocal<String> surname = new ThreadLocal<>();

    private TestContext() {
    }

    public static String getUsername() {
        return username.get();
    }

    public static void setUsername(String testUsername) {
        username.set(testUsername);
    }

    public static String getPassword() {
        return password.get();
    }

    public static void setPassword(String pass) {
        password.set(pass);
    }

    public static String getFirstName() {
        return firstname.get();
    }

    public static void setFirstName(String name) {
        firstname.set(name);
    }

    public static String getSurName() {
        return surname.get();
    }

    public static void setSurName(String lastname) {
        firstname.set(lastname);
    }

}
