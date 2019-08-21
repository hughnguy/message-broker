package com.klipfolio.messagebroker.mock;

import com.klipfolio.messagebroker.MessageBroker;
import com.klipfolio.messagebroker.MockMessageBroker;
import com.klipfolio.messagebroker.core.MessageConsumptionMode;
import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessMapper;
import com.klipfolio.messagebroker.event.AssetShared;

public class MockAssetSharedConsumer extends MockEventMessageConsumer {

	public MockAssetSharedConsumer(MockAssetSharedProcessor mockAssetSharedProcessor) {
	    super(new EventProcessMapper(AssetShared.class, mockAssetSharedProcessor));
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
