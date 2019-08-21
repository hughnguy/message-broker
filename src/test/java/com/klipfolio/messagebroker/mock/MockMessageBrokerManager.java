package com.klipfolio.messagebroker.mock;

import com.klipfolio.messagebroker.core.*;
import com.klipfolio.messagebroker.core.message.eventmessage.EventMessageBody;

public class MockMessageBrokerManager implements MessageBrokerManager<EventMessageBody> {

    private MessageExchange messageExchange;

    public MockMessageBrokerManager(MessageExchange messageExchange) {
        this.messageExchange = messageExchange;
    }

    @Override
    public Topic createTopic(String topicName) {
        return null;
    }

    @Override
    public void registerMessageConsumerToTopic(MessageConsumer consumer, Topic topic) {}

    @Override
    public void removeMessageConsumerFromTopic(MessageConsumer consumer, Topic topic) {}

    @Override
    public MessageQueue<EventMessageBody> getQueue(MessageConsumer consumer) {
        return null;
    }

    @Override
    public MessageExchange getExchange(Topic topic) {
        return messageExchange;
    }
}
