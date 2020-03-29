package com.crunchshop.messagebroker.mock;

import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.core.exception.EventInvalidFieldException;

import java.util.HashMap;
import java.util.Map;

public class MockGenericEvent implements Event {
    private String name = "MockGenericEvent";
    private int version = 1;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", getName());
        payload.put("version", getVersion());
        return payload;
    }

    @Override
    public void createFromPayload(Map<String, Object> payload) {
        name = (String) payload.get("name");
        version = (int) payload.get("version");
    }

    @Override
    public void validate() throws EventInvalidFieldException {
        if (name == null) throw new EventInvalidFieldException("name is missing");
    }
}
