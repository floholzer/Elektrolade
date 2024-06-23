package at.technikum.stationdatacollector.dto;

// Modellklasse, die die Informationen einer Station repräsentiert (ID, kWh, Kunden-ID)
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {
        private int id;
        private float kwh;
        private int customer_id;

        // Überschreiben der toString-Methode für lesbare Ausgabe
        public String toString() {
                return "Station{" +
                        "id=" + id +
                        ", kwh=" + kwh +
                        ", customer_id=" + customer_id +
                        '}';
        }
}
