package com.crunchshop.messagebroker.impl.redis;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.core.MessageConsumer;

import java.util.concurrent.ScheduledExecutorService;

class RedisDeliverOnceQueue<T> extends RedisMessageQueue<T> {

    RedisDeliverOnceQueue(RedisClient redisClient, ScheduledExecutorService scheduler) {
        super(redisClient, scheduler);
    }

    @Override
    public String generateQueueName(MessageConsumer<T> consumer) {
        return MessageBroker.getInstance().generateEnvironmentSpecificQueueName(consumer.getName());
    }
}
