package com.elakov.rangiffler.jupiter.callback;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.elakov.rangiffler.config.web.BrowserProperties;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class BrowserConfigExtension implements TestSuiteCallback, AfterEachCallback, TestExecutionExceptionHandler {

    @Override
    public void beforeSuite() {
        Configuration.browser = BrowserProperties.BROWSER_NAME;
        Configuration.browserVersion = BrowserProperties.BROWSER_VERSION;
        Configuration.browserSize = BrowserProperties.BROWSER_SIZE;
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Selenide.closeWebDriver();
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
            throws Throwable {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Allure.addAttachment("Screen on fail",
                    new ByteArrayInputStream(((TakesScreenshot) WebDriverRunner.getWebDriver())
                            .getScreenshotAs(OutputType.BYTES))
            );
        }

        throw throwable;
    }

}
