package at.technikum.datacollectionreceiver.controller;

import at.technikum.datacollectionreceiver.dto.Station;
import at.technikum.datacollectionreceiver.dto.StationData;
import at.technikum.datacollectionreceiver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

@Controller
public class ReceiverController {
    private static StationData stationData;
    private static final MessageService messageService = new MessageService();


    public static void collect(String[] message) throws Exception {
        if (message[1].equals("start")) {
            stationData = new StationData(message[0], new ArrayList<>());

        } else if (message[1].equals("finished")) {
            messageService.send("start " + stationData.getCustomer_id());
            for (Station station : stationData.getStations()) {
                messageService.send(station.getId() + " " + station.getKwh());
            }
            messageService.send("end " + stationData.getCustomer_id());
        } else {
            Station newStation = new Station(message[1], message[2]);
            stationData.getStations().add(newStation);
        }
    }
}
