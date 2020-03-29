package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.event.Event;

public interface EventProcessor<T extends Event> {
    void processEvent(T event) throws Exception;
}
