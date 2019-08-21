package com.klipfolio.messagebroker.impl.aws;

import com.klipfolio.messagebroker.core.Topic;

class AWSTopic implements Topic {

    private final String name;

    private String topicArn;

    AWSTopic(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    String getTopicArn() {
        return topicArn;
    }
}
