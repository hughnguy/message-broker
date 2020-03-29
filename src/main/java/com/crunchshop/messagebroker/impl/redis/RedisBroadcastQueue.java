package com.crunchshop.messagebroker.impl.redis;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.core.MessageConsumer;

import java.util.concurrent.ScheduledExecutorService;

class RedisBroadcastQueue<T> extends RedisMessageQueue<T> {

    RedisBroadcastQueue(RedisClient redisClient, ScheduledExecutorService scheduler) {
        super(redisClient, scheduler);
    }

    @Override
    public String generateQueueName(MessageConsumer<T> consumer) {
        return MessageBroker.getInstance().generateEnvironmentSpecificQueueName(consumer.getName()) +
                "-" +
                MessageBroker.getInstance().getUniqueInstanceName();
    }
}
