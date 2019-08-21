package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.MessageBroker;
import com.klipfolio.messagebroker.MockMessageBroker;
import com.klipfolio.messagebroker.core.*;
import com.klipfolio.messagebroker.event.AssetShared;
import com.klipfolio.messagebroker.mock.*;
import com.klipfolio.messagebroker.util.MockEventUtils;
import mockit.*;
import org.junit.Test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventProcessMapperSubscriberTest {

    private MockAssetSharedConsumer createMockMessageQueueManagerForConsumer(final MockMessageBrokerManager mockMessageQueueManager) {
        return new MockAssetSharedConsumer(new MockAssetSharedProcessor()) {
            @Override
            public String getName() {
                return "MockAssetSharedConsumer";
            }
            @Override
            public MessageBroker getMessageBroker() {
                return new MockMessageBroker() {
                    @Override
                    public MessageBrokerManagerFactory getMessageBrokerManagerFactory() {
                        return new MockMessageBrokerManagerFactory(mockMessageQueueManager);
                    }
                };
            }
        };
    }

    @Test
    public void testLogErrorMessageIfFailedToRegisterConsumerToEvent() {
        final MockMessageBrokerManager mockMessageQueueManager = new MockMessageBrokerManager(new MockMessageExchange()) {
            @Override
            public void registerMessageConsumerToTopic(MessageConsumer consumer, Topic topic) {
                throw new RuntimeException("Registration to topic failed");
            }
        };
        MockAssetSharedConsumer mockConsumer = createMockMessageQueueManagerForConsumer(mockMessageQueueManager);

        EventSubscriber eventSubscriber = new EventSubscriber(mockConsumer);

        final Log logger = LogFactory.getLog(EventSubscriber.class.getSimpleName());

        new Expectations(logger) {{
            logger.error("Failed to register consumer MockAssetSharedConsumer to event AssetShared", (Throwable) any); times = 1;
        }};

        EventProcessMapper eventProcessMapper = new EventProcessMapper(AssetShared.class, new MockAssetSharedProcessor());
        eventSubscriber.registerConsumerToEvent(eventProcessMapper);
    }

    @Test
    public void testLogErrorMessageIfFailedToRemoveConsumerFromEvent() {
        final MockMessageBrokerManager mockMessageQueueManager = new MockMessageBrokerManager(new MockMessageExchange()) {
            @Override
            public void removeMessageConsumerFromTopic(MessageConsumer consumer, Topic topic) {
                throw new RuntimeException("Removal from topic failed");
            }
        };
        MockAssetSharedConsumer mockConsumer = createMockMessageQueueManagerForConsumer(mockMessageQueueManager);
        EventSubscriber eventSubscriber = new EventSubscriber(mockConsumer);

        final Log logger = LogFactory.getLog(EventSubscriber.class.getSimpleName());

        new Expectations(logger) {{
            logger.error("Unable to remove consumer MockAssetSharedConsumer from event AssetShared", (Throwable) any); times = 1;
        }};

        AssetShared mockEvent = MockEventUtils.createMockEvent();
        eventSubscriber.removeConsumerFromEvent(mockEvent.getName());
    }
}
