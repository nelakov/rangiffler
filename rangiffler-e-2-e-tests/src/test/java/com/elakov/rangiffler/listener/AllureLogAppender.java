package com.elakov.rangiffler.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.status.ErrorStatus;
import com.google.common.html.HtmlEscapers;
import lombok.Getter;
import lombok.Setter;


public class AllureLogAppender<E> extends AppenderBase<E> {

    @Getter
    private static AllureLogAppender<?> instace;

    private final ThreadLocal<StringBuilder> logsBuffer = new ThreadLocal<>();

    @Setter
    @Getter
    private Encoder<E> encoder;

    public AllureLogAppender() {
        super();
        if (instace != null) {
            throw new RuntimeException("AllureLogAppender уже инициализирован");
        }
        instace = this;
    }

    @Override
    public void start() {
        if (this.encoder == null) {
            addStatus(new ErrorStatus("No encode set for the appender named \"" + name + "\".", this));
        } else {
            super.start();
        }
    }

    @Override
    protected void append(E eventObject) {
        ILoggingEvent event = (ILoggingEvent) eventObject;
        String text = new String(getEncoder().encode(eventObject));
        text = HtmlEscapers.htmlEscaper().escape(text);
        text = text.replaceAll("\r\n", "<br>");
        text = text.replaceAll("\n", "<br>");
        text = text.replaceAll(" ", "&nbsp;");
        text = text.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        int level = event.getLevel().levelInt;
        if (level == Level.WARN_INT) {
            text = "<div style=\"color:rgb(249,194,107)\">" + text + "</div>";
        } else if (level == Level.ERROR_INT) {
            text = "<div style=\"color:rgb(222,98,79)\">" + text + "</div>";
        } else if (level == Level.DEBUG_INT) {
            text = "<div style=\"color:rgb(120,120,120)\">" + text + "</div>";
        } else if (event.getMessage() != null && event.getMessage().startsWith(AllureStepListener.STEP_PREFIX)) {
            text = "<div style=\"color:rgb(70,140,170)\">" + text + "</div>";
        }
        if (logsBuffer.get() == null) {
            logsBuffer.set(new StringBuilder());
        }
        logsBuffer.get().append(text);
    }

    public void clearLog() {
        if (logsBuffer.get() != null) {
            logsBuffer.get().setLength(0);
        }
    }

    public String getLog() {
        if (logsBuffer.get() != null) {
            String message = logsBuffer.get().toString();
            return """
                    <!DOCTYPE html><html>
                    <head><meta charset="UTF-8"></head>
                    <body style="background-color:rgb(43, 43, 43); color:rgb(170,170,170); font-size:14px; font-family:monospace;">
                    """ +
                    "<script>" +
                    autoResizeFrameScript +
                    "</script>" +
                    message +
                    "</body></html>";

        } else {
            return "######## EMPTY LOGS ########";
        }
    }

    public static final String autoResizeFrameScript = """
            const resizeFrame = function() {
                const frame = Array.from(this.parent.frames)
                    .find(f=>
                        f.location.href == this.location.href
                        && !f.frameElement.classList.contains("attachment__iframe_fullscreen"));
                        
                if (frame) {
                    frame.frameElement.height = 100 + document.body.scrollHeight;
                }
            };
            window.onresize = resizeFrame;
            window.onload = resizeFrame;
            """;
}
