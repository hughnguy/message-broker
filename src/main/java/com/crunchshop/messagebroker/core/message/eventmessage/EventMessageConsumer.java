package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.core.Message;
import com.crunchshop.messagebroker.core.MessageConsumptionMode;
import com.crunchshop.messagebroker.core.exception.MessageDiscardException;
import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.event.GenericEvent;
import com.crunchshop.messagebroker.core.MessageConsumer;
import com.crunchshop.messagebroker.core.exception.MessageRequeueException;
import com.crunchshop.messagebroker.event.SupportedEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class EventMessageConsumer implements MessageConsumer<EventMessageBody> {

    private static final int MAX_RECEIVE_MESSAGE_COUNT = 10;
    private static final int LONG_POLLING_TIME_SECONDS = 1;
    private static final MessageConsumptionMode DEFAULT_CONSUMPTION_MODE = MessageConsumptionMode.DELIVER_ONCE_PER_SERVICE;

    private final Log log = LogFactory.getLog(getName());

    private EventMessageValidator validator = new EventMessageValidator(this);
    private Map<String, EventProcessor> eventNameToProcessMap = new HashMap<>();

    public EventMessageConsumer(EventProcessMapper... eventsToProcess) {
        this.registerEvents(eventsToProcess);
    }

    private void registerEvents(EventProcessMapper... eventsToProcess) {
        EventSubscriber eventSubscriber = new EventSubscriber(this);

        for(EventProcessMapper eventProcessMapper : eventsToProcess) {
            eventSubscriber.registerConsumerToEvent(eventProcessMapper);
            eventNameToProcessMap.put(eventProcessMapper.getEventName(), eventProcessMapper.getEventProcessor());
        }
    }

    public MessageBroker getMessageBroker() {
        return MessageBroker.getInstance();
    }

    @Override
    public MessageConsumptionMode getMessageConsumptionMode() {
        return DEFAULT_CONSUMPTION_MODE;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getMaxReceiveMessageCount() {
        return MAX_RECEIVE_MESSAGE_COUNT;
    }

    @Override
    public int getPollingIntervalInSeconds() {
        return LONG_POLLING_TIME_SECONDS;
    }

    @Override
    public EventMessageDeserializer getMessageDeserializer() {
        return new EventMessageDeserializer();
    }

    @Override
    public void consumeMessage(Message<EventMessageBody> message) throws MessageRequeueException, MessageDiscardException {
        EventMessageBody body = message.getBody();
        String eventName = body.getEventName();
        String eventPayload = body.getEventPayload();
        int eventVersion = body.getEventVersion();

        String logMessage = String.format(
                "Received event %s from %s(%s) with payload: %s",
                body.getEnvironmentSpecificEventName(),
                body.getSourceService(),
                body.getSourceEnvironment(),
                eventPayload
        );
        log.info(logMessage);

        Event eventWithNoPayload = getEventIfSupported(eventName);
        Event event = deserializePayloadIntoEvent(eventWithNoPayload, eventPayload);

        checkEventVersion(event, eventVersion);

        validateEvent(event);

        processEvent(event);
    }

    private Event getEventIfSupported(String eventName) throws MessageDiscardException, MessageRequeueException {
        SupportedEvent supportedEvent = SupportedEvent.getByName(eventName);

        if(supportedEvent != null) {
            try {
                return supportedEvent.getNewEventInstance();
            } catch(IllegalAccessException | InstantiationException e) {
                throw new MessageDiscardException(e);
            }
        } else {
            return new GenericEvent();
        }
    }

    private void checkEventVersion(Event event, int eventVersion) throws MessageRequeueException {
        if(event.getVersion() != eventVersion) {
            throw new MessageRequeueException("Current service node is not able to process this event version. Re-queuing event.");
        }
    }

    private Event deserializePayloadIntoEvent(Event event, String payload) throws MessageDiscardException {
        try {
            return getMessageDeserializer().deserializePayloadIntoEvent(event, payload);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new MessageDiscardException(e);
        }
    }

    private void validateEvent(Event event) throws MessageDiscardException, MessageRequeueException {
        try {
            validator.validate(event);

        } catch(MessageDiscardException | MessageRequeueException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    private void processEvent(Event event) throws MessageRequeueException {
        try {
            EventProcessor eventProcessor = getEventProcessor(event);
            eventProcessor.processEvent(event);

        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new MessageRequeueException(e);
        }
    }

    EventProcessor getEventProcessor(Event event) {
        return eventNameToProcessMap.get(event.getName());
    }
}
