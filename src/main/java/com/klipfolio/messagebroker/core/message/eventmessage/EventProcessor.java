package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.event.Event;

public interface EventProcessor<T extends Event> {
    void processEvent(T event) throws Exception;
}
