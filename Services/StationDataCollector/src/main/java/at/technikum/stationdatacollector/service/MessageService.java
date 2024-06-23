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

    // Sendet eine Nachricht an eine bestimmte Zieladresse über RabbitMQ
    public boolean send(int stationId, String kwh, String customer_id) throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        String message = customer_id + ","+ stationId + "," + kwh;
        String queueName = "station_data_queue";

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println(">> DataCollector sent to Queue:'" + queueName + "', Message:'" + message + "'");
        }
        return true;
    }
    // Methode zum Senden einer Endnachricht
    public boolean sendEnd() throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        String message = "data end";
        String queueName = "station_data_queue";

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println(">> DataCollector sent to Queue:'" + queueName + "', Message:'" + message + "'");
        }
        return true;
    }

    // Hört auf Nachrichten und ruft bei Erhalt die collect-Methode im CollectorController auf
    public void receive() throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "stations_info_queue";

        // Create queue if not exists
        channel.queueDeclare(queueName, false, false, false, null);

        System.out.println(">> DataCollector listening to Queue:'" + queueName + "'");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] message_info = message.split(",");
            System.out.println(">> DataCollector received Message:'" + message + "'");
            try {
                CollectorController.collect(message_info[0], message_info[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
