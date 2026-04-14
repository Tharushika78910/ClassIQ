package backend.model.dao.impl;

import backend.db.DBConnection;
import backend.model.dao.MarksDao;
import backend.model.dto.StudentDetailsDTO;
import backend.model.entity.Student;
import backend.model.entity.StudentMarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarksDaoImpl implements MarksDao {

    private static final String FEEDBACK_COL = "feed_back";
    private static final String DEFAULT_LANGUAGE = "en";

    private static final String COL_MARKS_ID = "marks_id";
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_STUDENT_NUMBER = "student_number";
    private static final String COL_EMAIL = "email";
    private static final String COL_USER_ID = "user_id";

    private static final String COL_MATHEMATICS = "mathematics";
    private static final String COL_ENGLISH = "english";
    private static final String COL_SCIENCE = "science";
    private static final String COL_CRAFT = "craft";
    private static final String COL_LANGUAGES = "languages";
    private static final String COL_TOTAL = "total";
    private static final String COL_AVERAGE = "average";

    private String normalizeLanguage(String languageCode) {
        if (languageCode == null || languageCode.isBlank()) {
            return DEFAULT_LANGUAGE;
        }
        return languageCode.trim().toLowerCase();
    }

    @Override
    public void saveMarks(StudentMarks marks) throws SQLException {

        String sql = """
            INSERT INTO student_marks
              (student_id, mathematics, english, science, craft, languages, total, average, feed_back)
            VALUES
              (?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
              mathematics = VALUES(mathematics),
              english     = VALUES(english),
              science     = VALUES(science),
              craft       = VALUES(craft),
              languages   = VALUES(languages),
              total       = VALUES(total),
              average     = VALUES(average),
              feed_back   = VALUES(feed_back)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, marks.getStudentId());
            ps.setInt(2, marks.getSubject1());
            ps.setInt(3, marks.getSubject2());
            ps.setInt(4, marks.getSubject3());
            ps.setInt(5, marks.getSubject4());
            ps.setInt(6, marks.getSubject5());
            ps.setInt(7, marks.getTotal());
            ps.setDouble(8, marks.getAverage());

            String fb = marks.getFeedback() == null ? "" : marks.getFeedback();
            ps.setString(9, fb);

            ps.executeUpdate();
        }
    }

    @Override
    public StudentMarks findByStudentId(int studentId) throws SQLException {

        String sql = """
            SELECT marks_id, student_id, mathematics, english, science, craft, languages, total, average, feed_back
            FROM student_marks
            WHERE student_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                StudentMarks m = new StudentMarks();
                m.setMarksId(rs.getInt(COL_MARKS_ID));
                m.setStudentId(rs.getInt(COL_STUDENT_ID));

                m.setSubject1(rs.getObject(COL_MATHEMATICS) == null ? 0 : rs.getInt(COL_MATHEMATICS));
                m.setSubject2(rs.getObject(COL_ENGLISH) == null ? 0 : rs.getInt(COL_ENGLISH));
                m.setSubject3(rs.getObject(COL_SCIENCE) == null ? 0 : rs.getInt(COL_SCIENCE));
                m.setSubject4(rs.getObject(COL_CRAFT) == null ? 0 : rs.getInt(COL_CRAFT));
                m.setSubject5(rs.getObject(COL_LANGUAGES) == null ? 0 : rs.getInt(COL_LANGUAGES));

                m.setTotal(rs.getObject(COL_TOTAL) == null ? 0 : rs.getInt(COL_TOTAL));
                m.setAverage(rs.getObject(COL_AVERAGE) == null ? 0.0 : rs.getDouble(COL_AVERAGE));
                m.setFeedback(rs.getString(FEEDBACK_COL));

                return m;
            }
        }
    }

    @Override
    public StudentDetailsDTO findStudentDetails(int studentId, String languageCode) throws SQLException {

        String sql = """
            SELECT
                s.student_id,
                COALESCE(tr_req.first_name, tr_en.first_name, s.first_name) AS first_name,
                COALESCE(tr_req.last_name, tr_en.last_name, s.last_name) AS last_name,
                s.student_number,
                s.email,
                s.user_id,
                sm.marks_id,
                sm.mathematics,
                sm.english,
                sm.science,
                sm.craft,
                sm.languages,
                sm.total,
                sm.average,
                sm.feed_back
            FROM student s
            LEFT JOIN student_translation tr_req
                ON s.student_id = tr_req.student_id
               AND tr_req.language_code = ?
            LEFT JOIN student_translation tr_en
                ON s.student_id = tr_en.student_id
               AND tr_en.language_code = 'en'
            LEFT JOIN student_marks sm
                ON sm.student_id = s.student_id
            WHERE s.student_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, normalizeLanguage(languageCode));
            ps.setInt(2, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Student st = new Student();
                st.setStudentId(rs.getInt(COL_STUDENT_ID));
                st.setFirstName(rs.getString(COL_FIRST_NAME));
                st.setLastName(rs.getString(COL_LAST_NAME));
                st.setStudentNumber(rs.getString(COL_STUDENT_NUMBER));
                st.setEmail(rs.getString(COL_EMAIL));
                st.setUserId(rs.getInt(COL_USER_ID));

                StudentMarks mk = new StudentMarks();
                mk.setStudentId(studentId);

                Integer marksId = (Integer) rs.getObject(COL_MARKS_ID);
                if (marksId != null) {
                    mk.setMarksId(marksId);
                }

                mk.setSubject1(rs.getObject(COL_MATHEMATICS) == null ? 0 : rs.getInt(COL_MATHEMATICS));
                mk.setSubject2(rs.getObject(COL_ENGLISH) == null ? 0 : rs.getInt(COL_ENGLISH));
                mk.setSubject3(rs.getObject(COL_SCIENCE) == null ? 0 : rs.getInt(COL_SCIENCE));
                mk.setSubject4(rs.getObject(COL_CRAFT) == null ? 0 : rs.getInt(COL_CRAFT));
                mk.setSubject5(rs.getObject(COL_LANGUAGES) == null ? 0 : rs.getInt(COL_LANGUAGES));

                mk.setTotal(rs.getObject(COL_TOTAL) == null ? 0 : rs.getInt(COL_TOTAL));
                mk.setAverage(rs.getObject(COL_AVERAGE) == null ? 0.0 : rs.getDouble(COL_AVERAGE));
                mk.setFeedback(rs.getString(FEEDBACK_COL));

                StudentDetailsDTO dto = new StudentDetailsDTO();
                dto.setStudent(st);
                dto.setMarks(mk);

                return dto;
            }
        }
    }

    @Override
    public void saveOrUpdateFeedback(int studentId, String feedback) throws SQLException {

        String fb = feedback == null ? "" : feedback;

        String sql = """
            INSERT INTO student_marks
              (student_id, mathematics, english, science, craft, languages, total, average, feed_back)
            VALUES
              (?, 0, 0, 0, 0, 0, 0, 0.0, ?)
            ON DUPLICATE KEY UPDATE
              feed_back = VALUES(feed_back)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setString(2, fb);
            ps.executeUpdate();
        }
    }

    @Override
    public String findFeedback(int studentId) throws SQLException {

        String sql = "SELECT feed_back FROM student_marks WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return rs.getString(FEEDBACK_COL);
            }
        }
    }

    @Override
    public void deleteFeedback(int studentId) throws SQLException {

        String sql = "UPDATE student_marks SET feed_back = '' WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }
}