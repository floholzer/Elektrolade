package at.technikum.pdfgenerator;

import at.technikum.pdfgenerator.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdFgeneratorApplication {
    static MessageService messageService = new MessageService();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PdFgeneratorApplication.class, args);
        messageService.receive();
    }

}
