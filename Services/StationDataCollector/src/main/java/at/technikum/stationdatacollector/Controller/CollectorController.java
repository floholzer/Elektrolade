package at.technikum.stationdatacollector.Controller;

import at.technikum.stationdatacollector.Model.Model;
import at.technikum.stationdatacollector.Service.DatabaseService;
import at.technikum.stationdatacollector.Service.MessageService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

//Enthält die Hauptlogik zur Verarbeitung von Nachrichten und zur Sammlung der Stationsdaten.
public class CollectorController {
    private static DatabaseService databaseService = new DatabaseService();
    private static MessageService messageService = new MessageService();
    private static int index = 1;

    //Startet den Listener für die Nachrichtenverarbeitung
//    run-Methode initialisiert das Nachrichtensystem und beginnt mit dem Abhören von Nachrichten.
    public static void run() throws IOException, TimeoutException {
        String[] subscribe = new String[1];
        subscribe[0] = "data_collector";
        messageService.listen(subscribe);
    }

    //Verarbeitet die Nachrichten und sammelt die Stationsdaten
//    collect-Methode verarbeitet die empfangenen Nachrichten und ruft die Daten der Stationen ab.
    public static void collect(String customer_id ,String message) throws SQLException {
        ArrayList<Model> stations = null;
        if(message.equals("end")) finalize(customer_id);
        else stations =  databaseService.getStations(customer_id, message);
        if(stations != null) {
            stations.forEach(station -> {
                try {
                    messageService.sendMessage("collection_receiver", index +  " " + station.getKwh(), customer_id);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            index++;
        }
    };
    //Beendet die Sammlung der Stationsdaten
//    finalize-Methode sendet eine Abschlussnachricht, wenn der Sammelprozess abgeschlossen ist.
    public static void finalize(String customer_id) {
        index = 1;
        try {
            messageService.sendMessage("collection_receiver", "finished", customer_id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
