package com.crunchshop.messagebroker.impl.aws;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.core.MessageConsumer;

class AWSBroadcastQueue<T> extends AWSMessageQueue<T> {

    AWSBroadcastQueue(AWSClient awsClient) {
        super(awsClient);
    }

    @Override
    public String generateQueueName(MessageConsumer<T> consumer) {
        return MessageBroker.getInstance().generateEnvironmentSpecificQueueName(consumer.getName()) +
                "-" +
                MessageBroker.getInstance().getUniqueInstanceName();
    }
}
