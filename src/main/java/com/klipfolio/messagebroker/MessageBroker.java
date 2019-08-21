package com.klipfolio.messagebroker;

import com.klipfolio.messagebroker.core.MessageBrokerManager;
import com.klipfolio.messagebroker.core.MessageBrokerManagerFactory;
import com.klipfolio.messagebroker.core.exception.EventPublishException;
import com.klipfolio.messagebroker.core.message.eventmessage.EventMessageBody;
import com.klipfolio.messagebroker.core.message.eventmessage.EventMessageProducer;
import com.klipfolio.messagebroker.event.Event;
import lombok.NonNull;

public class MessageBroker {

    private String serviceName;
    private String environment;
    private String uniqueInstanceName;

    private static MessageBroker singleton = null;

    private MessageBrokerManagerFactory messageBrokerManagerFactory;

    protected MessageBroker() {}

    private MessageBroker(
            String serviceName,
            String environment,
            String uniqueInstanceName,
            MessageBrokerManagerFactory messageBrokerManagerFactory
    ) {
        this.serviceName = serviceName;
        this.environment = environment;
        this.uniqueInstanceName = uniqueInstanceName;
        this.messageBrokerManagerFactory = messageBrokerManagerFactory;
    }

    /**
     * Initializes the message broker
     * @param serviceName the name of service. Multiple nodes of this service will share the same name (ex: webui)
     * @param environment the name of environment (ex: dev, canary, prod)
     * @param uniqueInstanceName the unique name of this node (ex: webui-1, webui-2)
     * @param messageBrokerManagerFactory the message queue manager factory containing default queue implementation
     * @return
     */
    public synchronized static MessageBroker init(
            @NonNull String serviceName,
            @NonNull String environment,
            @NonNull String uniqueInstanceName,
            @NonNull MessageBrokerManagerFactory messageBrokerManagerFactory
    ) {
        if (singleton != null) {
            return singleton;
        }

        singleton = new MessageBroker(
                serviceName,
                environment,
                uniqueInstanceName,
                messageBrokerManagerFactory
        );
        return singleton;
    }

    /**
     * Returns the singleton instance of this class
     * @return the singleton instance
     */
    public static MessageBroker getInstance() {
        if(singleton == null) {
            throw new AssertionError("You have to call init first");
        }
        return singleton;
    }

    /**
     * Returns the factory that manages all the different queue implementations
     * @return the message queue manager factory
     */
    public MessageBrokerManagerFactory getMessageBrokerManagerFactory() {
        return messageBrokerManagerFactory;
    }

    /**
     * Switch queue implementations (redis, aws, rabbitmq) for the specific event type
     * @param eventClass the event class to set a specific queue type to
     * @param messageBrokerManager the message queue manager of queue type
     */
    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private void switchQueueImplementationForEvent(Class<? extends Event> eventClass, MessageBrokerManager messageBrokerManager) {
        messageBrokerManagerFactory.setMessageBrokerManagerForTopic(eventClass.getSimpleName(), messageBrokerManager);
    }

    /**
     * Topic name to fan out messages to consumers only in a specific environment (dev, canary, prod, etc.)
     * @param topicName the name of topic
     * @return the environment specific name of topic
     */
    public String generateEnvironmentSpecificTopicName(String topicName) {
        return new StringBuilder()
                .append(topicName)
                .append("-")
                .append(getEnvironment())
                .toString();
    }

    /**
     * Queue name will determine which messages will be received in its specific environment (dev, canary, prod, etc.)
     * @param consumerName the name of the consumer listening to the queue
     * @return the environment specific name of the queue
     */
    public String generateEnvironmentSpecificQueueName(String consumerName) {
        return new StringBuilder()
                .append(consumerName)
                .append("-")
                .append(getEnvironment())
                .append("-")
                .append(getServiceName())
                .toString();
    }

    /**
     * Returns the service name. Multiple nodes of this service will share the same name (ex: webui)
     * @return the service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Returns the unique node instance name (ex: webui-1, webui-2, etc.)
     * @return the unique node instance name
     */
    public String getUniqueInstanceName() {
        return uniqueInstanceName;
    }

    /**
     * Returns the environment (ex: dev, canary, prod, etc.)
     * @return the environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * Publishes the event to an exchange in order to fan out the message to all listening consumers
     * @param event the event to publish
     * @throws EventPublishException if the publishing of the event fails
     */
    public void publishEvent(Event event) throws EventPublishException {
        String sourceService = getServiceName();
        String sourceEnvironment = getEnvironment();
        String eventName = event.getName();
        String environmentSpecificEventName = generateEnvironmentSpecificTopicName(eventName);

        MessageBrokerManagerFactory<EventMessageBody> factory = getMessageBrokerManagerFactory();
        MessageBrokerManager<EventMessageBody> manager = factory.getMessageBrokerManagerForTopic(eventName);

        EventMessageProducer producer = new EventMessageProducer(manager);

        producer.publish(
                sourceService,
                sourceEnvironment,
                environmentSpecificEventName,
                event
        );
    }
}
