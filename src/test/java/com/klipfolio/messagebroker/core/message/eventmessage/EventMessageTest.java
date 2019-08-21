package com.klipfolio.messagebroker.core.message.eventmessage;

import org.junit.Assert;
import org.junit.Test;

public class EventMessageTest {

    @Test
    public void testEventMessageConstructorSetAllCorrectFieldsInBody() {
        final String sourceService = "sourceService";
        final String sourceEnvironment = "sourceEnvironment";
        final String environmentSpecificEventName = "environmentSpecificEventName";
        final String eventName = "eventName";
        final String eventPayload = "eventPayload";
        final int eventVersion = 1;

        EventMessage eventMessage = new EventMessage(
                sourceService,
                sourceEnvironment,
                environmentSpecificEventName,
                eventName,
                eventPayload,
                eventVersion
        );

        EventMessageBody body = eventMessage.getBody();

        Assert.assertEquals(body.getSourceService(), sourceService);
        Assert.assertEquals(body.getSourceEnvironment(), sourceEnvironment);
        Assert.assertEquals(body.getEnvironmentSpecificEventName(), environmentSpecificEventName);
        Assert.assertEquals(body.getEventName(), eventName);
        Assert.assertEquals(body.getEventPayload(), eventPayload);
        Assert.assertEquals(body.getEventVersion(), eventVersion);
    }
}
