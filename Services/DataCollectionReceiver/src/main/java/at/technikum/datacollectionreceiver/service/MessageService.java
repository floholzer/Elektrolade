package at.technikum.datacollectionreceiver.service;

import at.technikum.datacollectionreceiver.controller.ReceiverController;
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
    private final ConnectionFactory factory = new ConnectionFactory();

    public boolean send(String message) throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        String queueName = "pdf_generator";
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(">> CollectionReceiver sent to Queue:'" + queueName + "', Message:'" + message + "'");
        }
        return true;
    }

    public void DispatcherListener() throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "job_notify_queue";

        System.out.println(">> CollectionReceiver listening to Queue: '" + queueName + "'");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(">> CollectionReceiver received Message: '" + message + "'");
            String[] messages = message.split(",");
            if (messages[1].equals("collection started")) {
                System.out.println("Job started notification received");
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public void DataCollectorListener() throws Exception {
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "station_data_queue";

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(">> DataReceiver received Message: '" + message + "'");

            String[] messages = message.split(",");

            try {
                ReceiverController.collect(messages);
            } catch (Exception e) {
                throw new RuntimeException("Error while collecting stations." + e.getMessage());
            }
        };

        System.out.println(">> CollectionReceiver listening to Queue: '" + queueName + "'");
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
