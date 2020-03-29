package com.crunchshop.messagebroker.core;

import java.io.IOException;

public interface MessageSerializer<T> {
    /**
     * Serialize the queue message into a string
     * @return the serialized message
     * @throws IOException
     */
    String serializeQueueMessage(T message) throws IOException;
}
