package com.crunchshop.messagebroker.core.exception;

/**
 * Exception is throw when a consumer fails to process a message.
 * The message should be re-queued in this case
 */
public class MessageRequeueException extends Exception {
    public MessageRequeueException(Throwable t) {
        super(t);
    }
    public MessageRequeueException(String message) {
        super(message);
    }
    public MessageRequeueException(String message, Throwable t) {
        super(message, t);
    }
}
