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
    private static final Map<String, ArrayList<String>> stationDataMap = new HashMap<>();
    private static final MessageService messageService = new MessageService();
    private static String customerId;

    public static void collect(String[] message) throws Exception {

        if (message[0].equals("data end")) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, ArrayList<String>> entry : stationDataMap.entrySet()) {
                int stationId = Integer.parseInt(entry.getKey())+1;
                ArrayList<String> kwhList = entry.getValue();
                String kwhListString = String.join(",", kwhList);
                sb.append(customerId).append(";").append(stationId).append(";").append(kwhListString).append("|");
            }
            sb.deleteCharAt(sb.length() - 1); // Remove the last "|"
            messageService.send(sb.toString());
            stationDataMap.clear();
        } else {
            customerId = message[0];
            String stationId = message[1];
            String kwh = message[2];
            stationDataMap.computeIfAbsent(stationId, k -> new ArrayList<>()).add(kwh);
        }
    }
}
