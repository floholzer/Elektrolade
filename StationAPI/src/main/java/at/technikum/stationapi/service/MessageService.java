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


    public boolean send(String message) throws IOException, TimeoutException {
        factory.setHost("localhost");
        factory.setPort(30003);
        String queueName = "start_queue";
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {

            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println(">> StationAPI sent to Queue: '" + queueName + "', Message: '" + message + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
