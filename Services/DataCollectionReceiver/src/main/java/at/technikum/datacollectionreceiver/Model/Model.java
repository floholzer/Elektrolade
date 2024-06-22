package at.technikum.datacollectionreceiver.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Model {
    private String id;
    private String kwh;

    public Model(String id, String kwh) {
        this.id = id;
        this.kwh = kwh;
    }
}
