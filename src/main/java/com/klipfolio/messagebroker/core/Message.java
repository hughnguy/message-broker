package com.klipfolio.messagebroker.core;

public interface Message<T> {
    /**
     * Gets the body of the message
     * @return the body of the message
     */
    T getBody();
}
