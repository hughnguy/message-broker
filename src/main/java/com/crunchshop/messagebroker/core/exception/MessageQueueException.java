package com.crunchshop.messagebroker.core.exception;

public class MessageQueueException extends Exception {
    public MessageQueueException(String message) {
        super(message);
    }
    public MessageQueueException(String message, Exception exception) {
        super(message, exception);
    }
}
