package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.event.AssetShared;
import com.klipfolio.messagebroker.util.JSONUtil;
import com.klipfolio.messagebroker.util.MockEventUtils;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EventMessageSerializerTest {

    @Test
    public void testSerializesPayloadIntoString() throws Exception {
        EventMessageSerializer serializer = new EventMessageSerializer();
        AssetShared mockEvent = new AssetShared() {
            @Override
            public Map<String, Object> getPayload() {
                Map<String, Object> payload = new HashMap<>();
                payload.put("testStringField", "test");
                payload.put("testIntField", 1);
                return payload;
            }
        };
        String payload = serializer.serializeEventIntoPayload(mockEvent);
        String expectedPayload = "{\"testStringField\":\"test\",\"testIntField\":1}";

        JSONAssert.assertEquals(expectedPayload, payload, true);
    }

    @Test(expected = IOException.class)
    public void testThrowsIOExceptionIfUnableToSerializePayload(@Mocked final JSONUtil jsonUtil) throws Exception {
        new Expectations() {{
            jsonUtil.stringify(any); result = new Exception("Serialization failed.");
        }};
        EventMessageSerializer serializer = new EventMessageSerializer();
        AssetShared mockEvent = MockEventUtils.createMockEvent();
        serializer.serializeEventIntoPayload(mockEvent);
    }

    @Test
    public void testSerializesQueueMessage() throws Exception {
        AssetShared mockEvent = new AssetShared() {
            @Override
            public Map<String, Object> getPayload() {
                Map<String, Object> payload = new HashMap<>();
                payload.put("testStringField", "test");
                payload.put("testIntField", 1);
                return payload;
            }
        };
        EventMessageSerializer serializer = new EventMessageSerializer();
        String eventPayload = serializer.serializeEventIntoPayload(mockEvent);
        String eventName = mockEvent.getName();

        EventMessage eventMessage = new EventMessage(
                "sourceService",
                "sourceEnvironment",
                "environmentSpecificEventName",
                eventName,
                eventPayload,
                1
        );

        String payload = serializer.serializeQueueMessage(eventMessage.getBody());
        String expectedPayload = "{\"eventVersion\":1,\"sourceService\":\"sourceService\",\"sourceEnvironment\":\"sourceEnvironment\",\"environmentSpecificEventName\":\"environmentSpecificEventName\",\"eventName\":\"AssetShared\",\"eventPayload\":\"{\\\"testStringField\\\":\\\"test\\\",\\\"testIntField\\\":1}\"}";

        JSONAssert.assertEquals(expectedPayload, payload, true);
    }

    @Test(expected = IOException.class)
    public void testThrowsIOExceptionIfUnableToSerializeQueueMessage(@Mocked final JSONUtil jsonUtil) throws Exception {
        new Expectations() {{
            jsonUtil.stringify(any); result = new Exception("Serialization failed.");
        }};
        EventMessage eventMessage = new EventMessage(
                "sourceService",
                "sourceEnvironment",
                "environmentSpecificEventName",
                "eventName",
                "eventPayload",
                1
        );
        EventMessageSerializer serializer = new EventMessageSerializer();
        serializer.serializeQueueMessage(eventMessage.getBody());
    }
}
