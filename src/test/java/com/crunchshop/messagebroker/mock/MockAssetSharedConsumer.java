package com.crunchshop.messagebroker.mock;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.MockMessageBroker;
import com.crunchshop.messagebroker.core.MessageConsumptionMode;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessMapper;
import com.crunchshop.messagebroker.event.AssetShared;

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
