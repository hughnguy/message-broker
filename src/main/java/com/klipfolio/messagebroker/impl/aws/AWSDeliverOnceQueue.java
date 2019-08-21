package com.klipfolio.messagebroker.impl.aws;

import com.klipfolio.messagebroker.MessageBroker;
import com.klipfolio.messagebroker.core.MessageConsumer;

class AWSDeliverOnceQueue<T> extends AWSMessageQueue<T> {

    AWSDeliverOnceQueue(AWSClient awsClient) {
        super(awsClient);
    }

    @Override
    public String generateQueueName(MessageConsumer<T> consumer) {
        return MessageBroker.getInstance().generateEnvironmentSpecificQueueName(consumer.getName());
    }
}
