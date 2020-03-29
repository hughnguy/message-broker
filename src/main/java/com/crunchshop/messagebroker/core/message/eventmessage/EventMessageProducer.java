package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.core.*;
import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.core.exception.EventPublishException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventMessageProducer implements MessageProducer<EventMessageBody> {

    private static final Log LOG = LogFactory.getLog(EventMessageProducer.class.getSimpleName());

    MessageBrokerManager<EventMessageBody> manager;

    public EventMessageProducer(MessageBrokerManager<EventMessageBody> manager) {
        this.manager = manager;
    }

    public void publish(
            String sourceService,
            String sourceEnvironment,
            String environmentSpecificEventName,
            Event event
    ) throws EventPublishException {
        try {
            event.validate();

            Topic topic = manager.createTopic(environmentSpecificEventName);
            String eventPayload = getMessageSerializer().serializeEventIntoPayload(event);
            String eventName = event.getName();
            int eventVersion = event.getVersion();

            EventMessage eventMessage = new EventMessage(
                    sourceService,
                    sourceEnvironment,
                    environmentSpecificEventName,
                    eventName,
                    eventPayload,
                    eventVersion
            );

            publish(topic, eventMessage);

        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            throw new EventPublishException(e);
        }
    }

    @Override
    public void publish(Topic topic, Message<EventMessageBody> message) throws Exception {
        MessageExchange exchange = getExchange(topic);
        String serializedText = getMessageSerializer().serializeQueueMessage(message.getBody());
        exchange.routeMessage(serializedText);
    }

    @Override
    public MessageExchange getExchange(Topic topic) {
        return manager.getExchange(topic);
    }

    @Override
    public EventMessageSerializer getMessageSerializer() {
        return new EventMessageSerializer();
    }
}
