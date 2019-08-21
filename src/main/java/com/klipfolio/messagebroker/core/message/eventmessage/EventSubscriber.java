package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.core.MessageBrokerManager;
import com.klipfolio.messagebroker.core.MessageBrokerManagerFactory;
import com.klipfolio.messagebroker.core.Topic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class EventSubscriber {

    private static final Log LOG = LogFactory.getLog(EventSubscriber.class.getSimpleName());

    private EventMessageConsumer consumer;

    EventSubscriber(EventMessageConsumer consumer) {
        this.consumer = consumer;
    }

    void registerConsumerToEvent(EventProcessMapper eventProcessMapper) {
        String eventName = eventProcessMapper.getEventName();

        if(eventName != null) {
            MessageBrokerManager<EventMessageBody> manager = getMessageBrokerManagerForEvent(eventName);
            try {
                manager.registerMessageConsumerToTopic(consumer, createTopicFromEventName(manager, eventName));
            } catch (Exception e) {
                LOG.error("Failed to register consumer " + consumer.getName() + " to event " + eventName, e);
            }
        }
    }

    void removeConsumerFromEvent(String eventName) {
        MessageBrokerManager<EventMessageBody> manager = getMessageBrokerManagerForEvent(eventName);
        try {
            manager.removeMessageConsumerFromTopic(consumer, createTopicFromEventName(manager, eventName));
        } catch(Exception e) {
            LOG.error("Unable to remove consumer " + consumer.getName() + " from event " + eventName, e);
        }
    }

    EventMessageConsumer getConsumer() {
        return consumer;
    }

    private Topic createTopicFromEventName(MessageBrokerManager<EventMessageBody> manager, String eventName) {
        String environmentSpecificEventName = consumer.getMessageBroker().generateEnvironmentSpecificTopicName(eventName);
        return manager.createTopic(environmentSpecificEventName);
    }

    private MessageBrokerManager<EventMessageBody> getMessageBrokerManagerForEvent(String eventName) {
        MessageBrokerManagerFactory factory = consumer.getMessageBroker().getMessageBrokerManagerFactory();
        return factory.getMessageBrokerManagerForTopic(eventName);
    }
}
