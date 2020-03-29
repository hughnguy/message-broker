package com.crunchshop.messagebroker.impl.redis;

import com.crunchshop.messagebroker.core.MessageBrokerManager;
import com.crunchshop.messagebroker.core.MessageConsumer;
import com.crunchshop.messagebroker.core.MessageConsumptionMode;
import com.crunchshop.messagebroker.core.Topic;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class RedisMessageBrokerManager<T> implements MessageBrokerManager<T> {

    private static final int DEFAULT_SCHEDULER_POOL_SIZE = 5;

    private final Map<Topic, RedisMessageExchange> exchanges;
    private final Map<MessageConsumer<T>, RedisMessageQueue<T>> queues;
    private final ScheduledExecutorService scheduler;
    private final RedisClient redisClient;

    public RedisMessageBrokerManager(JedisPool jedisPool) {
        redisClient = new RedisClient(jedisPool);
        exchanges = new ConcurrentHashMap<>();
        queues = new ConcurrentHashMap<>();
        scheduler = Executors.newScheduledThreadPool(DEFAULT_SCHEDULER_POOL_SIZE);
    }

    @Override
    public void registerMessageConsumerToTopic(MessageConsumer<T> consumer, Topic topic) {
        RedisMessageExchange exchange = getExchange(topic);
        RedisMessageQueue<T> queue = getQueue(consumer);
        queue.registerConsumerToExchange(consumer, exchange);
    }

    @Override
    public void removeMessageConsumerFromTopic(MessageConsumer<T> consumer, Topic topic) {
        RedisMessageExchange exchange = getExchange(topic);
        RedisMessageQueue<T> queue = getQueue(consumer);
        queue.removeConsumer(consumer, exchange);
    }

    @Override
    public Topic createTopic(String topicName) {
        return new RedisTopic(topicName);
    }

    @Override
    public RedisMessageExchange getExchange(Topic topic) {
        RedisMessageExchange exchange = exchanges.get(topic);
        if (exchange == null) {
            exchange = new RedisMessageExchange(redisClient, (RedisTopic) topic);
            exchanges.put(topic, exchange);
        }
        return exchange;
    }

    @Override
    public RedisMessageQueue<T> getQueue(MessageConsumer<T> consumer) {
        MessageConsumptionMode mode = consumer.getMessageConsumptionMode();
        RedisMessageQueue queue = queues.get(consumer);
        if (queue == null) {
            switch (mode) {
                case BROADCAST_TO_ALL_CONSUMERS:
                    queue = new RedisBroadcastQueue(redisClient, scheduler);
                    break;
                case DELIVER_ONCE_PER_SERVICE:
                    queue = new RedisDeliverOnceQueue(redisClient, scheduler);
                    break;
                default:
                    throw new RuntimeException("Unknown consumption mode: " + mode);
            }
            queues.put(consumer, queue);
        }
        return queue;
    }
}
