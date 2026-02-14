package Backend.model.dao.impl;

import Backend.db.DBConnection;
import Backend.model.dao.MarksDao;
import Backend.model.entity.StudentMarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarksDaoImpl implements MarksDao {

    private static final String FEEDBACK_COL = "feed_back";

    @Override
    public StudentMarks findByStudentId(int studentId) throws SQLException {

        String sql = "SELECT * FROM student_marks WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null; // no row yet

                StudentMarks m = new StudentMarks();
                m.setMarksId(rs.getInt("marks_id"));
                m.setStudentId(rs.getInt("student_id"));
                m.setSubject1(rs.getInt("subject1"));
                m.setSubject2(rs.getInt("subject2"));
                m.setSubject3(rs.getInt("subject3"));
                m.setSubject4(rs.getInt("subject4"));
                m.setSubject5(rs.getInt("subject5"));
                m.setTotal(rs.getInt("total"));
                m.setAverage(rs.getDouble("average"));
                m.setFeedback(rs.getString(FEEDBACK_COL));
                return m;
            }
        }
    }

    // Insert marks OR update if the row already exists
    @Override
    public void saveMarks(StudentMarks marks) throws SQLException {

        if (marks == null) throw new IllegalArgumentException("marks cannot be null");

        // 1) Try UPDATE first
        String updateSql = """
            UPDATE student_marks
            SET subject1=?, subject2=?, subject3=?, subject4=?, subject5=?, total=?, average=?
            WHERE student_id=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(updateSql)) {

            ps.setInt(1, marks.getSubject1());
            ps.setInt(2, marks.getSubject2());
            ps.setInt(3, marks.getSubject3());
            ps.setInt(4, marks.getSubject4());
            ps.setInt(5, marks.getSubject5());
            ps.setInt(6, marks.getTotal());
            ps.setDouble(7, marks.getAverage());
            ps.setInt(8, marks.getStudentId());

            int updated = ps.executeUpdate();

            // 2) If no row updated -> INSERT
            if (updated == 0) {

                String insertSql = """
                    INSERT INTO student_marks
                    (student_id, subject1, subject2, subject3, subject4, subject5, total, average, feed_back)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

                try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                    ins.setInt(1, marks.getStudentId());
                    ins.setInt(2, marks.getSubject1());
                    ins.setInt(3, marks.getSubject2());
                    ins.setInt(4, marks.getSubject3());
                    ins.setInt(5, marks.getSubject4());
                    ins.setInt(6, marks.getSubject5());
                    ins.setInt(7, marks.getTotal());
                    ins.setDouble(8, marks.getAverage());

                    // keep feedback if provided, otherwise empty
                    String fb = marks.getFeedback() == null ? "" : marks.getFeedback();
                    ins.setString(9, fb);

                    ins.executeUpdate();
                }
            }
        }
    }

    @Override
    public void saveOrUpdateFeedback(int studentId, String feedback) throws SQLException {

        String updateSql = "UPDATE student_marks SET " + FEEDBACK_COL + " = ? WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(updateSql)) {

            ps.setString(1, feedback == null ? "" : feedback);
            ps.setInt(2, studentId);

            int updated = ps.executeUpdate();

            // If no row updated -> INSERT row (marks default 0)
            if (updated == 0) {
                String insertSql = """
                    INSERT INTO student_marks
                    (student_id, subject1, subject2, subject3, subject4, subject5, total, average, feed_back)
                    VALUES (?, 0, 0, 0, 0, 0, 0, 0, ?)
                    """;

                try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                    ins.setInt(1, studentId);
                    ins.setString(2, feedback == null ? "" : feedback);
                    ins.executeUpdate();
                }
            }
        }
    }
}
