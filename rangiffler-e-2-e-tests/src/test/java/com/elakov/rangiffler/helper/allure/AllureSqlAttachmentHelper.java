package com.elakov.rangiffler.helper.allure;

import io.qameta.allure.attachment.AttachmentData;

public record AllureSqlAttachmentHelper(String name, String sql, String statement) implements AttachmentData {

    public AllureSqlAttachmentHelper(String name, String sql, String statement) {
        this.name = name;
        this.sql = sql;
        this.statement = statement;
    }

    @Override
    public String getName() {
        return name;
    }

}
