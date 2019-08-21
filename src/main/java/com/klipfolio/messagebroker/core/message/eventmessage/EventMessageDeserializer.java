package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.core.Message;
import com.klipfolio.messagebroker.core.MessageDeserializer;
import com.klipfolio.messagebroker.core.exception.MessageDiscardException;
import com.klipfolio.messagebroker.event.Event;
import com.klipfolio.messagebroker.util.JSONUtil;

import java.io.IOException;
import java.util.Map;

public class EventMessageDeserializer implements MessageDeserializer<EventMessageBody> {

    Event deserializePayloadIntoEvent(Event event, String payload) throws MessageDiscardException {
        try {
            Map<String, Object> map = JSONUtil.parse(payload);
            event.createFromPayload(map);
            return event;

        } catch(Exception e) {
            throw new MessageDiscardException("Failed to deserialize event: " + event.getName() + ". Unable to parse json payload: " + payload, e);
        }
    }

    @Override
    public Message<EventMessageBody> deserializeQueueMessage(String serializedText) throws IOException {
        try {
            EventMessageBody eventMessageBody = (EventMessageBody) JSONUtil.parse(serializedText, EventMessageBody.class);
            return new EventMessage(eventMessageBody);

        } catch(IOException e) {
            throw new IOException("Unable to deserialize text received from queue: " + serializedText, e);
        }
    }
}
