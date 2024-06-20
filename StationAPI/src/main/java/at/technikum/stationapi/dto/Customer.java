package at.technikum.stationapi.dto;

public class Customer {
    private int customer_id;
    private String first_name;
    private String last_name;

    public Customer(int customer_id, String first_name, String last_name) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
