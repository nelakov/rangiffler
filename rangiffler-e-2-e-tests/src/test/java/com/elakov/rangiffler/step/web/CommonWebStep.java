package com.elakov.rangiffler.step.web;

import com.elakov.rangiffler.page.auth.LoginPage;
import com.elakov.rangiffler.page.auth.RegistrationPage;
import com.elakov.rangiffler.page.auth.RegistrationSuccessPage;
import com.elakov.rangiffler.page.auth.StartPage;
import com.elakov.rangiffler.page.component.Header;
import com.elakov.rangiffler.page.tabs.NavigatorTabs;
import com.elakov.rangiffler.page.tabs.TravelsTab;
import com.github.javafaker.Faker;

import static com.codeborne.selenide.Selenide.open;
import static com.elakov.rangiffler.config.services.ServicesProperties.CLIENT_BASE_URL;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public abstract class CommonWebStep<T> {

    protected StartPage startPage = new StartPage();
    protected LoginPage loginPage = new LoginPage();
    protected RegistrationPage registrationPage = new RegistrationPage();
    protected RegistrationSuccessPage registrationSuccessPage = new RegistrationSuccessPage();
    protected Header header = new Header();
    protected NavigatorTabs navigatorTabs = new NavigatorTabs();
    protected TravelsTab travelsTab = new TravelsTab();
    protected Faker faker = new Faker();

    public T openStartPageAndRedirectToLogin() {
        step("Open 'Start page'",
                () -> open(CLIENT_BASE_URL));
        startPage
                .openLoginPage(loginPage)
                .checkThatPageLoaded();
        return (T) this;
    }

    public <T> T openTravelsPage(T page) {
        open(CLIENT_BASE_URL, TravelsTab.class);
        return page;
    }

}
