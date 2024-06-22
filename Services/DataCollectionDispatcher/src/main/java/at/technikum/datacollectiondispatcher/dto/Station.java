package at.technikum.datacollectiondispatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    private int id;
    private String db_url;
    private float lat;
    private float lng;

    public String toString() {
        return "Station(" +
                "id=" + this.getId() + ", " +
                "db_url=" + this.getDb_url() + ", " +
                "lat=" + this.getLat() + ", " +
                "lng=" + this.getLng() + ")";
    }
}
