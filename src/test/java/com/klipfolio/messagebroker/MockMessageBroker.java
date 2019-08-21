package com.klipfolio.messagebroker;

import com.klipfolio.messagebroker.core.MessageBrokerManagerFactory;
import com.klipfolio.messagebroker.mock.MockMessageBrokerManagerFactory;

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
