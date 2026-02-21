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

    // LOGIN
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

    // VERIFY USERNAME + EMAIL
    public Integer findUserIdByUsernameAndEmail(String username, String email) {

        String sql = """
                SELECT au.user_id
                FROM app_user au
                LEFT JOIN student s ON s.user_id = au.user_id
                LEFT JOIN teacher t ON t.user_id = au.user_id
                WHERE au.user_name = ?
                  AND (s.email = ? OR t.email = ?)
                LIMIT 1
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("user_id");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // UPDATE PASSWORD
    public boolean updatePassword(int userId, String newPassword) {

        String sql = "UPDATE app_user SET password = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}