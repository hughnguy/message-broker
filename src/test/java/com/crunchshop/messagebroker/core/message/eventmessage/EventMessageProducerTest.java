package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.mock.MockMessageExchange;
import com.crunchshop.messagebroker.core.exception.EventPublishException;
import com.crunchshop.messagebroker.event.AssetShared;
import com.crunchshop.messagebroker.mock.MockMessageBrokerManager;
import com.crunchshop.messagebroker.util.MockEventUtils;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class EventMessageProducerTest {

    @Test
    public void testPublishGetsExchangeAndCallsSendMessage(@Mocked final MockMessageExchange mockMessageExchange) throws Exception {
        EventMessageProducer eventMessageProducer = new EventMessageProducer(new MockMessageBrokerManager(new MockMessageExchange()));
        AssetShared mockEvent = MockEventUtils.createMockEvent();
        eventMessageProducer.publish("sourceService", "sourceEnvironment", "environmentSpecificEventName", mockEvent);

        new Verifications() {{
            String serializedText;
            String expectedSerializedText = "{\"sourceService\":\"sourceService\",\"sourceEnvironment\":\"sourceEnvironment\",\"environmentSpecificEventName\":\"environmentSpecificEventName\",\"eventName\":\"AssetShared\",\"eventPayload\":\"{\\\"assetName\\\":\\\"assetName\\\",\\\"sharerId\\\":\\\"sharerId\\\",\\\"assetTypeDisplayName\\\":\\\"assetTypeDisplayName\\\",\\\"assetId\\\":\\\"assetId\\\",\\\"shareTargetType\\\":\\\"shareTargetType\\\",\\\"rawAssetType\\\":\\\"rawAssetType\\\",\\\"shareRightType\\\":\\\"shareRightType\\\",\\\"shareTargetId\\\":\\\"shareTargetId\\\"}\"}";
            mockMessageExchange.routeMessage(serializedText = withCapture()); times = 1;

            EventMessageDeserializer eventMessageDeserializer = new EventMessageDeserializer();
            EventMessageBody expectedEventMessageBody = eventMessageDeserializer.deserializeQueueMessage(expectedSerializedText).getBody();
            EventMessageBody eventMessageBody = eventMessageDeserializer.deserializeQueueMessage(serializedText).getBody();

            JSONAssert.assertEquals(expectedEventMessageBody.getEventPayload(), eventMessageBody.getEventPayload(), true);
            Assert.assertEquals(expectedEventMessageBody.getEventName(), eventMessageBody.getEventName());
            Assert.assertEquals(expectedEventMessageBody.getEnvironmentSpecificEventName(), eventMessageBody.getEnvironmentSpecificEventName());
            Assert.assertEquals(expectedEventMessageBody.getSourceEnvironment(), eventMessageBody.getSourceEnvironment());
            Assert.assertEquals(expectedEventMessageBody.getSourceService(), eventMessageBody.getSourceService());
        }};
    }

    @Test(expected = EventPublishException.class)
    public void testPublishWithInvalidEventFieldThrowsEventPublishException() throws Exception {
        EventMessageProducer eventMessageProducer = new EventMessageProducer(new MockMessageBrokerManager(new MockMessageExchange()));
        AssetShared mockEvent = new AssetShared(null, null, null, null, null, null, null, null);
        eventMessageProducer.publish("sourceService", "sourceEnvironment", "environmentSpecificEventName", mockEvent);
    }

    @Test(expected = EventPublishException.class)
    public void testFailedEventPayloadSerializationThrowsEventPublishException(@Mocked final EventMessageSerializer eventMessageSerializer) throws Exception {
        new Expectations() {{
            new EventMessageSerializer(); result = eventMessageSerializer;
            eventMessageSerializer.serializeEventIntoPayload((Event) any); result = new Exception("Serialization of payload failed.");
        }};
        EventMessageProducer eventMessageProducer = new EventMessageProducer(new MockMessageBrokerManager(new MockMessageExchange()));

        AssetShared mockEvent = MockEventUtils.createMockEvent();
        eventMessageProducer.publish("sourceService", "sourceEnvironment", "environmentSpecificEventName", mockEvent);
    }
}
