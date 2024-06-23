package at.technikum.stationdatacollector.controller;

import at.technikum.stationdatacollector.dto.Station;
import at.technikum.stationdatacollector.service.DatabaseService;
import at.technikum.stationdatacollector.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class CollectorController {
    // Instanziieren von DatabaseService und MessageService
    private static DatabaseService databaseService = new DatabaseService();
    private static MessageService messageService = new MessageService();
    private static int index = 1; // Zähler für die Nachrichten

    @Autowired
    public CollectorController(DatabaseService databaseService, MessageService messageService) {
        this.databaseService = databaseService;
        this.messageService = messageService;
    }

    // Methode, um Nachrichten zu verarbeiten und Stationsdaten zu sammeln
    public static void collect(String customerId, String db_url) {
        ArrayList<Station> stations;
        if (db_url.equals("collection ended")) {
    // Wenn die Nachricht "collection ended" ist, wird das Ende der Datensammlung signalisiert
            try {
                messageService.sendEnd(); // Senden einer Endnachricht
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            index = 0; // Zurücksetzen des Zählers
        } else {
            try {
                // Abrufen der Stationsdaten von der Datenbank
                stations = databaseService.getStations(customerId, db_url);

                if (stations != null) {
                    // Senden der Stationsdaten über MessageService
                    for (Station station : stations) {
                        messageService.send(index, String.valueOf(station.getKwh()), customerId);
                    }

                }
                index++; // Erhöhen des Zählers
            } catch (Exception e) {
                throw new RuntimeException("Error while collecting stations." + e.getMessage());
            }
        }
    }
}
