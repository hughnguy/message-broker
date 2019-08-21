package com.klipfolio.messagebroker.core;

import java.io.IOException;

public interface MessageDeserializer<T> {
    /**
     * Deserialize the text into the message object
     * @param serializedText the serialized text
     * @throws IOException
     */
    Message<T> deserializeQueueMessage(String serializedText) throws IOException;
}
