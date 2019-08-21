package com.klipfolio.messagebroker.mock;

import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessor;
import com.klipfolio.messagebroker.event.AssetShared;
import com.klipfolio.messagebroker.event.Event;

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
