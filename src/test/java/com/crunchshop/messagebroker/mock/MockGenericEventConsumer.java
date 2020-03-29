package com.crunchshop.messagebroker.mock;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.MockMessageBroker;
import com.crunchshop.messagebroker.core.MessageConsumptionMode;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessMapper;

public class MockGenericEventConsumer extends MockEventMessageConsumer {

	public MockGenericEventConsumer(MockGenericEventProcessor mockGenericEventProcessor) {
	    super(new EventProcessMapper(MockGenericEvent.class, mockGenericEventProcessor));
	}

    @Override
    public MessageConsumptionMode getMessageConsumptionMode() {
        return MessageConsumptionMode.DELIVER_ONCE_PER_SERVICE;
    }

    @Override
    public MessageBroker getMessageBroker() {
        return MockMessageBroker.getInstance();
    }
}
