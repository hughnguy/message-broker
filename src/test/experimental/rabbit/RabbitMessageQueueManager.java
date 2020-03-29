package com.crunchshop.experimental.rabbit;

import com.crunchshop.messagebroker.core.MessageConsumer;
import com.crunchshop.messagebroker.core.MessageDeliveryMode;
import com.crunchshop.messagebroker.core.MessageQueueManager;
import com.crunchshop.messagebroker.core.Topic;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RabbitMessageQueueManager<T> implements MessageQueueManager<T> {

    private final RabbitClient rabbitClient;
    private final Map<Topic, RabbitMessageExchange> exchanges;
    private final Map<MessageConsumer<T>, RabbitMessageQueue<T>> queues;

    public RabbitMessageQueueManager(ConnectionFactory factory) {
        rabbitClient = new RabbitClient(factory);
        exchanges = new ConcurrentHashMap<>();
        queues = new ConcurrentHashMap<>();
    }

    @Override
    public void registerMessageConsumerToTopic(MessageConsumer<T> consumer, Topic topic) throws Exception {
        RabbitMessageExchange exchange = getOrCreateExchange(topic);
        RabbitMessageQueue<T> queue = getOrCreateQueue(consumer);
        queue.registerConsumerToExchange(consumer, exchange);
    }

    @Override
    public void removeMessageConsumerFromTopic(MessageConsumer<T> consumer, Topic topic) throws Exception {
        RabbitMessageExchange exchange = getOrCreateExchange(topic);
        RabbitMessageQueue<T> queue = getOrCreateQueue(consumer);
        queue.removeConsumer(consumer, exchange);
    }

    @Override
    public Topic createTopic(String topicName) {
        return new RabbitTopic(topicName);
    }

    @Override
    public RabbitMessageExchange getOrCreateExchange(Topic topic) throws Exception {
        RabbitMessageExchange exchange = exchanges.get(topic);
        if (exchange == null) {
            exchange = new RabbitMessageExchange(rabbitClient, (RabbitTopic)topic);
            exchanges.put(topic, exchange);
        }
        return exchange;
    }

    @Override
    public RabbitMessageQueue<T> getOrCreateQueue(MessageConsumer<T> consumer) throws Exception {
        MessageDeliveryMode type = consumer.getMessageDeliveryMode();
        RabbitMessageQueue queue = queues.get(consumer);
        if (queue == null) {
            switch (type) {
                case BROADCAST_TO_ALL_CONSUMERS:
                    queue = new RabbitBroadcastQueue(rabbitClient);
                    break;
                case DELIVER_ONCE_PER_SERVICE:
                    queue = new RabbitDeliverOnceQueue(rabbitClient);
                    break;
                default:
                    throw new Exception("Unknown queue type: " + type);
            }
            queues.put(consumer, queue);
        }
        return queue;
    }
}
