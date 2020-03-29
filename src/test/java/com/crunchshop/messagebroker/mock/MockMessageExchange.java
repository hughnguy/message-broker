package com.crunchshop.messagebroker.mock;

import com.crunchshop.messagebroker.core.MessageExchange;
import com.crunchshop.messagebroker.core.Topic;

public class MockMessageExchange implements MessageExchange {

    @Override
    public Topic getTopic() {
        return null;
    }

    @Override
    public void routeMessage(String serializedText) {

    }
}
