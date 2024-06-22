package at.technikum.datacollectionreceiver.Service;

import at.technikum.datacollectionreceiver.Controller.ReceiverController;
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
    private ConnectionFactory factory = new ConnectionFactory();

    public void setConnectionFactory(ConnectionFactory factory) {
        this.factory = factory;
    }

    public boolean sendMessage(String to, String message) throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare("spring_app", "direct");

            channel.basicPublish("spring_app", to, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + to + "':'" + message + "'");
        }
        return true;
    }

    public void listen(String[] argv, ReceiverController receiverController) throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("spring_app", "direct");

        String queueName = channel.queueDeclare().getQueue();
        for (String bindingKey : argv) {
            channel.queueBind(queueName, "spring_app", bindingKey);
        }

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] message_info = message.split(" ");
            System.out.println(" [x] Received '" + message + "'");
            try {
                receiverController.collect(message_info);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        System.out.println(" [x] Collection receiver listening to  '" + queueName + "'");
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
