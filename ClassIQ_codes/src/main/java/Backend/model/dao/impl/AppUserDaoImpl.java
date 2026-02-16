package Backend.model.dao.impl;

import Backend.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AppUserDaoImpl {

    public static class LoginResult {
        public final int userId;
        public final String username;
        public final String role;

        public LoginResult(int userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }
    }

    public LoginResult login(String username, String password) {
        String sql = """
                SELECT user_id, user_name, role
                FROM app_user
                WHERE user_name = ? AND password = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new LoginResult(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("role")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
