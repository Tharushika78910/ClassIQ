package backend.model.dao.impl;

import backend.db.DBConnection;
import backend.model.dao.StudentDao;
import backend.model.entity.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    private static final String DEFAULT_LANGUAGE = "en";

    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_STUDENT_NUMBER = "student_number";
    private static final String COL_EMAIL = "email";
    private static final String COL_USER_ID = "user_id";

    private static final String SQL_CREATE = """
        INSERT INTO student (student_id, first_name, last_name, student_number, email, user_id)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

    private static final String SQL_FIND_BY_ID = """
        SELECT
            s.student_id,
            COALESCE(tr_req.first_name, tr_en.first_name, s.first_name) AS first_name,
            COALESCE(tr_req.last_name, tr_en.last_name, s.last_name) AS last_name,
            s.student_number,
            s.email,
            s.user_id
        FROM student s
        LEFT JOIN student_translation tr_req
            ON s.student_id = tr_req.student_id
           AND tr_req.language_code = ?
        LEFT JOIN student_translation tr_en
            ON s.student_id = tr_en.student_id
           AND tr_en.language_code = 'en'
        WHERE s.student_id = ?
    """;

    private static final String SQL_FIND_ALL = """
        SELECT
            s.student_id,
            COALESCE(tr_req.first_name, tr_en.first_name, s.first_name) AS first_name,
            COALESCE(tr_req.last_name, tr_en.last_name, s.last_name) AS last_name,
            s.student_number,
            s.email,
            s.user_id
        FROM student s
        LEFT JOIN student_translation tr_req
            ON s.student_id = tr_req.student_id
           AND tr_req.language_code = ?
        LEFT JOIN student_translation tr_en
            ON s.student_id = tr_en.student_id
           AND tr_en.language_code = 'en'
        ORDER BY s.student_id
    """;

    private static final String SQL_GET_TRANSLATED_STUDENT_NAME = """
        SELECT
            COALESCE(tr_req.first_name, tr_en.first_name, s.first_name) AS first_name,
            COALESCE(tr_req.last_name, tr_en.last_name, s.last_name) AS last_name
        FROM student s
        LEFT JOIN student_translation tr_req
            ON s.student_id = tr_req.student_id
           AND tr_req.language_code = ?
        LEFT JOIN student_translation tr_en
            ON s.student_id = tr_en.student_id
           AND tr_en.language_code = 'en'
        WHERE s.student_id = ?
    """;

    private static final String SQL_UPDATE = """
        UPDATE student
        SET first_name = ?, last_name = ?, student_number = ?, email = ?, user_id = ?
        WHERE student_id = ?
    """;

    private static final String SQL_DELETE = """
        DELETE FROM student
        WHERE student_id = ?
    """;

    private static final String SQL_EXISTS_BY_ID = """
        SELECT 1
        FROM student
        WHERE student_id = ?
    """;

    private static final String SQL_FIND_BY_STUDENT_NUMBER = """
        SELECT
            s.student_id,
            COALESCE(tr_req.first_name, tr_en.first_name, s.first_name) AS first_name,
            COALESCE(tr_req.last_name, tr_en.last_name, s.last_name) AS last_name,
            s.student_number,
            s.email,
            s.user_id
        FROM student s
        LEFT JOIN student_translation tr_req
            ON s.student_id = tr_req.student_id
           AND tr_req.language_code = ?
        LEFT JOIN student_translation tr_en
            ON s.student_id = tr_en.student_id
           AND tr_en.language_code = 'en'
        WHERE s.student_number = ?
    """;

    private static final String SQL_FIND_STUDENT_ID_BY_STUDENT_NUMBER = """
        SELECT student_id
        FROM student
        WHERE student_number = ?
    """;

    private static final String SQL_FIND_FIRST_STUDENT = """
        SELECT
            s.student_id,
            COALESCE(tr_req.first_name, tr_en.first_name, s.first_name) AS first_name,
            COALESCE(tr_req.last_name, tr_en.last_name, s.last_name) AS last_name,
            s.student_number,
            s.email,
            s.user_id
        FROM student s
        LEFT JOIN student_translation tr_req
            ON s.student_id = tr_req.student_id
           AND tr_req.language_code = ?
        LEFT JOIN student_translation tr_en
            ON s.student_id = tr_en.student_id
           AND tr_en.language_code = 'en'
        ORDER BY s.student_id ASC
        LIMIT 1
    """;

    private static final String SQL_FIND_ALL_BASIC = """
        SELECT
            s.student_id,
            COALESCE(tr_req.first_name, tr_en.first_name, s.first_name) AS first_name,
            COALESCE(tr_req.last_name, tr_en.last_name, s.last_name) AS last_name,
            s.student_number
        FROM student s
        LEFT JOIN student_translation tr_req
            ON s.student_id = tr_req.student_id
           AND tr_req.language_code = ?
        LEFT JOIN student_translation tr_en
            ON s.student_id = tr_en.student_id
           AND tr_en.language_code = 'en'
        ORDER BY s.student_number
    """;

    // CREATE
    @Override
    public void create(Student s) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_CREATE)) {

            ps.setInt(1, s.getStudentId());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getStudentNumber());
            ps.setString(5, s.getEmail());
            ps.setInt(6, s.getUserId());

            ps.executeUpdate();
        }
    }

    // FIND BY ID
    @Override
    public Student findById(int studentId) throws SQLException {
        return findById(studentId, DEFAULT_LANGUAGE);
    }

    @Override
    public Student findById(int studentId, String languageCode) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setString(1, normalizeLanguage(languageCode));
            ps.setInt(2, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapRow(rs);
            }
        }
    }

    // FIND ALL
    @Override
    public List<Student> findAll() throws SQLException {
        return findAll(DEFAULT_LANGUAGE);
    }

    @Override
    public List<Student> findAll(String languageCode) throws SQLException {
        List<Student> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL)) {

            ps.setString(1, normalizeLanguage(languageCode));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // Get translated full name for marksheet page
    public String getTranslatedStudentName(int studentId, String languageCode) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_GET_TRANSLATED_STUDENT_NAME)) {

            ps.setString(1, normalizeLanguage(languageCode));
            ps.setInt(2, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString(COL_FIRST_NAME);
                    String lastName = rs.getString(COL_LAST_NAME);

                    if (firstName == null) {
                        firstName = "";
                    }
                    if (lastName == null) {
                        lastName = "";
                    }

                    return (firstName + " " + lastName).trim();
                }
            }
        }

        return "";
    }

    // UPDATE
    @Override
    public void update(Student s) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {

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
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    // EXISTS
    @Override
    public boolean existsById(int studentId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTS_BY_ID)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // FIND BY STUDENT NUMBER
    @Override
    public Student findByStudentNumber(String studentNumber) throws SQLException {
        return findByStudentNumber(studentNumber, DEFAULT_LANGUAGE);
    }

    @Override
    public Student findByStudentNumber(String studentNumber, String languageCode) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_STUDENT_NUMBER)) {

            ps.setString(1, normalizeLanguage(languageCode));
            ps.setString(2, studentNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapRow(rs);
            }
        }
    }

    // REQUIRED by StudentDetailsController
    @Override
    public int findStudentIdByStudentNumber(String studentNumber) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_STUDENT_ID_BY_STUDENT_NUMBER)) {

            ps.setString(1, studentNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Student not found: " + studentNumber);
                }
                return rs.getInt(COL_STUDENT_ID);
            }
        }
    }

    // FIND FIRST STUDENT
    @Override
    public Student findFirstStudent() throws SQLException {
        return findFirstStudent(DEFAULT_LANGUAGE);
    }

    @Override
    public Student findFirstStudent(String languageCode) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_FIRST_STUDENT)) {

            ps.setString(1, normalizeLanguage(languageCode));

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapRow(rs);
            }
        }
    }

    // FIND ALL BASIC
    @Override
    public List<Student> findAllBasic() throws SQLException {
        return findAllBasic(DEFAULT_LANGUAGE);
    }

    @Override
    public List<Student> findAllBasic(String languageCode) throws SQLException {
        List<Student> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL_BASIC)) {

            ps.setString(1, normalizeLanguage(languageCode));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setStudentId(rs.getInt(COL_STUDENT_ID));
                    s.setFirstName(rs.getString(COL_FIRST_NAME));
                    s.setLastName(rs.getString(COL_LAST_NAME));
                    s.setStudentNumber(rs.getString(COL_STUDENT_NUMBER));
                    list.add(s);
                }
            }
        }

        return list;
    }

    private String normalizeLanguage(String languageCode) {
        if (languageCode == null || languageCode.isBlank()) {
            return DEFAULT_LANGUAGE;
        }
        return languageCode.trim().toLowerCase();
    }

    // HELPER
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt(COL_STUDENT_ID));
        s.setFirstName(rs.getString(COL_FIRST_NAME));
        s.setLastName(rs.getString(COL_LAST_NAME));
        s.setStudentNumber(rs.getString(COL_STUDENT_NUMBER));
        s.setEmail(rs.getString(COL_EMAIL));
        s.setUserId(rs.getInt(COL_USER_ID));
        return s;
    }
}