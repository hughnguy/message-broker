package com.klipfolio.experimental.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RabbitClient {

    private final Logger log = LoggerFactory.getLogger(RabbitClient.class.getSimpleName());

    private static final String HOST = "localhost";
    private static final int PORT = 5672;

    private Connection connection;

    public RabbitClient(ConnectionFactory factory) {
        factory.setHost(HOST);
        factory.setPort(PORT);

        try {
            connection = factory.newConnection();
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Channel createChannel() throws Exception {
        return connection.createChannel();
    }

    public void destroy() throws Exception {
        connection.close();
    }
}
