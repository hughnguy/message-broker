package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.core.exception.MessageDiscardException;
import com.klipfolio.messagebroker.event.Event;
import com.klipfolio.messagebroker.event.GenericEvent;
import com.klipfolio.messagebroker.event.UserEvent;
import com.klipfolio.messagebroker.mock.*;
import com.klipfolio.messagebroker.util.MockEventUtils;
import com.klipfolio.messagebroker.core.exception.MessageRequeueException;
import com.klipfolio.messagebroker.event.AssetShared;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class EventMessageConsumerTest {

    @Test
    public void testConsumerNameIsEquivalentToClassName() {
        MockAssetSharedConsumer mockConsumer = new MockAssetSharedConsumer(new MockAssetSharedProcessor());
        Assert.assertEquals(MockAssetSharedConsumer.class.getSimpleName(), mockConsumer.getName());
    }

    @Test
    public void testConsumerRegistersCorrectEvents(@Mocked final EventSubscriber eventSubscriber) {
        new MockAssetSharedConsumer(new MockAssetSharedProcessor());

        new Verifications() {{
            EventProcessMapper eventProcessMapper;
            eventSubscriber.registerConsumerToEvent(eventProcessMapper = withCapture()); times = 1;
            Assert.assertEquals(AssetShared.class, eventProcessMapper.getEventClass());
        }};
    }

    @Test
    public void testConsumerProcessesCorrectEvent() throws Exception {
        AssetShared mockEvent = MockEventUtils.createMockEvent();
        MockAssetSharedProcessor mockProcessor = new MockAssetSharedProcessor();
        MockAssetSharedConsumer mockConsumer = new MockAssetSharedConsumer(mockProcessor);

        EventMessage eventMessage = MockEventUtils.createMockEventMessageForConsumer(mockEvent);
        mockConsumer.consumeMessage(eventMessage);

        Assert.assertTrue(mockProcessor.isProcessed());
        Assert.assertEquals(mockEvent.getName(), mockProcessor.getEventProcessed().getName());
    }

    @Test
    public void testConsumerProcessesNotInSupportedListEvent() throws Exception {
        Event mockEvent = new MockGenericEvent();
        MockGenericEventProcessor mockProcessor = new MockGenericEventProcessor();
        MockGenericEventConsumer mockConsumer = new MockGenericEventConsumer(mockProcessor) ;

        EventMessage eventMessage = MockEventUtils.createMockEventMessageForConsumer(mockEvent);
        mockConsumer.consumeMessage(eventMessage);

        Assert.assertTrue(mockProcessor.isProcessed());
        Assert.assertEquals(mockEvent.getName(), mockProcessor.getEventProcessed().getName());
    }

    @Test(expected = MessageDiscardException.class)
    public void testConsumerMessageWithNonJSONPayloadThrowsMessageCorruptException() throws Exception {
        AssetShared mockEvent = MockEventUtils.createMockEvent();
        MockAssetSharedConsumer mockConsumer = new MockAssetSharedConsumer(new MockAssetSharedProcessor());

        String eventPayload = "this is not json";
        String eventName = mockEvent.getName();

        EventMessage eventMessage = new EventMessage(
                "sourceService",
                "sourceEnvironment",
                "environmentSpecificEventName",
                eventName,
                eventPayload,
                1
        );

        mockConsumer.consumeMessage(eventMessage);
    }

    @Test(expected = MessageDiscardException.class)
    public void testConsumerMessageEventValidationFailureThrowsMessageCorruptException() throws Exception {
        AssetShared mockEvent = new AssetShared(null, null, null, null, null, null, null, null); // null fields will fail validation
        MockAssetSharedConsumer mockConsumer = new MockAssetSharedConsumer(new MockAssetSharedProcessor());

        EventMessage eventMessage = MockEventUtils.createMockEventMessageForConsumer(mockEvent);
        mockConsumer.consumeMessage(eventMessage);
    }

    @Test(expected = MessageRequeueException.class)
    public void testConsumerMessageProcessorFailureThrowsConsumerProcessingException() throws Exception {
        AssetShared mockEvent = MockEventUtils.createMockEvent();
        MockAssetSharedConsumer mockConsumer = new MockAssetSharedConsumer(new MockAssetSharedProcessor() {
            @Override
            public void processEvent(AssetShared event) throws Exception {
                throw new Exception("Processing failed");
            }
        });

        EventMessage eventMessage = MockEventUtils.createMockEventMessageForConsumer(mockEvent);
        mockConsumer.consumeMessage(eventMessage);
    }
}
