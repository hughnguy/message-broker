package com.klipfolio.messagebroker.impl.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.SQSActions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.klipfolio.messagebroker.core.Message;
import com.klipfolio.messagebroker.core.MessageConsumer;
import com.klipfolio.messagebroker.core.MessageDeserializer;
import com.klipfolio.messagebroker.core.MessageExchange;
import com.klipfolio.messagebroker.core.MessageQueue;
import com.klipfolio.messagebroker.core.exception.MessageDiscardException;
import com.klipfolio.messagebroker.core.exception.MessageRequeueException;
import com.klipfolio.messagebroker.util.JSONUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AWSMessageQueue<T> implements MessageQueue<T> {

    static private volatile boolean running = true;

    private final Log log = LogFactory.getLog(AWSMessageQueue.class.getSimpleName());

    private static final int RECEIVE_MESSAGE_WAIT_TIME_SECONDS = 1;
    private static final int VISIBILITY_TIMEOUT_SECONDS = 60;
    private static final int LONG_POLLING_WAIT_SECONDS = 20;

    private final AWSClient awsClient;
    private AWSQueueId queueId;
    private String queueName;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                running = false;
            }
        });
    }

    AWSMessageQueue(AWSClient awsClient) {
        this.awsClient = awsClient;
    }

    @Override
    public void registerConsumerToExchange(MessageConsumer<T> consumer, MessageExchange exchange) {
        queueName = generateQueueName(consumer);
        createQueueAndBindToExchange(queueName, exchange);
        addListener(consumer);
    }

    void removeConsumer(MessageConsumer<T> consumer, MessageExchange exchange) {
        queueName = generateQueueName(consumer);
        createQueueAndBindToExchange(queueName, exchange);
        String subscriptionId = queueId.getSubscriptionId();
        awsClient.getSns().unsubscribe(subscriptionId);
    }

    @Override
    public void addListener(final MessageConsumer<T> consumer) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (running) {
                        readQueue(queueId, consumer);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }).start();
    }

    private Message<T> deserializeMessage(MessageConsumer<T> consumer, com.amazonaws.services.sqs.model.Message message) {
        try {
            Map json = JSONUtil.parse(message.getBody());
            String messageBody = (String) json.get("Message");
            MessageDeserializer<T> messageSerializer = consumer.getMessageDeserializer();
            return messageSerializer.deserializeQueueMessage(messageBody);

        } catch(Exception e) {
            log.error("Unable to deserialize message. Discarding message from queue.", e);
            deleteMessage(message.getReceiptHandle());
        }
        return null;
    }

    private void readQueue(AWSQueueId queueId, MessageConsumer<T> consumer) {
        String queueUrl = queueId.getQueueUrl();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withWaitTimeSeconds(LONG_POLLING_WAIT_SECONDS)
                .withMaxNumberOfMessages(consumer.getMaxReceiveMessageCount());

        List<com.amazonaws.services.sqs.model.Message> messages = Collections.emptyList();

        try {
            messages = awsClient.getSqs().receiveMessage(receiveMessageRequest).getMessages();
        } catch (Exception e) {
            log.error("An error occurred while attempting to long poll SQS queue.", e);
        }

        for (com.amazonaws.services.sqs.model.Message message : messages) {
            Message<T> newMessage = deserializeMessage(consumer, message);

            if (newMessage != null) {
                consumeMessage(consumer, newMessage, message.getReceiptHandle());
            }
        }
    }

    private void consumeMessage(MessageConsumer<T> consumer, Message<T> newMessage, String receiptHandle) {
        try {
            consumer.consumeMessage(newMessage);
            deleteMessage(receiptHandle);

        } catch(MessageDiscardException e) {
            log.error("Message corrupt. Discarding message from queue.");
            deleteMessage(receiptHandle);

        } catch(MessageRequeueException e) {
            log.error("Consumer failed to process the message. Message will be re-queued after visibility timeout finishes.");
        }
    }

    private void deleteMessage(String receiptHandle) {
        String queueUrl = queueId.getQueueUrl();
        awsClient.getSqs().deleteMessage(queueUrl, receiptHandle);
    }

    private void createQueueAndBindToExchange(String queueName, MessageExchange exchange) {
        String queueUrl = awsClient
                .getSqs()
                .createQueue(
                        new CreateQueueRequest(queueName)
                        .addAttributesEntry("ReceiveMessageWaitTimeSeconds", String.valueOf(RECEIVE_MESSAGE_WAIT_TIME_SECONDS))
                        .addAttributesEntry("VisibilityTimeout", String.valueOf(VISIBILITY_TIMEOUT_SECONDS))
                )
                .getQueueUrl();

        AWSTopic topic = (AWSTopic) exchange.getTopic();

        String subscriptionId = subscribeQueueToTopic(awsClient.getSns(), awsClient.getSqs(), topic.getTopicArn(), queueUrl);

        queueId = new AWSQueueId(queueUrl, subscriptionId);
    }

    /**
     * Overrides access policy to allow all SNS topics to publish to queue
     */
    private String subscribeQueueToTopic(AmazonSNS sns, AmazonSQS sqs, String snsTopicArn, String sqsQueueUrl) throws AmazonClientException {
        List<String> sqsAttrNames = Arrays.asList(QueueAttributeName.QueueArn.toString(), QueueAttributeName.Policy.toString());

        Map<String, String> sqsAttrs = sqs.getQueueAttributes(sqsQueueUrl, sqsAttrNames).getAttributes();

        String sqsQueueArn = sqsAttrs.get(QueueAttributeName.QueueArn.toString());

        Policy policy = new Policy();

        policy.getStatements().add(new Statement(Statement.Effect.Allow)
                .withId("topic-subscription-" + sqsQueueArn)
                .withPrincipals(Principal.AllUsers)
                .withActions(SQSActions.SendMessage)
                .withResources(new Resource(sqsQueueArn)));

        Map<String, String> newAttrs = new HashMap<>();
        newAttrs.put(QueueAttributeName.Policy.toString(), policy.toJson());
        sqs.setQueueAttributes(new SetQueueAttributesRequest(sqsQueueUrl, newAttrs));

        SubscribeResult subscribeResult = sns.subscribe(snsTopicArn, "sqs", sqsQueueArn);
        return subscribeResult.getSubscriptionArn();
    }
}
