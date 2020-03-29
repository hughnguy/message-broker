package com.crunchshop.messagebroker.mock;

import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessor;
import com.crunchshop.messagebroker.event.AssetShared;

public class MockAssetSharedProcessor implements EventProcessor<AssetShared> {

    private boolean processed = false;
    private Event eventProcessed;

    @Override
    public void processEvent(AssetShared event) throws Exception {
        processed = true;
        eventProcessed = event;
    }

    public boolean isProcessed() {
        return processed;
    }

    public Event getEventProcessed() {
        return eventProcessed;
    }
}
