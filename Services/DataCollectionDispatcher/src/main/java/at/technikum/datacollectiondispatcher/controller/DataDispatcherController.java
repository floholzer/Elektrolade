package at.technikum.datacollectiondispatcher.controller;

import at.technikum.datacollectiondispatcher.dto.Station;
import at.technikum.datacollectiondispatcher.service.DataService;
import at.technikum.datacollectiondispatcher.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class DataDispatcherController {

    private static final MessageService messageService = new MessageService();
    private static final DataService dataService = new DataService();


    public static void sendData(String customerId) throws Exception {
        ArrayList<Station> stations = dataService.getStations();

        messageService.send("job_notify_queue", "collection started", customerId);

        for (Station station : stations) {
            try {
                messageService.send("stations_info_queue", station.getDb_url(), customerId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        messageService.send("stations_info_queue", "collection ended", customerId);
    }

}
