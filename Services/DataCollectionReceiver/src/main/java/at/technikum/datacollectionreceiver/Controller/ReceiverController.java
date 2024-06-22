package at.technikum.datacollectionreceiver.Controller;

import at.technikum.datacollectionreceiver.Model.Model;
import at.technikum.datacollectionreceiver.Model.StationData;
import at.technikum.datacollectionreceiver.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

@Controller
public class ReceiverController {
    private StationData stationData;
    private final MessageService messageService;

    @Autowired
    public ReceiverController(MessageService messageService) {
        this.messageService = messageService;
    }

    public void run() throws IOException, TimeoutException {
        String[] subscribe = {"collection_receiver"};
        messageService.listen(subscribe, this);
    }

    public void collect(String[] message) throws Exception {
        if (message[1].equals("start")) {
            stationData = new StationData(message[0], new ArrayList<>());
        } else if (message[1].equals("finished")) {
            messageService.sendMessage("pdf_service", "start " + stationData.getCustomer_id());
            for (Model station : stationData.getStations()) {
                messageService.sendMessage("pdf_service", station.getId() + " " + station.getKwh());
            }
            messageService.sendMessage("pdf_service", "end " + stationData.getCustomer_id());
        } else {
            Model newStation = new Model(message[1], message[2]);
            stationData.getStations().add(newStation);
        }
    }

    public StationData getStationData() {
        return stationData;
    }
}
