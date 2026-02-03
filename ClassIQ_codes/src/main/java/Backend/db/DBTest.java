package Backend.db;

import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection()) {
            System.out.println(" Database connected successfully");
        } catch (Exception e) {
            System.out.println(" Database connection failed");
            e.printStackTrace();
        }
    }
}
