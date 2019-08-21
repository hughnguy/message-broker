package com.klipfolio.messagebroker.mock;

import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessor;
import com.klipfolio.messagebroker.event.Event;

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
