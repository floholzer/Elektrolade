package at.technikum.stationapi.controller;

import at.technikum.stationapi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/invoices")
public class RESTController {
    @Autowired
    private final MessageService messageService = new MessageService();

    //checks if customer_id is valid and sends message to start the data gathering process
    @PostMapping("/{customer_id}")
    public void collectInvoice(@PathVariable("customer_id") int customer_id) {
        if(customer_id < 0 || customer_id == Integer.MAX_VALUE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            messageService.send(String.valueOf(customer_id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{customer_id}")
    public String getInvoice(@PathVariable int customer_id){
        if(customer_id < 0 || customer_id == Integer.MAX_VALUE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return "Invoice Link: link_to_invoice";
    }
}
