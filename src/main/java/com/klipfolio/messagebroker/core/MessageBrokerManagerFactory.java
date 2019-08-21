package com.klipfolio.messagebroker.core;

import java.util.HashMap;
import java.util.Map;

public class MessageBrokerManagerFactory<T> {

    private Map<String, MessageBrokerManager<T>> topicToBrokerManagerMap = new HashMap<>();
    private MessageBrokerManager<T> defaultMessageBrokerManager;

    public MessageBrokerManagerFactory(MessageBrokerManager<T> defaultMessageBrokerManager) {
        this.defaultMessageBrokerManager = defaultMessageBrokerManager;
    }

    public void setMessageBrokerManagerForTopic(String topicName, MessageBrokerManager<T> messageBrokerManager) {
        topicToBrokerManagerMap.put(topicName, messageBrokerManager);
    }

    public MessageBrokerManager<T> getMessageBrokerManagerForTopic(String topicName) {
        MessageBrokerManager<T> messageBrokerManager = topicToBrokerManagerMap.get(topicName);

        if(messageBrokerManager != null) {
            return messageBrokerManager;
        } else {
            return getDefaultMessageBroker();
        }
    }

    private MessageBrokerManager<T> getDefaultMessageBroker() {
        return defaultMessageBrokerManager;
    }
}
