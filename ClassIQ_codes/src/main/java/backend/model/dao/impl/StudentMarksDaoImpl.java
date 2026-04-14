package backend.model.dao.impl;

import backend.db.DBConnection;
import backend.model.entity.StudentMarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class StudentMarksDaoImpl {

    private static final String COL_MARKS_ID = "marks_id";
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_MATHEMATICS = "mathematics";
    private static final String COL_ENGLISH = "english";
    private static final String COL_SCIENCE = "science";
    private static final String COL_CRAFT = "craft";
    private static final String COL_LANGUAGES = "languages";
    private static final String COL_TOTAL = "total";
    private static final String COL_AVERAGE = "average";

    private static final String IFNULL_PREFIX = "IFNULL(";
    private static final String ZERO_SUFFIX = ",0)";
    private static final String PLUS = " + ";

    private static final Set<String> ALLOWED_SUBJECT_COLUMNS = Set.of(
            COL_MATHEMATICS,
            COL_ENGLISH,
            COL_SCIENCE,
            COL_CRAFT,
            COL_LANGUAGES
    );

    private static final String SQL_TOTAL_EXPRESSION =
            IFNULL_PREFIX + COL_MATHEMATICS + ZERO_SUFFIX + PLUS +
                    IFNULL_PREFIX + COL_ENGLISH + ZERO_SUFFIX + PLUS +
                    IFNULL_PREFIX + COL_SCIENCE + ZERO_SUFFIX + PLUS +
                    IFNULL_PREFIX + COL_CRAFT + ZERO_SUFFIX + PLUS +
                    IFNULL_PREFIX + COL_LANGUAGES + ZERO_SUFFIX;

    private static final String SQL_AVERAGE_EXPRESSION =
            "ROUND((" + SQL_TOTAL_EXPRESSION + ") / 5, 2)";

    // save/update subject total into student_marks (one student)
    public void upsertSubjectTotal(int studentId, String subject, int subjectTotal) throws SQLException {
        String col = validateAllowedColumn(mapSubjectToColumn(subject));

        String upsert =
                "INSERT INTO student_marks (student_id, " + col + ") " +
                        "VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE " + col + " = VALUES(" + col + ")";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(upsert)) {

            ps.setInt(1, studentId);
            ps.setInt(2, subjectTotal);
            ps.executeUpdate();
        }

        recalcTotals(studentId);
    }

    public StudentMarks findByStudentId(int studentId) throws SQLException {
        String sql = """
            SELECT marks_id, student_id, mathematics, english, science, craft, languages, total, average
            FROM student_marks
            WHERE student_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StudentMarks marks = new StudentMarks();

                    marks.setMarksId(rs.getInt(COL_MARKS_ID));
                    marks.setStudentId(rs.getInt(COL_STUDENT_ID));
                    marks.setSubject1(rs.getInt(COL_MATHEMATICS));
                    marks.setSubject2(rs.getInt(COL_ENGLISH));
                    marks.setSubject3(rs.getInt(COL_SCIENCE));
                    marks.setSubject4(rs.getInt(COL_CRAFT));
                    marks.setSubject5(rs.getInt(COL_LANGUAGES));
                    marks.setTotal(rs.getInt(COL_TOTAL));
                    marks.setAverage(rs.getDouble(COL_AVERAGE));

                    return marks;
                }
            }
        }

        return null;
    }

    // clear subject for ALL students (when teacher clicks Clear)
    public int clearSubjectForAllStudents(String subject) throws SQLException {
        String col = validateAllowedColumn(mapSubjectToColumn(subject));

        String sql = "UPDATE student_marks SET " + col + " = 0";

        int updated;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            updated = ps.executeUpdate();
        }

        recalcTotalsForAllStudents();
        return updated;
    }

    private void recalcTotals(int studentId) throws SQLException {
        String sql =
                "UPDATE student_marks " +
                        "SET total = " + SQL_TOTAL_EXPRESSION + ", " +
                        "    average = " + SQL_AVERAGE_EXPRESSION + " " +
                        "WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    private void recalcTotalsForAllStudents() throws SQLException {
        String sql =
                "UPDATE student_marks " +
                        "SET total = " + SQL_TOTAL_EXPRESSION + ", " +
                        "    average = " + SQL_AVERAGE_EXPRESSION;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    private String mapSubjectToColumn(String subject) {
        if (subject == null) {
            return COL_MATHEMATICS;
        }

        String s = subject.trim().toLowerCase();

        if (s.contains("math")) {
            return COL_MATHEMATICS;
        }
        if (s.contains("eng")) {
            return COL_ENGLISH;
        }
        if (s.contains("sci")) {
            return COL_SCIENCE;
        }
        if (s.contains(COL_CRAFT)) {
            return COL_CRAFT;
        }
        if (s.contains("lang")) {
            return COL_LANGUAGES;
        }

        return COL_MATHEMATICS;
    }

    private String validateAllowedColumn(String column) {
        if (!ALLOWED_SUBJECT_COLUMNS.contains(column)) {
            throw new IllegalArgumentException("Invalid subject column: " + column);
        }
        return column;
    }
}