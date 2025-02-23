package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.core.MessageSerializer;
import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.util.JSONUtil;

import java.io.IOException;
import java.util.Map;

public class EventMessageSerializer implements MessageSerializer<EventMessageBody> {

    public String serializeEventIntoPayload(Event event) throws IOException {
        try {
            Map<String,Object> payload = event.getPayload();
            return JSONUtil.stringify(payload);

        } catch(Exception e) {
            throw new IOException("Unable to serialize fields for event: " + event.getName(), e);
        }
    }

    @Override
    public String serializeQueueMessage(EventMessageBody message) throws IOException {
        try {
            return JSONUtil.stringify(message);

        } catch(Exception e) {
            throw new IOException("Unable to serialize fields for event message body with event payload: " + message.getEventPayload(), e);
        }
    }
}
