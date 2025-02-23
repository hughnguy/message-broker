package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.core.exception.ConsumerNotListeningException;
import com.crunchshop.messagebroker.core.exception.MessageDiscardException;
import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.core.exception.MessageRequeueException;
import com.crunchshop.messagebroker.core.exception.EventInvalidFieldException;

class EventMessageValidator {

    private EventMessageConsumer consumer;

    EventMessageValidator(EventMessageConsumer consumer) {
        this.consumer = consumer;
    }

    void validate(Event event) throws MessageDiscardException, MessageRequeueException {
        String eventName = event.getName();
        try {
            checkIfConsumerIsListeningToEvent(event);
            event.validate();

        } catch(ConsumerNotListeningException e) {
            throw new MessageRequeueException(new StringBuilder()
                    .append("This consumer is not listening to the event: ")
                    .append(eventName)
                    .append(". Re-queuing in case a recently deployed instance has subscribed to it. ")
                    .append("If no instances are listening, then the queue subscription should be manually removed from the topic.").toString(), e);

        } catch(EventInvalidFieldException e) {
            throw new MessageDiscardException("Failed to validate event: " + e.getMessage(), e);
        }
    }

    private void checkIfConsumerIsListeningToEvent(Event event) throws ConsumerNotListeningException {
        EventProcessor eventProcessor = consumer.getEventProcessor(event);

        if(eventProcessor == null) {
            throw new ConsumerNotListeningException();
        }
    }
}
