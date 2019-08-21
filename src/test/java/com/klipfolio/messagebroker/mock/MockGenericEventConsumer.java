package com.klipfolio.messagebroker.mock;

import com.klipfolio.messagebroker.MessageBroker;
import com.klipfolio.messagebroker.MockMessageBroker;
import com.klipfolio.messagebroker.core.MessageConsumptionMode;
import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessMapper;
import com.klipfolio.messagebroker.event.AssetShared;
import com.klipfolio.messagebroker.event.GenericEvent;

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
