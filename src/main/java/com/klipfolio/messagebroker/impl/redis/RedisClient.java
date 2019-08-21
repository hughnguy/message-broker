package com.klipfolio.messagebroker.impl.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class RedisClient {

    private JedisPool jedisPool;

    RedisClient(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    void subscribeQueueToTopic(String queueName, RedisTopic redisTopic) {
        addQueueToSet(redisTopic.getRedisSetKey(), queueName);
    }

    void unsubscribeQueueFromTopic(String queueName, RedisTopic redisTopic) {
        removeQueueFromSet(redisTopic.getRedisSetKey(), queueName);
    }

    void writeMessageToAllSubscribedQueues(RedisTopic redisTopic, String message) {
        List<String> queueNames = getAllQueuesSubscribedToTopic(redisTopic);
        for(String queueName : queueNames) {
            writeMessageToQueue(queueName, message);
        }
    }

    List<String> batchReadMessagesFromQueue(String queueName, int maxNumberOfMessages) {
        return batchReadFromQueue(queueName, maxNumberOfMessages);
    }

    private void writeMessageToQueue(String queueName, String message) {
        writeToQueue(queueName, message);
    }

    private List<String> getAllQueuesSubscribedToTopic(RedisTopic redisTopic) {
        Set<String> queues = getAllMembersInSet(redisTopic.getRedisSetKey());
        return new ArrayList<>(queues);
    }

    private List<String> batchReadFromQueue(String queueKey, int maxNumberOfMessages) {
        List<String> messages = new LinkedList<>();

        for(int i =0; i < maxNumberOfMessages; i++) {
            String message = readFromQueue(queueKey);
            if(message != null) {
                messages.add(message);
            } else {
                break;
            }
        }
        return messages;
    }

    private String readFromQueue(String queueKey) {
        Jedis jedis = getJedis();
        try {
            return jedis.lpop(queueKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    private void writeToQueue(String queueKey, String message) {
        Jedis jedis = getJedis();
        try {
            jedis.rpush(queueKey, message);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    private void addQueueToSet(String setName, String queueKey) {
        Jedis jedis = getJedis();
        try {
            jedis.sadd(setName, queueKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    private void removeQueueFromSet(String setName, String queueKey) {
        Jedis jedis = getJedis();
        try {
            jedis.srem(setName, queueKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    private Set<String> getAllMembersInSet(String setKey) {
        Jedis jedis = getJedis();
        try {
            return jedis.smembers(setKey);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}
