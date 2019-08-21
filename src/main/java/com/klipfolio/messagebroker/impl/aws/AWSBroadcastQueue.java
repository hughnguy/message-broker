package com.klipfolio.messagebroker.impl.aws;

import com.klipfolio.messagebroker.MessageBroker;
import com.klipfolio.messagebroker.core.MessageConsumer;

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
