package com.klipfolio.messagebroker.core;

import com.klipfolio.messagebroker.core.exception.MessageDiscardException;
import com.klipfolio.messagebroker.core.exception.MessageRequeueException;

public interface MessageConsumer<T> {
    /**
     * Get the name of the consumer
     * @return the consumer name
     */
    String getName();

    /**
     * Return the deserializer used to convert the serialized message into a message object
     * that the consumer can read
     * @return the message deserializer
     */
    MessageDeserializer<T> getMessageDeserializer();

    /**
     * Consume and process the message
     * @param message the message from the queue
     * @throws MessageRequeueException
     * @throws MessageDiscardException
     */
    void consumeMessage(Message<T> message) throws MessageRequeueException, MessageDiscardException;

    /**
     * Returns the message consumption mode to determine which consumers will receive the message
     * @return the consumption mode
     */
    MessageConsumptionMode getMessageConsumptionMode();

    /**
     * Returns the polling interval for the consumer to read from the queue (if applicable)
     * @return polling interval in seconds
     */
    int getPollingIntervalInSeconds();

    /**
     * Returns the max amount of messages to receive per polling interval
     * @return number of messages to read from queue
     */
    int getMaxReceiveMessageCount();
}
