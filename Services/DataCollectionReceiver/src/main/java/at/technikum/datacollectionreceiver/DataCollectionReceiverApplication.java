package at.technikum.datacollectionreceiver;

import at.technikum.datacollectionreceiver.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataCollectionReceiverApplication {
    private static final MessageService messageService = new MessageService();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DataCollectionReceiverApplication.class, args);
        messageService.DispatcherListener();
        messageService.DataCollectorListener();
    }
}
