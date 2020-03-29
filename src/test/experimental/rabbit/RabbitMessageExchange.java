package com.crunchshop.experimental.rabbit;

import com.crunchshop.messagebroker.core.MessageExchange;
import com.crunchshop.messagebroker.core.Topic;
import com.rabbitmq.client.Channel;

import java.io.IOException;

public class RabbitMessageExchange implements MessageExchange {

    private static final String EXCHANGE_TYPE_FANOUT = "fanout";

    private final RabbitClient rabbitClient;
    private final Channel channel;
    private final RabbitTopic topic;

    RabbitMessageExchange(RabbitClient rabbitClient, RabbitTopic topic) throws Exception {
        this.rabbitClient = rabbitClient;
        this.topic = topic;
        this.channel = rabbitClient.createChannel();
        this.channel.exchangeDeclare(topic.getName(), EXCHANGE_TYPE_FANOUT);
    }

    @Override
    public void routeMessage(String serializedText) throws IOException {
        channel.basicPublish(getTopic().getName(), "", null, serializedText.getBytes("UTF-8"));
    }

    @Override
    public Topic getTopic() {
        return topic;
    }
}
