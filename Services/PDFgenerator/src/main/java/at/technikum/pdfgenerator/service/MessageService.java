package at.technikum.pdfgenerator.service;

import at.technikum.pdfgenerator.controller.PDFController;
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

    public void receive() throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "pdf_generator";

        System.out.println(">> PDFGenerator listening to Queue: " + queueName);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(">> PDFGenerator received message: " + message);
            try {
                PDFController.generateInvoice(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

}

