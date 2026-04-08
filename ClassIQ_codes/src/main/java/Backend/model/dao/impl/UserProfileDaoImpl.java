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
        public final String subject;

        public TeacherProfile(int teacherId, String name, String email, String subject) { // ✅ UPDATE
            this.teacherId = teacherId;
            this.name = name;
            this.email = email;
            this.subject = subject;
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
        return findTeacherByUserId(userId, null);
    }

    public TeacherProfile findTeacherByUserId(int userId, String languageCode) {
        String sql;
        boolean useTranslation = languageCode != null && !languageCode.equals("en");
        
        if (useTranslation) {
            sql = """
                    SELECT t.teacher_id, 
                           COALESCE(tt.first_name, t.first_name) as first_name,
                           COALESCE(tt.last_name, t.last_name) as last_name,
                           t.email, t.subject
                    FROM teacher t
                    LEFT JOIN teacher_translation tt ON t.teacher_id = tt.teacher_id AND tt.language_code = ?
                    WHERE t.user_id = ?
                    """;
        } else {
            sql = """
                    SELECT teacher_id, first_name, last_name, email, subject
                    FROM teacher
                    WHERE user_id = ?
                    """;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (useTranslation) {
                ps.setString(1, languageCode);
                ps.setInt(2, userId);
            } else {
                ps.setInt(1, userId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                // Add explicit variable assignments
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String name = firstName + " " + lastName;
                String email = rs.getString("email");
                String subject = rs.getString("subject");
                int teacherId = rs.getInt("teacher_id");
 
                // Simplify constructor call
                return new TeacherProfile(teacherId, name, email, subject);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
