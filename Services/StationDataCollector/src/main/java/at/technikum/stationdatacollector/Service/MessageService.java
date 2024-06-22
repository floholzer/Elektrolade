package at.technikum.stationdatacollector.Service;

import at.technikum.stationdatacollector.Controller.CollectorController;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

// Verwaltet das Senden und Empfangen von Nachrichten über RabbitMQ
@Service
public class MessageService {

    private ConnectionFactory factory = new ConnectionFactory();

    // Sendet eine Nachricht an eine bestimmte Zieladresse
    public boolean sendMessage(String to, String message, String customerId) throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        message = customerId + " " + message;
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare("spring_app", "direct");

            channel.basicPublish("spring_app", to, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + to + "':'" + message + "'");
        }
        return true;
    }

    // Hört auf Nachrichten und ruft bei Erhalt die collect-Methode im CollectorController auf
    public void listen(String[] argv, CollectorController collectorController) throws IOException, TimeoutException {
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
                collectorController.collect(message_info[0], message_info[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        System.out.println(" [x] Station Data Collector listening to  '" + queueName + "'");
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
