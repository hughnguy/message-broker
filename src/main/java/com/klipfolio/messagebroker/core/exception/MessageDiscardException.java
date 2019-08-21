package com.klipfolio.messagebroker.core.exception;

public class MessageDiscardException extends Exception {
    public MessageDiscardException(Throwable t) {
        super(t);
    }
    public MessageDiscardException(String message) {
        super(message);
    }
    public MessageDiscardException(String message, Throwable t) {
        super(message, t);
    }
}
