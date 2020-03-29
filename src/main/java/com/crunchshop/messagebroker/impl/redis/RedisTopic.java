package com.crunchshop.messagebroker.impl.redis;

import com.crunchshop.messagebroker.core.Topic;

public class RedisTopic implements Topic {

    private final String name;

    /**
     * Redis set containing all queues binded to topic
     */
    private String redisSetKey;

    RedisTopic(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    
    void setRedisSetKey(String redisTopicSetKey) {
        this.redisSetKey = redisTopicSetKey;
    }

    String getRedisSetKey() {
        return redisSetKey;
    }
}
