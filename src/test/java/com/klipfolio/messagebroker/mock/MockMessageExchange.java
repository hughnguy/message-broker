package com.klipfolio.messagebroker.mock;

import com.klipfolio.messagebroker.core.MessageExchange;
import com.klipfolio.messagebroker.core.Topic;

public class MockMessageExchange implements MessageExchange {

    @Override
    public Topic getTopic() {
        return null;
    }

    @Override
    public void routeMessage(String serializedText) {

    }
}
