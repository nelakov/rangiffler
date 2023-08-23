package com.elakov.rangiffler.listener;

import com.elakov.rangiffler.helper.allure.AllureSqlAttachmentHelper;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

public class AllureSqlLogger extends StdoutLogger {

    private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        super.logSQL(connectionId, now, elapsed, category, prepared, sql, url);
        AllureSqlAttachmentHelper sqlAttachment = new AllureSqlAttachmentHelper("sql attachment", sql, prepared);
        String templatePath = "sql-query.ftl";
        attachmentProcessor.addAttachment(sqlAttachment, new FreemarkerAttachmentRenderer(templatePath));
    }
}
