package com.crunchshop.messagebroker.core;

public interface MessageBrokerManager<T> {
    /**
     * Create a topic to publish messages to and fan out to consumers
     * @param topicName the name of the topic to create
     * @return the topic
     */
    Topic createTopic(String topicName);

    /**
     * Register a consumer to the topic in order to receive messages
     * @param consumer the consumer which will be registered to the topic
     * @param topic the topic which will fan out messages to the consumer
     * @throws Exception
     */
    void registerMessageConsumerToTopic(MessageConsumer<T> consumer, Topic topic);

    /**
     * Remove consumer from the topic to stop receiving messages
     * @param consumer the consumer which will be removed from the topic
     * @param topic the topic which will stop fanning out messages to the consumer
     * @throws Exception
     */
    void removeMessageConsumerFromTopic(MessageConsumer<T> consumer, Topic topic);

    /**
     * Get or create a queue for the consumer if one does not exist yet
     * @param consumer the consumer which will be listening to the queue
     * @return the message queue created for the consumer
     * @throws Exception
     */
    MessageQueue<T> getQueue(MessageConsumer<T> consumer);

    /**
     * Get or create an exchange that is associated to the topic
     * @param topic the topic that will be associated to the exchange
     * @return the exchange mapped to the topic
     * @throws Exception
     */
    MessageExchange getExchange(Topic topic);
}
