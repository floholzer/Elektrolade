package at.technikum.datacollectionreceiver.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    private String id;
    private String kwh;

    @Override
    public String toString() {
        return "Station{" +
                "id='" + id + '\'' +
                ", kwh='" + kwh + '\'' +
                '}';
    }
}
