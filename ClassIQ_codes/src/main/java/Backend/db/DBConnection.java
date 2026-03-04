package Backend.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }

    public static Connection getConnection() throws SQLException {
        String host = env("DB_HOST", "localhost");
        String port = env("DB_PORT", "3306");
        String name = env("DB_NAME", "classiq");
        String user = env("DB_USER", "root");
        String pass = env("DB_PASS", "root123");

        String url = "jdbc:mariadb://" + host + ":" + port + "/" + name;
        return DriverManager.getConnection(url, user, pass);
    }
}