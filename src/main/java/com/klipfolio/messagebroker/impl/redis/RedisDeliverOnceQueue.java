package com.klipfolio.messagebroker.impl.redis;

import com.klipfolio.messagebroker.MessageBroker;
import com.klipfolio.messagebroker.core.MessageConsumer;

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
