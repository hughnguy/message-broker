package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.event.Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventProcessMapper {

    private static final Log LOG = LogFactory.getLog(EventProcessMapper.class.getSimpleName());

    private EventProcessor eventProcessor;
    private Class<? extends Event> eventClass;

    public EventProcessMapper(
            Class<? extends Event> eventClass,
            EventProcessor eventProcessor
    ) {
        this.eventClass = eventClass;
        this.eventProcessor = eventProcessor;
    }

    EventProcessor getEventProcessor() {
        return eventProcessor;
    }

    Class<? extends Event> getEventClass() {
        return eventClass;
    }

    String getEventName() {
        try {
            return eventClass.newInstance().getName();

        } catch(IllegalAccessException | InstantiationException e) {
            LOG.error("Unable to get supported event name for event class.", e);
            return null;
        }
    }
}
