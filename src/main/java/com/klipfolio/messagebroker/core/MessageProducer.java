package com.klipfolio.messagebroker.core;


public interface MessageProducer<T> {
    /**
     * Get the message exchange needed in order to publish to the topic
     * @param topic the topic to publish a message to
     * @return the exchange associated to the topic
     * @throws Exception
     */
    MessageExchange getExchange(Topic topic) throws Exception;

    /**
     * Return the serializer used to convert the message into a serialized form
     * that the consumer can read
     * @return the message deserializer
     */
    MessageSerializer<T> getMessageSerializer();

    /**
     * Publish a message to the topic
     * @param topic the topic to publish a message to
     * @param message the message to publish to the topic
     * @throws Exception
     */
    void publish(Topic topic, Message<T> message) throws Exception;
}
