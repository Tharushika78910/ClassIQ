package Backend.model.dao.impl;

import Backend.db.DBConnection;
import Backend.model.dao.StudentDao;
import Backend.model.entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    // CREATE
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

    // FIND BY ID  FIXED
    @Override
    public Student findById(int studentId) throws SQLException {

        String sql = """
            SELECT student_id, first_name, last_name, student_number, email, user_id
            FROM student
            WHERE student_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapRow(rs);
            }
        }
    }

    // FIND ALL
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
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // UPDATE
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

    // DELETE
    @Override
    public void delete(int studentId) throws SQLException {

        String sql = "DELETE FROM student WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    // EXISTS
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

    // FIND BY STUDENT NUMBER
    @Override
    public Student findByStudentNumber(String studentNumber) throws SQLException {

        String sql = """
            SELECT student_id, first_name, last_name, student_number, email, user_id
            FROM student
            WHERE student_number = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapRow(rs);
            }
        }
    }

    // REQUIRED by StudentDetailsController
    @Override
    public int findStudentIdByStudentNumber(String studentNumber) throws SQLException {

        String sql = "SELECT student_id FROM student WHERE student_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Student not found: " + studentNumber);
                }
                return rs.getInt("student_id");
            }
        }
    }

    // FIND FIRST STUDENT
    @Override
    public Student findFirstStudent() throws SQLException {

        String sql = """
            SELECT student_id, first_name, last_name, student_number, email, user_id
            FROM student
            ORDER BY student_id ASC
            LIMIT 1
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return null;
            return mapRow(rs);
        }
    }

    // FIND ALL BASIC
    @Override
    public List<Student> findAllBasic() throws SQLException {

        String sql = """
            SELECT student_id, first_name, last_name, student_number
            FROM student
            ORDER BY student_number
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
                list.add(s);
            }
        }

        return list;
    }

    // HELPER
    private Student mapRow(ResultSet rs) throws SQLException {
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
