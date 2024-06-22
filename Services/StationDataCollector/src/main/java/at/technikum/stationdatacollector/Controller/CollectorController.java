package at.technikum.stationdatacollector.Controller;

import at.technikum.stationdatacollector.Model.Model;
import at.technikum.stationdatacollector.Service.DatabaseService;
import at.technikum.stationdatacollector.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

@Controller
public class CollectorController {
    private final DatabaseService databaseService;
    private final MessageService messageService;
    private int index = 1;

    @Autowired
    public CollectorController(DatabaseService databaseService, MessageService messageService) {
        this.databaseService = databaseService;
        this.messageService = messageService;
    }

    // Startet den Listener für Nachrichten
    public void run() throws IOException, TimeoutException {
        String[] subscribe = {"data_collector"};
        messageService.listen(subscribe, this);
    }

    // Verarbeitet Nachrichten und sammelt Stationsdaten
    public void collect(String customerId, String message) {
        if ("end".equals(message)) {
            finalizeCollection(customerId);
        } else {
            try {
                String dbUrl = getDatabaseUrlForStation(message);
                ArrayList<Model> stations = databaseService.getStations(customerId, dbUrl);
                if (stations != null) {
                    for (Model station : stations) {
                        messageService.sendMessage("collection_receiver", index + " " + station.getKwh(), customerId);
                    }
                    index++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Beendet die Sammlung der Stationsdaten
    public void finalizeCollection(String customerId) {
        index = 1;
        try {
            messageService.sendMessage("collection_receiver", "finished", customerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gibt die passende Datenbank-URL basierend auf der Nachricht zurück
    private String getDatabaseUrlForStation(String message) {
        switch (message) {
            case "station1":
                return "jdbc:postgresql://localhost:30011/station1db";
            case "station2":
                return "jdbc:postgresql://localhost:30012/station2db";
            case "station3":
                return "jdbc:postgresql://localhost:30013/station3db";
            default:
                return "jdbc:postgresql://localhost:30002/stationdb";
        }
    }
}
