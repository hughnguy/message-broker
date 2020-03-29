package com.crunchshop.messagebroker.mock;

import com.crunchshop.messagebroker.core.MessageBrokerManager;
import com.crunchshop.messagebroker.core.MessageBrokerManagerFactory;

public class MockMessageBrokerManagerFactory extends MessageBrokerManagerFactory {

    public MockMessageBrokerManagerFactory(MessageBrokerManager messageBrokerManager) {
        super(messageBrokerManager);
    }

    public MockMessageBrokerManagerFactory() {
        super(new MockMessageBrokerManager(new MockMessageExchange()));
    }
}
