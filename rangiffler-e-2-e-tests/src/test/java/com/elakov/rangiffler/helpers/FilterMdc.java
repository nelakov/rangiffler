package com.elakov.rangiffler.helpers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.MDC;

import java.util.Objects;

public class FilterMdc extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (Objects.isNull(MDC.get("Enable"))) {
            return FilterReply.DENY;
        }
        return FilterReply.ACCEPT;
    }
}