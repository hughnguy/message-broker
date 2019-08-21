package com.klipfolio.messagebroker.core;

public interface MessageExchange {
    /**
     * Gets the topic from the exchange to publish a message to
     * @return the topic to publish to
     */
    Topic getTopic();

    /**
     * Publishes a message to the specified topic
     * @param serializedMessage the message to fan out and send to listening queues
     */
    void routeMessage(String serializedMessage);
}
