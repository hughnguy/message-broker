package com.crunchshop.messagebroker.event;

import com.crunchshop.messagebroker.core.exception.EventInvalidFieldException;

import java.util.Map;

public interface Event {
    /**
     * Get the name of the event
     * @return the event name
     */
    String getName();

    /**
     * Get the event version number. During deployments,
     * if an old node is still running and receives a newer version of an event,
     * it will re-queue the message until a new node receives and processes the event
     * @return the version number of event
     */
    int getVersion();

    /**
     * The payload to be serialized and transported onto the queue
     * @return the payload
     */
    Map<String, Object> getPayload();

    /**
     * Creates the event object from the de-serialized payload
     * @param payload the de-serialized payload from queue message
     */
    void createFromPayload(Map<String, Object> payload);

    /**
     * Validate the event contains the correct fields
     * @throws EventInvalidFieldException
     */
    void validate() throws EventInvalidFieldException;
}
