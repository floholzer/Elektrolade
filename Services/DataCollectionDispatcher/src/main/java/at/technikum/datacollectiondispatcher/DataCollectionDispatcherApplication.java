package at.technikum.datacollectiondispatcher;

import at.technikum.datacollectiondispatcher.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataCollectionDispatcherApplication {
    private static final MessageService messageService = new MessageService();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DataCollectionDispatcherApplication.class, args);
        messageService.receive();
    }

}
