package com.klipfolio.messagebroker.core;

public enum MessageConsumptionMode {
    /**
     * All consumers in a service group will receive the message
     *
     * ex: all nodes will consume a message to clear their cache
     */
    BROADCAST_TO_ALL_CONSUMERS,
    /**
     * Only 1 consumer in a service group will receive the message
     *
     * ex: only 1 node should consume the message and send a single email
     */
    DELIVER_ONCE_PER_SERVICE
}
