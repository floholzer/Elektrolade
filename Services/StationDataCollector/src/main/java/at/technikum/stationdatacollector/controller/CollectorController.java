package at.technikum.stationdatacollector.controller;

import at.technikum.stationdatacollector.dto.Station;
import at.technikum.stationdatacollector.service.DatabaseService;
import at.technikum.stationdatacollector.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class CollectorController {
    private static final DatabaseService databaseService = new DatabaseService();
    private static final MessageService messageService = new MessageService();
    private static int index = 1;


    // Verarbeitet Nachrichten und sammelt Stationsdaten
    public static void collect(String customerId, String db_url) {
        ArrayList<Station> stations;
        if (db_url.equals("collection ended")) {
            finalizeCollection(customerId);
        } else {
            try {
                stations = databaseService.getStations(customerId, db_url);

                if (stations != null) {
                    for (Station station : stations) {
                        messageService.send(index + " " + station.getKwh(), customerId);
                    }
                    index++;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error while collecting stations." + e.getMessage());
            }
        }
    }

    // Beendet die Sammlung der Stationsdaten
    public static void finalizeCollection(String customerId) {
        index = 1;
        try {
            messageService.send( ">> DataCollector finished", customerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
