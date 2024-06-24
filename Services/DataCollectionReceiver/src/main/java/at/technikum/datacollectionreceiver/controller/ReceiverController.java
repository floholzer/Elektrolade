package at.technikum.datacollectionreceiver.controller;

import at.technikum.datacollectionreceiver.dto.Station;
import at.technikum.datacollectionreceiver.dto.StationData;
import at.technikum.datacollectionreceiver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Controller
public class ReceiverController {
    // Speichert die Stationsdaten
    static final Map<String, ArrayList<String>> stationDataMap = new HashMap<>();
    static MessageService messageService = new MessageService();
    static String customerId;

    // Methode, um empfangene Nachrichten zu verarbeiten und Stationsdaten zu sammeln
    public static void collect(String[] message) throws Exception {

        if (message[0].equals("data end")) {
            // Wenn die Nachricht "data end" ist, aggregiere die Daten und sende sie an den PDF Generator
            StringBuilder sb = new StringBuilder();
            // Iteriere über die Map und sende die Daten an den PDF Generator
            for (Map.Entry<String, ArrayList<String>> entry : stationDataMap.entrySet()) {
                int stationId = Integer.parseInt(entry.getKey());
                ArrayList<String> kwhList = entry.getValue();
                String kwhListString = String.join(",", kwhList); // kwH-Liste in einen String umwandeln
                sb.append(customerId).append(";").append(stationId).append(";").append(kwhListString).append("|"); // Zusammenfügen der Daten
            }
            sb.deleteCharAt(sb.length() - 1); // Entfernen des letzten Zeichens "|"
            messageService.send(sb.toString());
            stationDataMap.clear(); // Leeren der Datamap
        } else {
            // Speichern der empfangenen Daten in der Map
            customerId = message[0]; // Kunden-ID speichern
            String stationId = message[1]; // Station-ID speichern
            String kwh = message[2]; // kwh speichern
            stationDataMap.computeIfAbsent(stationId, k -> new ArrayList<>()).add(kwh); // Hinzufügen der kwh zur Station
        }
    }
}
