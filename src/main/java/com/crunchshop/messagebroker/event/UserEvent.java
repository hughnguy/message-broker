package com.crunchshop.messagebroker.event;

import com.crunchshop.messagebroker.core.exception.EventInvalidFieldException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class UserEvent implements Event {

    private String userId;

    @Override
    public Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();

        payload.put("userId", userId);

        return payload;
    }

    @Override
    public void createFromPayload(Map<String, Object> payload) {
        userId = (String) payload.get("userId");
    }

    @Override
    public void validate() throws EventInvalidFieldException {
        if(userId == null) {
            throw new EventInvalidFieldException("User event is missing a user id.");
        }
    }
}
