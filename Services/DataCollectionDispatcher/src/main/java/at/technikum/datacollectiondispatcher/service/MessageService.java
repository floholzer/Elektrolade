package at.technikum.datacollectiondispatcher.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
public class MessageService {
    ConnectionFactory factory = new ConnectionFactory();


    public boolean send(String queueName, String message, String customer_id) throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        message = customer_id + " -> " + message;
        UUID uuid = UUID.randomUUID();

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {

            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println(" [#"+ uuid + "] Sent to '" + queueName + "', Message:'" + message + "'");
        }
        return true;
    }

    public void reveive(String[] argv) throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("spring_app", "direct");
        String queueName = channel.queueDeclare().getQueue();

        System.out.println(" [x] Dispatcher listening to  '" + queueName + "'");
        for (String bindingKey : argv) {
            channel.queueBind(queueName, "spring_app", bindingKey);
        }

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
            try {
                //DistpatchingController.dispatch(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
