package com.crunchshop.messagebroker.impl.aws;

import com.crunchshop.messagebroker.core.MessageExchange;
import com.crunchshop.messagebroker.core.Topic;

class AWSExchange implements MessageExchange {

    private final AWSClient awsClient;
    private final AWSTopic topic;

    AWSExchange(AWSClient awsClient, AWSTopic topic) {
        this.awsClient = awsClient;
        this.topic = topic;
        topic.setTopicArn(awsClient.getSns().createTopic(topic.getName()).getTopicArn());
    }

    @Override
    public void routeMessage(String serializedText) {
        awsClient.getSns().publish(topic.getTopicArn(), serializedText);
    }

    @Override
    public Topic getTopic() {
        return topic;
    }
}
