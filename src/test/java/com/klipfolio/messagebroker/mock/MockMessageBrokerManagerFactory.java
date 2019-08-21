package com.klipfolio.messagebroker.mock;

import com.klipfolio.messagebroker.core.MessageBrokerManager;
import com.klipfolio.messagebroker.core.MessageBrokerManagerFactory;

public class MockMessageBrokerManagerFactory extends MessageBrokerManagerFactory {

    public MockMessageBrokerManagerFactory(MessageBrokerManager messageBrokerManager) {
        super(messageBrokerManager);
    }

    public MockMessageBrokerManagerFactory() {
        super(new MockMessageBrokerManager(new MockMessageExchange()));
    }
}
