package Backend.model.dao.impl;

import Backend.db.DBConnection;
import Backend.model.dao.MarksDao;
import Backend.model.dto.StudentDetailsDTO;
import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarksDaoImpl implements MarksDao {

    private static final String FEEDBACK_COL = "feed_back";

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
            ps.setInt(2, marks.getSubject1()); // mathematics
            ps.setInt(3, marks.getSubject2()); // english
            ps.setInt(4, marks.getSubject3()); // science
            ps.setInt(5, marks.getSubject4()); // craft
            ps.setInt(6, marks.getSubject5()); // languages

            ps.setInt(7, marks.getTotal());
            ps.setDouble(8, marks.getAverage());

            String fb = marks.getFeedback() == null ? "" : marks.getFeedback();
            ps.setString(9, fb);

            ps.executeUpdate();
        }
    }

    @Override
    public StudentMarks findByStudentId(int studentId) throws SQLException {

        String sql = "SELECT * FROM student_marks WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                StudentMarks m = new StudentMarks();
                m.setMarksId(rs.getInt("marks_id"));
                m.setStudentId(rs.getInt("student_id"));

                m.setSubject1(rs.getObject("mathematics") == null ? 0 : rs.getInt("mathematics"));
                m.setSubject2(rs.getObject("english") == null ? 0 : rs.getInt("english"));
                m.setSubject3(rs.getObject("science") == null ? 0 : rs.getInt("science"));
                m.setSubject4(rs.getObject("craft") == null ? 0 : rs.getInt("craft"));
                m.setSubject5(rs.getObject("languages") == null ? 0 : rs.getInt("languages"));

                m.setTotal(rs.getObject("total") == null ? 0 : rs.getInt("total"));
                m.setAverage(rs.getObject("average") == null ? 0.0 : rs.getDouble("average"));

                m.setFeedback(rs.getString(FEEDBACK_COL));

                return m;
            }
        }
    }

    @Override
    public StudentDetailsDTO findStudentDetails(int studentId) throws SQLException {

        String sql = """
            SELECT
                s.student_id, s.first_name, s.last_name, s.student_number, s.email, s.user_id,
                sm.marks_id, sm.mathematics, sm.english, sm.science, sm.craft, sm.languages,
                sm.total, sm.average, sm.feed_back
            FROM student s
            LEFT JOIN student_marks sm ON sm.student_id = s.student_id
            WHERE s.student_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Student st = new Student();
                st.setStudentId(rs.getInt("student_id"));
                st.setFirstName(rs.getString("first_name"));
                st.setLastName(rs.getString("last_name"));
                st.setStudentNumber(rs.getString("student_number"));
                st.setEmail(rs.getString("email"));
                st.setUserId(rs.getInt("user_id"));

                StudentMarks mk = new StudentMarks();
                mk.setStudentId(studentId);

                Integer marksId = (Integer) rs.getObject("marks_id");
                if (marksId != null) mk.setMarksId(marksId);

                mk.setSubject1(rs.getObject("mathematics") == null ? 0 : rs.getInt("mathematics"));
                mk.setSubject2(rs.getObject("english") == null ? 0 : rs.getInt("english"));
                mk.setSubject3(rs.getObject("science") == null ? 0 : rs.getInt("science"));
                mk.setSubject4(rs.getObject("craft") == null ? 0 : rs.getInt("craft"));
                mk.setSubject5(rs.getObject("languages") == null ? 0 : rs.getInt("languages"));

                mk.setTotal(rs.getObject("total") == null ? 0 : rs.getInt("total"));
                mk.setAverage(rs.getObject("average") == null ? 0.0 : rs.getDouble("average"));

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
                if (!rs.next()) return null;
                return rs.getString(FEEDBACK_COL);
            }
        }
    }

    // clear feedback in DB (do NOT delete the marks row)
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