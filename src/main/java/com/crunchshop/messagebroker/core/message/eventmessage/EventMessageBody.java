package com.crunchshop.messagebroker.core.message.eventmessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventMessageBody {
    private String sourceService;
    private String sourceEnvironment;
    private String environmentSpecificEventName;
    private String eventName;
    private String eventPayload;
    private int eventVersion;
}
