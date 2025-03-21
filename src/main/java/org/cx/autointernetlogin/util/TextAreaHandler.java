package org.cx.autointernetlogin.util;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * 自定义日志显示 - 将日志输出到目标富文本控件中
 */
public class TextAreaHandler extends Handler {
    private final TextArea textArea;

    public TextAreaHandler(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        String message = getFormatter().format(record);
        Platform.runLater(() -> textArea.appendText(message));
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
