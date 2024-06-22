package at.technikum.stationdatacollector.Model;

// Modellklasse, die die Informationen einer Station repräsentiert (ID, kWh, Kunden-ID)
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Model {
        @Getter @Setter
        private int id;

        @Getter @Setter
        private float kwh;

        @Getter @Setter
        private int customer_id;
}
