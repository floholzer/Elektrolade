package at.technikum.datacollectionreceiver.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class StationData {
    private String customer_id;
    private ArrayList<Model> stations;

    public StationData(String customer_id, ArrayList<Model> stations) {
        this.customer_id = customer_id;
        this.stations = stations;
    }
}
