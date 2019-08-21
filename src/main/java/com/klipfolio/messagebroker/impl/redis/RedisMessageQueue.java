package com.klipfolio.messagebroker.impl.redis;

import com.klipfolio.messagebroker.core.*;
import com.klipfolio.messagebroker.core.exception.MessageRequeueException;
import com.klipfolio.messagebroker.core.exception.MessageDiscardException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

abstract class RedisMessageQueue<T> implements MessageQueue<T> {

    private final Log log = LogFactory.getLog(RedisMessageQueue.class.getSimpleName());

    private final RedisClient redisClient;
    private final ScheduledExecutorService scheduler;
    private String queueName;

    RedisMessageQueue(RedisClient redisClient, ScheduledExecutorService scheduler) {
        this.redisClient = redisClient;
        this.scheduler = scheduler;
    }

    private String generateQueueListName(String queueName) {
        return "queue:" + queueName + ":messages";
    }

    @Override
    public void addListener(final MessageConsumer<T> consumer) {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                readQueue(queueName, consumer);
            }
        }, 0, consumer.getPollingIntervalInSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void registerConsumerToExchange(MessageConsumer<T> consumer, MessageExchange exchange) {
        queueName = generateQueueListName(generateQueueName(consumer));
        createQueueAndBindToExchange(queueName, exchange);
        addListener(consumer);
    }

    void removeConsumer(MessageConsumer<T> consumer, MessageExchange exchange) {
        queueName = generateQueueListName(generateQueueName(consumer));
        redisClient.unsubscribeQueueFromTopic(queueName, (RedisTopic) exchange.getTopic());
    }

    private void createQueueAndBindToExchange(String queueName, MessageExchange exchange) {
        redisClient.subscribeQueueToTopic(queueName, (RedisTopic) exchange.getTopic());
    }

    private void readQueue(String queueName, MessageConsumer<T> consumer) {
        List<String> messages = redisClient.batchReadMessagesFromQueue(queueName, consumer.getMaxReceiveMessageCount());
        for (String message : messages) {
            Message<T> newMessage = deserializeMessage(consumer, message);

            if(newMessage != null) {
                consumeMessage(consumer, newMessage);
            }
        }
    }

    private Message<T> deserializeMessage(MessageConsumer<T> consumer, String message) {
        try {
            MessageDeserializer<T> messageSerializer = consumer.getMessageDeserializer();
            return messageSerializer.deserializeQueueMessage(message);

        } catch(IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private void consumeMessage(MessageConsumer<T> consumer, Message<T> newMessage) {
        try {
            consumer.consumeMessage(newMessage);

        } catch(MessageDiscardException e) {
            log.error("Message corrupt. Redis queue has already discarded the message.");
        } catch(MessageRequeueException e) {
            log.error("Consumer failed to process the message. Redis queue does not support re-attempt of message.");
        }
    }
}
