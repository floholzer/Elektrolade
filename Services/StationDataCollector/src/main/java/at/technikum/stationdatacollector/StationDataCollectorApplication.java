package at.technikum.stationdatacollector;

import at.technikum.stationdatacollector.Controller.CollectorController;
import at.technikum.stationdatacollector.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StationDataCollectorApplication implements CommandLineRunner {

    @Autowired
    private CollectorController collectorController;

    @Autowired
    private MessageService messageService;

    public static void main(String[] args) {
        SpringApplication.run(StationDataCollectorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        collectorController.run();
    }
}
