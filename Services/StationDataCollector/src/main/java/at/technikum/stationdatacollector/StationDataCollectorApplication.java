package at.technikum.stationdatacollector;

import at.technikum.stationdatacollector.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StationDataCollectorApplication {
    static MessageService messageService = new MessageService();


    public static void main(String[] args) throws Exception {
        SpringApplication.run(StationDataCollectorApplication.class, args);
        messageService.receive(); // Startet den Listener zum Empfangen von Nachrichten
    }
}
