package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.core.MessageBrokerManagerFactory;
import com.crunchshop.messagebroker.core.MessageConsumer;
import com.crunchshop.messagebroker.core.Topic;
import com.crunchshop.messagebroker.mock.*;
import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.MockMessageBroker;
import com.crunchshop.messagebroker.event.AssetShared;
import com.crunchshop.messagebroker.util.MockEventUtils;
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
