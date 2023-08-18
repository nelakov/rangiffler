package com.elakov.rangiffler.helpers;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import io.qameta.allure.util.ResultsUtils;
import lombok.extern.slf4j.Slf4j;

import static io.qameta.allure.Allure.addAttachment;

@Slf4j
public class AllureAttach extends StdoutLogger {

    private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();
    private final String templatePath = "sql-query.ftl";

    public static void addStepParameter(String name, Object value) {
        log.info("      " + name + ": " + value);
        Allure.getLifecycle().updateStep(r -> r
                .getParameters().add(ResultsUtils.createParameter(name, value)));
    }

    public static void attachText(String fileName, String content) {
        addAttachment(fileName, "text/plain", content, "txt");
    }

    public static void attachJson(String fileName, String content) {
        addAttachment(fileName, "application/json", content, "json");
    }

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        super.logSQL(connectionId, now, elapsed, category, prepared, sql, url);
        SqlAttachment sqlAttachment = new SqlAttachment("sql attachment", sql, prepared);
        attachmentProcessor.addAttachment(sqlAttachment, new FreemarkerAttachmentRenderer(templatePath));
    }



    // TODO: Добавить аттачи картинки и видео
}
