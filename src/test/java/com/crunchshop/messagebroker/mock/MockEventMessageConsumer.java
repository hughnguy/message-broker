package com.crunchshop.messagebroker.mock;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.MockMessageBroker;
import com.crunchshop.messagebroker.core.message.eventmessage.EventMessageConsumer;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessMapper;

abstract class MockEventMessageConsumer extends EventMessageConsumer {

    MockEventMessageConsumer(EventProcessMapper... eventsToProcess) {
        super(eventsToProcess);
    }
    @Override
    public MessageBroker getMessageBroker() {
        return MockMessageBroker.getInstance();
    }
}
