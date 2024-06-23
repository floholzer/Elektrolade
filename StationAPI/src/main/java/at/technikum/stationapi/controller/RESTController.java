package at.technikum.stationapi.controller;

import at.technikum.stationapi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;
import java.nio.file.Paths;

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

//    @GetMapping("/{customer_id}")
//    public String getInvoice(@PathVariable int customer_id){
//        if(customer_id < 0 || customer_id == Integer.MAX_VALUE){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
//        return "StationAPI/src/main/java/at/technikum/stationapi/files/invoice-"+customer_id+".pdf";
//    }

    @GetMapping("/{customer_id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String customer_id) {
        try {
            Path filePath = Paths.get("StationAPI/src/main/java/at/technikum/stationapi/files/invoice-"+customer_id+".pdf");
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
