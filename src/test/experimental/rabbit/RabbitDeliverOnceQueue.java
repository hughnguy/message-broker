package com.crunchshop.experimental.rabbit;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.core.MessageConsumer;

public class RabbitDeliverOnceQueue<T> extends RabbitMessageQueue<T> {

    public RabbitDeliverOnceQueue(RabbitClient rabbitClient) throws Exception {
        super(rabbitClient);
    }

    @Override
    public String generateQueueName(MessageConsumer<T> consumer) {
        return MessageBroker.getInstance().generateEnvironmentSpecificQueueName(consumer.getName());
    }
}
