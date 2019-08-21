package com.klipfolio.messagebroker.impl.redis;

import com.klipfolio.messagebroker.core.MessageExchange;
import com.klipfolio.messagebroker.core.Topic;

class RedisMessageExchange implements MessageExchange {

    private final RedisClient redisClient;
    private final RedisTopic topic;

    RedisMessageExchange(RedisClient redisClient, RedisTopic topic) {
        this.redisClient = redisClient;
        this.topic = topic;
        topic.setRedisSetKey(generateTopicSetName(topic.getName()));
    }

    private String generateTopicSetName(String topicName) {
        return "topic:" + topicName + ":queues";
    }

    @Override
    public void routeMessage(String serializedText) {
        redisClient.writeMessageToAllSubscribedQueues((RedisTopic) getTopic(), serializedText);
    }

    @Override
    public Topic getTopic() {
        return topic;
    }
}
