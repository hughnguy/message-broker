package com.crunchshop.messagebroker.mock;

import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessor;

public class MockGenericEventProcessor implements EventProcessor<Event> {

    private boolean processed = false;
    private Event eventProcessed;

    @Override
    public void processEvent(Event event) throws Exception {
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
