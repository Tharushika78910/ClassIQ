package Backend.model.dao.impl;

import Backend.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserProfileDaoImpl {

    public static class StudentProfile {
        public final int studentId;
        public final String name;
        public final String email;

        public StudentProfile(int studentId, String name, String email) {
            this.studentId = studentId;
            this.name = name;
            this.email = email;
        }
    }

    public static class TeacherProfile {
        public final int teacherId;
        public final String name;
        public final String email;

        public TeacherProfile(int teacherId, String name, String email) {
            this.teacherId = teacherId;
            this.name = name;
            this.email = email;
        }
    }

    public StudentProfile findStudentByUserId(int userId) {
        String sql = """
                SELECT student_id, first_name, last_name, email
                FROM student
                WHERE user_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                return new StudentProfile(rs.getInt("student_id"), name, rs.getString("email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public TeacherProfile findTeacherByUserId(int userId) {
        String sql = """
                SELECT teacher_id, first_name, last_name, email
                FROM teacher
                WHERE user_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                return new TeacherProfile(rs.getInt("teacher_id"), name, rs.getString("email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
