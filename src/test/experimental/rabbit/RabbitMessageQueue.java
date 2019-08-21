package com.klipfolio.experimental.rabbit;

import com.klipfolio.messagebroker.core.*;
import com.klipfolio.messagebroker.core.exception.ConsumerProcessingException;
import com.klipfolio.messagebroker.core.exception.MessageCorruptException;
import com.klipfolio.messagebroker.core.exception.MessageQueueException;
import com.rabbitmq.client.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public abstract class RabbitMessageQueue<T> implements MessageQueue<T> {

    private final Logger log = LoggerFactory.getLogger(RabbitMessageQueue.class.getSimpleName());

    private final RabbitClient rabbitClient;
    private final Channel channel;
    private String queueName;

    public RabbitMessageQueue(RabbitClient rabbitClient) throws Exception {
        this.rabbitClient = rabbitClient;
        this.channel = rabbitClient.createChannel();
    }

    @Override
    public void registerConsumerToExchange(MessageConsumer<T> consumer, MessageExchange exchange) throws MessageQueueException {
        this.queueName = generateQueueName(consumer);
        try {
            createQueueAndBindToExchange(this.queueName, exchange);
        } catch(IOException e) {
            throw new MessageQueueException("Failed to create queue and bind it to exchange.", e);
        }
        addListener(consumer);
    }

    public void removeConsumer(MessageConsumer<T> consumer, MessageExchange exchange) {
        // TODO: Rabbitmq remove consumer from queue
    }

    @Override
    public void addListener(MessageConsumer<T> consumer) throws MessageQueueException {
        try {
            channel.basicConsume(queueName, false, readQueue(consumer));
        } catch(IOException e) {
            throw new MessageQueueException("Failed to add listener to queue.", e);
        }
    }

    protected void createQueueAndBindToExchange(String queueName, MessageExchange exchange) throws IOException {
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchange.getTopic().getName(), "");
    }

    private Consumer readQueue(final MessageConsumer<T> consumer) {
        return new Consumer() {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                long deliveryTag = envelope.getDeliveryTag();
                final String message = new String(body, "UTF-8");

                Message<T> newMessage = deserializeMessage(consumer, message, deliveryTag);

                if (newMessage != null) {
                    consumeMessage(consumer, newMessage, deliveryTag);
                }
            }
            @Override
            public void handleConsumeOk(String consumerTag) {}
            @Override
            public void handleCancelOk(String consumerTag) {}
            @Override
            public void handleCancel(String consumerTag) {}
            @Override
            public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {}
            @Override
            public void handleRecoverOk(String consumerTag) {}
        };
    }

    private Message<T> deserializeMessage(MessageConsumer<T> consumer, String message, long deliveryTag) {
        try {
            MessageDeserializer<T> messageSerializer = consumer.getMessageDeserializer();
            return messageSerializer.deserializeQueueMessage(message);

        } catch(IOException e) {
            log.error(e.getMessage(), e);
            rejectMessageAndDiscard(deliveryTag);
        }
        return null;
    }

    private void consumeMessage(MessageConsumer<T> consumer, Message<T> newMessage, long deliveryTag) {
        try {
            consumer.consumeMessage(newMessage);
            acknowledgeMessageAndDiscard(deliveryTag);

        } catch(MessageCorruptException e) {
            log.error("Message corrupt. Discarding message from queue.");
            rejectMessageAndDiscard(deliveryTag);

        } catch(ConsumerProcessingException e) {
            log.error("Consumer failed to process the message. Adding message back to queue for re-attempt.");
            rejectMessageAndRequeue(deliveryTag);
        }
    }

    public void rejectMessageAndRequeue(Object deliveryTag) {
        try {
            channel.basicReject((Long) deliveryTag, true);
        } catch(IOException e) {
            log.error("Failed to reject and re-queue message.", e);
        }
    }

    public void rejectMessageAndDiscard(Object deliveryTag) {
        try {
            channel.basicReject((Long) deliveryTag, false);
        } catch(IOException e) {
            log.error("Failed to reject and discard message.", e);
        }
    }

    public void acknowledgeMessageAndDiscard(Object deliveryTag) {
        try {
            channel.basicAck((Long) deliveryTag, false);
        } catch(IOException e) {
            log.error("Failed to acknowledge and discard message.", e);
        }
    }
}
