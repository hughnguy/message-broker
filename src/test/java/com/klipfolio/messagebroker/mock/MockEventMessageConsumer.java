package com.klipfolio.messagebroker.mock;

import com.klipfolio.messagebroker.MessageBroker;
import com.klipfolio.messagebroker.MockMessageBroker;
import com.klipfolio.messagebroker.core.message.eventmessage.EventMessageConsumer;
import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessMapper;

abstract class MockEventMessageConsumer extends EventMessageConsumer {

    MockEventMessageConsumer(EventProcessMapper... eventsToProcess) {
        super(eventsToProcess);
    }
    @Override
    public MessageBroker getMessageBroker() {
        return MockMessageBroker.getInstance();
    }
}
