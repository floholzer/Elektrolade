package at.technikum.stationapi.controller;

import at.technikum.stationapi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class RESTController {
    @Autowired
    private static MessageService messageService = new MessageService();

    //gets file from storage
    @GetMapping("/invoices/{id}")
    public String getInvoice(@PathVariable int id){
        return "Invoice is retreived, please wait until download";
    }


    //sends message to get invoice process started
    @GetMapping("/invoice/{customer_id}")
    public void collectInvoice(@PathVariable int customer_id) {
        if(customer_id == Integer.MIN_VALUE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            //messageService.sendMessage("spring_app", ""+customer_id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
