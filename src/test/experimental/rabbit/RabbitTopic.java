package com.klipfolio.experimental.rabbit;

import com.klipfolio.messagebroker.core.Topic;

public class RabbitTopic implements Topic {

    private final String name;

    RabbitTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
