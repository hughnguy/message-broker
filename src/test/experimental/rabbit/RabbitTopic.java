package com.crunchshop.experimental.rabbit;

import com.crunchshop.messagebroker.core.Topic;

public class RabbitTopic implements Topic {

    private final String name;

    RabbitTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
