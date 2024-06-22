package at.technikum.stationdatacollector;

import at.technikum.stationdatacollector.Controller.CollectorController;
import at.technikum.stationdatacollector.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StationDataCollectorApplication {
    static MessageService messageService = new MessageService();


    public static void main(String[] args) throws Exception {
        SpringApplication.run(StationDataCollectorApplication.class, args);
        messageService.receive();
    }
}
