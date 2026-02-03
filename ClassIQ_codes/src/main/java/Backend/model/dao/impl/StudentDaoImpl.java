package Backend.model.dao.impl;

import Backend.config.DatabaseConfig;
import Backend.model.dao.StudentDao;
import Backend.model.entity.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDaoImpl implements StudentDao {

    @Override
    public void saveStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students(student_id, student_name) VALUES (?, ?)";

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, student.getStudentId());
            ps.setString(2, student.getStudentName());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existsById(int studentId) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE student_id = ?";

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}

