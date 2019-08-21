package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.core.Message;
import com.klipfolio.messagebroker.core.exception.MessageDiscardException;
import com.klipfolio.messagebroker.event.AssetShared;
import com.klipfolio.messagebroker.event.Event;
import com.klipfolio.messagebroker.util.JSONUtil;
import com.klipfolio.messagebroker.util.MockEventUtils;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EventMessageDeserializerTest {

    @Test
    public void testDeserializesPayloadIntoCorrectEventWithFields() throws Exception {
        EventMessageDeserializer deserializer = new EventMessageDeserializer();
        AssetShared mockEvent = MockEventUtils.createMockEvent();

        Map payload = mockEvent.getPayload();
        String stringPayload = JSONUtil.stringify(payload);

        Event event = deserializer.deserializePayloadIntoEvent(mockEvent, stringPayload);
        AssetShared assetShared = (AssetShared) event;

        Assert.assertEquals(mockEvent.getName(), event.getName());
        Assert.assertEquals(mockEvent.getAssetId(), assetShared.getAssetId());
        Assert.assertEquals(mockEvent.getAssetName(), assetShared.getAssetName());
        Assert.assertEquals(mockEvent.getAssetTypeDisplayName(), assetShared.getAssetTypeDisplayName());
        Assert.assertEquals(mockEvent.getRawAssetType(), assetShared.getRawAssetType());
        Assert.assertEquals(mockEvent.getShareTargetType(), assetShared.getShareTargetType());
        Assert.assertEquals(mockEvent.getShareTargetId(), assetShared.getShareTargetId());
        Assert.assertEquals(mockEvent.getShareRightType(), assetShared.getShareRightType());
        Assert.assertEquals(mockEvent.getSharerId(), assetShared.getSharerId());
    }

    @Test(expected = MessageDiscardException.class)
    public void testThrowsMessageCorruptExceptionIfUnableToDeserializeInvalidJSONPayloadIntoEvent() throws Exception {
        EventMessageDeserializer deserializer = new EventMessageDeserializer();
        AssetShared mockEvent = MockEventUtils.createMockEvent();
        deserializer.deserializePayloadIntoEvent(mockEvent, "not a valid json payload");
    }

    @Test(expected = MessageDiscardException.class)
    public void testThrowsMessageCorruptExceptionIfMessagePayloadFieldContainsIncorrectType() throws Exception {
        EventMessageDeserializer deserializer = new EventMessageDeserializer();
        AssetShared mockEvent = MockEventUtils.createMockEvent();
        mockEvent.getPayload();
        String payload = "{\"assetId\":{\"someField\":\"someValue\"}}";
        deserializer.deserializePayloadIntoEvent(mockEvent, payload);
    }

    @Test
    public void testDeserializesQueueMessage() throws Exception {
        final String sourceService = "sourceService";
        final String sourceEnvironment = "sourceEnvironment";
        final String environmentSpecificEventName = "environmentSpecificEventName";
        final String testStringEventField = "test";
        final int testIntEventField = 1;

        AssetShared mockEvent = new AssetShared() {
            @Override
            public Map<String, Object> getPayload() {
                Map<String, Object> payload = new HashMap<>();
                payload.put("testStringField", testStringEventField);
                payload.put("testIntField", testIntEventField);
                return payload;
            }
        };
        EventMessageSerializer serializer = new EventMessageSerializer();
        EventMessageDeserializer deserializer = new EventMessageDeserializer();
        String eventPayload = serializer.serializeEventIntoPayload(mockEvent);
        String eventName = mockEvent.getName();

        EventMessage eventMessage = new EventMessage(
                sourceService,
                sourceEnvironment,
                environmentSpecificEventName,
                eventName,
                eventPayload,
                1
        );
        EventMessageBody expectedEventMessageBody = eventMessage.getBody();

        String serializedPayload = "{\"sourceService\":\""+sourceService+"\",\"sourceEnvironment\":\""+sourceEnvironment+"\",\"environmentSpecificEventName\":\""+environmentSpecificEventName+"\",\"eventName\":\""+eventName+"\",\"eventPayload\":\"{\\\"testStringField\\\":\\\""+testStringEventField+"\\\",\\\"testIntField\\\":"+testIntEventField+"}\"}";
        Message<EventMessageBody> message = deserializer.deserializeQueueMessage(serializedPayload);
        EventMessageBody eventMessageBodyAfterDeserialization = message.getBody();

        Assert.assertEquals(expectedEventMessageBody.getEventName(), eventMessageBodyAfterDeserialization.getEventName());
        Assert.assertEquals(expectedEventMessageBody.getSourceService(), eventMessageBodyAfterDeserialization.getSourceService());
        Assert.assertEquals(expectedEventMessageBody.getSourceEnvironment(), eventMessageBodyAfterDeserialization.getSourceEnvironment());
        Assert.assertEquals(expectedEventMessageBody.getEnvironmentSpecificEventName(), eventMessageBodyAfterDeserialization.getEnvironmentSpecificEventName());
        JSONAssert.assertEquals(expectedEventMessageBody.getEventPayload(), eventMessageBodyAfterDeserialization.getEventPayload(), true);
    }

    @Test(expected = IOException.class)
    public void testThrowsIOExceptionIfUnableToDeserializeQueueMessage() throws Exception {
        EventMessageDeserializer deserializer = new EventMessageDeserializer();
        deserializer.deserializeQueueMessage("cannot deserialize");
    }
}
