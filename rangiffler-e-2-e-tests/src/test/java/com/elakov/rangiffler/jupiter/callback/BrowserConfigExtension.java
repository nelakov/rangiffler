package com.elakov.rangiffler.jupiter.callback;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.elakov.rangiffler.config.web.BrowserProperties;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

public class BrowserConfigExtension implements TestSuiteCallback, AfterEachCallback, TestExecutionExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserConfigExtension.class);
    private static final int VIDEO_RETRIES = 6;
    private static final long VIDEO_RETRY_DELAY_MS = 2000;

    @Override
    public void beforeSuite() {
        Configuration.browser = BrowserProperties.BROWSER_NAME;
        Configuration.browserVersion = BrowserProperties.BROWSER_VERSION;
        Configuration.browserSize = BrowserProperties.BROWSER_SIZE;

        if (BrowserProperties.isRemote()) {
            // Selenoid/Grid run: route the driver to the hub and ask Selenoid to
            // record video + expose VNC. Empty browser.remote keeps the local path.
            Configuration.remote = BrowserProperties.BROWSER_REMOTE;
            Configuration.browserCapabilities = new MutableCapabilities(Map.of(
                    "selenoid:options", Map.of(
                            "enableVNC", true,
                            "enableVideo", true
                    )
            ));
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        boolean failed = context.getExecutionException().isPresent();
        String sessionId = (BrowserProperties.isRemote() && WebDriverRunner.hasWebDriverStarted())
                ? ((RemoteWebDriver) WebDriverRunner.getWebDriver()).getSessionId().toString()
                : null;

        // The video is finalized only after the session ends, so close first, then fetch.
        Selenide.closeWebDriver();

        if (failed && sessionId != null && !BrowserProperties.VIDEO_STORAGE.isBlank()) {
            attachVideo(sessionId);
        }
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Allure.addAttachment("Screen on fail",
                    new ByteArrayInputStream(((TakesScreenshot) WebDriverRunner.getWebDriver())
                            .getScreenshotAs(OutputType.BYTES))
            );
        }
        throw throwable;
    }

    private void attachVideo(String sessionId) {
        String url = BrowserProperties.VIDEO_STORAGE + sessionId + ".mp4";
        for (int attempt = 0; attempt < VIDEO_RETRIES; attempt++) {
            try (InputStream video = URI.create(url).toURL().openStream()) {
                Allure.addAttachment("Video", "video/mp4", video, "mp4");
                return;
            } catch (FileNotFoundException notReadyYet) {
                Selenide.sleep(VIDEO_RETRY_DELAY_MS); // Selenoid still flushing the file
            } catch (IOException e) {
                LOG.warn("Could not attach Selenoid video from {}", url, e);
                return;
            }
        }
        LOG.warn("Selenoid video not available after {} retries: {}", VIDEO_RETRIES, url);
    }
}
