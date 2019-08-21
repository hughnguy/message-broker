package com.klipfolio.messagebroker.impl.aws;


class AWSQueueId {

    private final String queueUrl;
    private final String subscriptionId;

    AWSQueueId(String queueUrl, String subscriptionId) {
        this.queueUrl = queueUrl;
        this.subscriptionId = subscriptionId;
    }

    String getQueueUrl() {
        return queueUrl;
    }

    String getSubscriptionId() {
        return subscriptionId;
    }
}
