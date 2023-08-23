package com.elakov.rangiffler.test.web;

import com.elakov.rangiffler.jupiter.annotation.meta.Logger;
import com.elakov.rangiffler.jupiter.annotation.test.WebTest;
import com.elakov.rangiffler.page.auth.LoginPage;
import com.elakov.rangiffler.page.auth.RegistrationPage;
import com.elakov.rangiffler.page.auth.RegistrationSuccessPage;
import com.elakov.rangiffler.page.auth.StartPage;
import com.elakov.rangiffler.page.component.Header;
import com.elakov.rangiffler.page.tabs.NavigatorTabs;

@Logger
@WebTest
public abstract class BaseWebTest {

    protected StartPage startPage = new StartPage();
    protected LoginPage loginPage = new LoginPage();
    protected RegistrationPage registrationPage = new RegistrationPage();
    protected RegistrationSuccessPage registrationSuccessPage = new RegistrationSuccessPage();
    protected Header header = new Header();
    protected NavigatorTabs navigatorTabs = new NavigatorTabs();
}
