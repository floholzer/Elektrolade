package at.technikum.pdfgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private int id;
    private String first_name;
    private String last_name;

    public String toString() {
        return "Customer(" +
                "id=" + this.getId() + ", " +
                "first_name=" + this.getFirst_name() + ", " +
                "last_name=" + this.getLast_name() + ")";
    }
}
