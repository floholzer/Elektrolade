package at.technikum.stationapi.dto;

public class Station {

    private int id;
    private float kwh;
    private String customer_id;

    public Station(int id, float kwh, String customer_id) {
        this.id = id;
        this.kwh = kwh;
        this.customer_id = customer_id;
    }
}
