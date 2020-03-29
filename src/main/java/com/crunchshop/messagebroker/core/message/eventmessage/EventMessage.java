package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.core.Message;

public class EventMessage implements Message<EventMessageBody> {

    private EventMessageBody body;

    EventMessage(EventMessageBody body) {
        this.body = body;
    }

    public EventMessage(
            String sourceService,
            String sourceEnvironment,
            String environmentSpecificEventName,
            String eventName,
            String eventPayload,
            int eventVersion
    ) {
        body = new EventMessageBody(
                sourceService,
                sourceEnvironment,
                environmentSpecificEventName,
                eventName,
                eventPayload,
                eventVersion
        );
    }

    @Override
    public EventMessageBody getBody() {
        return body;
    }
}
