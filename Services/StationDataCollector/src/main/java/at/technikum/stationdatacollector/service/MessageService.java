package at.technikum.stationdatacollector.service;

import at.technikum.stationdatacollector.controller.CollectorController;
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
    private  CollectorController collectorController;


    // Sendet eine Nachricht an eine bestimmte Zieladresse
    public boolean send(String message, String customer_id) throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        message = "customer_id: " + customer_id + ", msg: " + message;
        String queueName = "data_receiver";
        try (
                Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println(">> Sent to Queue:'" + queueName + "', Message:'" + message + "'");
        }
        return true;
    }

    // Hört auf Nachrichten und ruft bei Erhalt die collect-Methode im CollectorController auf
    public void receive() throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "data_collector";

        System.out.println(">> DataCollector listening to Queue:'" + queueName + "'");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] message_info = message.split(",");
            System.out.println(">> DataCollector: Received Message:'" + message + "'");
            try {
                CollectorController.collect(message_info[0], message_info[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
