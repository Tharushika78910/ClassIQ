package Backend.model.dao.impl;

import Backend.db.DBConnection;
import Backend.model.dao.StudentDao;
import Backend.model.entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    @Override
    public void create(Student s) throws SQLException {
        String sql = """
            INSERT INTO student (student_id, first_name, last_name, student_number, email, user_id)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getStudentId());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getStudentNumber());
            ps.setString(5, s.getEmail());
            ps.setInt(6, s.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public Student findById(int studentId) throws SQLException {
        String sql = """
            SELECT student_id, first_name, last_name, student_number, email, user_id
            FROM student WHERE student_id = ?
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setFirstName(rs.getString("first_name"));
                s.setLastName(rs.getString("last_name"));
                s.setStudentNumber(rs.getString("student_number"));
                s.setEmail(rs.getString("email"));
                s.setUserId(rs.getInt("user_id"));
                return s;
            }
        }
    }

    @Override
    public List<Student> findAll() throws SQLException {
        String sql = """
            SELECT student_id, first_name, last_name, student_number, email, user_id
            FROM student
        """;
        List<Student> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setFirstName(rs.getString("first_name"));
                s.setLastName(rs.getString("last_name"));
                s.setStudentNumber(rs.getString("student_number"));
                s.setEmail(rs.getString("email"));
                s.setUserId(rs.getInt("user_id"));
                list.add(s);
            }
        }
        return list;
    }

    @Override
    public void update(Student s) throws SQLException {
        String sql = """
            UPDATE student
            SET first_name=?, last_name=?, student_number=?, email=?, user_id=?
            WHERE student_id=?
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setString(3, s.getStudentNumber());
            ps.setString(4, s.getEmail());
            ps.setInt(5, s.getUserId());
            ps.setInt(6, s.getStudentId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int studentId) throws SQLException {
        String sql = "DELETE FROM student WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existsById(int studentId) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
