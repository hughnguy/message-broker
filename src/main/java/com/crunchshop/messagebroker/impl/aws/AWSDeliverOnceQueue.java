package com.crunchshop.messagebroker.impl.aws;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.core.MessageConsumer;

class AWSDeliverOnceQueue<T> extends AWSMessageQueue<T> {

    AWSDeliverOnceQueue(AWSClient awsClient) {
        super(awsClient);
    }

    @Override
    public String generateQueueName(MessageConsumer<T> consumer) {
        return MessageBroker.getInstance().generateEnvironmentSpecificQueueName(consumer.getName());
    }
}
