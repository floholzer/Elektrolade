package at.technikum.datacollectionreceiver.dto;

import lombok.*;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationData {
    private String customer_id;
    private ArrayList<Station> stations;

    @Override
    public String toString() {
        return "StationData{" +
                "customer_id='" + customer_id + '\'' +
                ", stations=" + stations +
                '}';
    }
}
