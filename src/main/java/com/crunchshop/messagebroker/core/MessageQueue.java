package com.crunchshop.messagebroker.core;

import com.crunchshop.messagebroker.core.exception.MessageQueueException;

public interface MessageQueue<T> {
    /**
     * Add the consumer to start polling and listening to the message queue
     * @param consumer the consumer that will be listening to the queue
     * @throws MessageQueueException when adding listener fails
     */
    void addListener(MessageConsumer<T> consumer) throws MessageQueueException;

    /**
     * Generates the name of the queue specific to environment and consumer.
     * It's used to create the actual queue
     * @param consumer the consumer which will be listening/polling the queue
     * @return the queue name
     */
    String generateQueueName(MessageConsumer<T> consumer);

    /**
     * Registers message consumer to the specified exchange
     * @param consumer the consumer which will be listening/polling the queue
     * @param exchange the exchange which will fan out the message to the consumer that is listening to the queue
     * @throws MessageQueueException when registration fails
     */
    void registerConsumerToExchange(MessageConsumer<T> consumer, MessageExchange exchange) throws MessageQueueException;
}
