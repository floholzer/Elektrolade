package at.technikum.datacollectiondispatcher.service;

import at.technikum.datacollectiondispatcher.controller.DataDispatcherController;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Service
public class MessageService {
    ConnectionFactory factory = new ConnectionFactory();


    public boolean send(String queueName, String message, String customer_id) throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        message = customer_id + "," + message;

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {

            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println(">> Dispatcher sent to Queue: '" + queueName + "', Message: '" + message + "'");
        }
        return true;
    }

    public void receive() throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "start_queue";

        // Create queue if not exists
        channel.queueDeclare(queueName, false, false, false, null);

        System.out.println(">> Dispatcher listening to Queue: " + queueName);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(">> Dispatcher received Message: " + message);
            try {
                DataDispatcherController.sendData(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
