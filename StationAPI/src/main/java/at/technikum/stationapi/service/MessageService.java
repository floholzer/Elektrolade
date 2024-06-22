package at.technikum.stationapi.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class MessageService {
    private final ConnectionFactory factory = new ConnectionFactory();


    public boolean send(String queueName, String message) throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        try (
                Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
             ) {

            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println(">> Sent to Queue: '" + queueName + "', Message(Customer_id): '" + message + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
