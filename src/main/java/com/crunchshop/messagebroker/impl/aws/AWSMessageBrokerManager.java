package com.crunchshop.messagebroker.impl.aws;

import com.crunchshop.messagebroker.core.MessageBrokerManager;
import com.crunchshop.messagebroker.core.MessageConsumer;
import com.crunchshop.messagebroker.core.MessageConsumptionMode;
import com.crunchshop.messagebroker.core.Topic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AWSMessageBrokerManager<T> implements MessageBrokerManager<T> {

    private final Map<Topic, AWSExchange> exchanges;
    private final Map<MessageConsumer<T>, AWSMessageQueue<T>> queues;
    private final AWSClient awsClient;

    public AWSMessageBrokerManager(String awsAccessKey, String awsSecretKey, String region) {
        awsClient = new AWSClient(awsAccessKey, awsSecretKey, region);
        exchanges = new ConcurrentHashMap<>();
        queues = new ConcurrentHashMap<>();
    }

    @Override
    public void registerMessageConsumerToTopic(MessageConsumer<T> consumer, Topic topic) {
        AWSExchange exchange = getExchange(topic);
        AWSMessageQueue<T> queue = getQueue(consumer);
        queue.registerConsumerToExchange(consumer, exchange);
    }

    @Override
    public void removeMessageConsumerFromTopic(MessageConsumer<T> consumer, Topic topic) {
        AWSExchange exchange = getExchange(topic);
        AWSMessageQueue<T> queue = getQueue(consumer);
        queue.removeConsumer(consumer, exchange);
    }

    @Override
    public Topic createTopic(String topicName) {
        return new AWSTopic(topicName);
    }

    @Override
    public AWSExchange getExchange(Topic topic) {
        AWSExchange exchange = exchanges.get(topic);
        if (exchange == null) {
            exchange = new AWSExchange(awsClient, (AWSTopic) topic);
            exchanges.put(topic, exchange);
        }
        return exchange;
    }

    @Override
    public AWSMessageQueue<T> getQueue(MessageConsumer<T> consumer) {
        MessageConsumptionMode mode = consumer.getMessageConsumptionMode();
        AWSMessageQueue queue = queues.get(consumer);
        if (queue == null) {
            switch (mode) {
                case BROADCAST_TO_ALL_CONSUMERS:
                    queue = new AWSBroadcastQueue(awsClient);
                    break;
                case DELIVER_ONCE_PER_SERVICE:
                    queue = new AWSDeliverOnceQueue(awsClient);
                    break;
                default:
                    throw new RuntimeException("Unknown consumption mode: " + mode);
            }
            queues.put(consumer, queue);
        }
        return queue;
    }
}
