package at.technikum.datacollectionreceiver;

import at.technikum.datacollectionreceiver.Controller.ReceiverController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataCollectionReceiverApplication implements CommandLineRunner {

    @Autowired
    private ReceiverController receiverController;

    public static void main(String[] args) {
        SpringApplication.run(DataCollectionReceiverApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        receiverController.run();
    }
}
