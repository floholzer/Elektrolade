package at.technikum.pdfgenerator.service;

import at.technikum.pdfgenerator.dto.Customer;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DataService {
    private static Connection connect() throws SQLException {
        String connectionString="jdbc:postgresql://localhost:30001/customerdb";
        return DriverManager.getConnection(connectionString, "postgres", "postgres");
    }

    public static Customer getCustomer(int id) throws Exception {
        Customer customer; // create customer object

        try (Connection conn = connect()) {
            String query = "SELECT id, first_name, last_name FROM customer WHERE id = " + id + ";";
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet resultSet = stmt.executeQuery(query);

            if(resultSet.next()) {
                customer = new Customer(
                        resultSet.getInt(1),    // id
                        resultSet.getString(2), // first_name
                        resultSet.getString(3)  // last_name
                );
            } else {
                throw new Exception("No customer found for given ID in DB");
            }
        } catch (SQLException e) {
            throw new Exception("Error while fetching customer from DB" + e.getMessage());
        }
        return customer;
    }
}
