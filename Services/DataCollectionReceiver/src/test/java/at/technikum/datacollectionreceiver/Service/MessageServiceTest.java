package at.technikum.datacollectionreceiver.Service;

import at.technikum.datacollectionreceiver.Controller.ReceiverController;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.CancelCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private Connection connection;

    @Mock
    private Channel channel;

    @Mock
    private DeclareOk declareOk;

    private MessageService messageService;

    @BeforeEach
    void setUp() throws IOException, TimeoutException {
        MockitoAnnotations.openMocks(this);
        when(connectionFactory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);
        when(channel.queueDeclare()).thenReturn(declareOk);
        when(declareOk.getQueue()).thenReturn("queueName");
        messageService = new MessageService();
        messageService.setConnectionFactory(connectionFactory);
    }

    @Test
    void testSendMessage() throws Exception {
        doNothing().when(channel).exchangeDeclare(anyString(), anyString());
        doNothing().when(channel).basicPublish(anyString(), anyString(), any(), any());

        messageService.sendMessage("pdf_service", "test message");

        verify(channel, times(1)).exchangeDeclare("spring_app", "direct");
        verify(channel, times(1)).basicPublish("spring_app", "pdf_service", null, "test message".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testListen() throws IOException, TimeoutException {
        doNothing().when(channel).exchangeDeclare(anyString(), anyString());
        doNothing().when(channel).queueBind(anyString(), anyString(), anyString());
        doNothing().when(channel).basicConsume(anyString(), anyBoolean(), (DeliverCallback) any(), any(CancelCallback.class));

        ReceiverController receiverController = mock(ReceiverController.class);
        String[] subscribe = {"collection_receiver"};
        messageService.listen(subscribe, receiverController);

        verify(channel, times(1)).exchangeDeclare("spring_app", "direct");
        verify(channel, times(1)).queueDeclare();
        verify(channel, times(1)).queueBind("queueName", "spring_app", "collection_receiver");

        ArgumentCaptor<DeliverCallback> deliverCallbackCaptor = ArgumentCaptor.forClass(DeliverCallback.class);
        verify(channel, times(1)).basicConsume(eq("queueName"), eq(true), deliverCallbackCaptor.capture(), any(CancelCallback.class));

        DeliverCallback deliverCallback = deliverCallbackCaptor.getValue();
        assert deliverCallback != null;

        // Simuliere eine Nachricht
        String message = "1 start";
        deliverCallback.handle("consumerTag", new com.rabbitmq.client.Delivery(null, null, message.getBytes(StandardCharsets.UTF_8)));

        try {
            verify(receiverController, times(1)).collect(new String[]{"1", "start"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
