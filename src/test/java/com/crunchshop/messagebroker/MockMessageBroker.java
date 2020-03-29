package com.crunchshop.messagebroker;

import com.crunchshop.messagebroker.mock.MockMessageBrokerManagerFactory;
import com.crunchshop.messagebroker.core.MessageBrokerManagerFactory;

public class MockMessageBroker extends MessageBroker {

    private static MockMessageBroker singleton = null;

    public static MockMessageBroker getInstance() {
        if(singleton == null) {
            singleton = new MockMessageBroker();
        }
        return singleton;
    }

    @Override
    public MessageBrokerManagerFactory getMessageBrokerManagerFactory() {
        return new MockMessageBrokerManagerFactory();
    }

    @Override
    public String getServiceName() {
        return "mock_service_name";
    }

    @Override
    public String getUniqueInstanceName() {
        return "mock_unique_instance_name";
    }

    @Override
    public String getEnvironment() {
        return "mock_environment";
    }
}
