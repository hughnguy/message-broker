package com.crunchshop.messagebroker.event;

import com.crunchshop.messagebroker.core.exception.EventInvalidFieldException;

import java.util.HashMap;
import java.util.Map;

public class GenericEvent implements Event {
    private Map <String, Object> payload;

    public GenericEvent() {
        payload = new HashMap<>();
    }

    @Override
    public String getName() {
        return (String) payload.get("name");
    }

    @Override
    public int getVersion() {
        Object val = payload.get("version");
        return val != null ? (int)payload.get("version") : 1;
    }

    public void setAttribute(String key, String value) {
        payload.put(key, value);
    }

    public void setAttribute(String key, int value) {
        payload.put(key, value);
    }

    @Override
    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public void createFromPayload(Map<String, Object> payload) {
        this.payload.putAll(payload);
    }

    @Override
    public void validate() throws EventInvalidFieldException {
        if (payload.get("name") == null) {
            throw new EventInvalidFieldException("Missing required name field.");
        }
    }
}
