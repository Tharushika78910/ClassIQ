package Backend.model.dao.impl;

import Backend.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentMarksDaoImpl {

    // save/update subject total into student_marks (one student)
    public void upsertSubjectTotal(int studentId, String subject, int subjectTotal) throws SQLException {
        String col = mapSubjectToColumn(subject);

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

        // update total + average for that student
        recalcTotals(studentId);
    }

    // clear subject for ALL students (when teacher clicks Clear)
    public int clearSubjectForAllStudents(String subject) throws SQLException {
        String col = mapSubjectToColumn(subject);

        String sql = "UPDATE student_marks SET " + col + " = 0";

        int updated;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            updated = ps.executeUpdate();
        }

        // update total + average for ALL students
        recalcTotalsForAllStudents();
        return updated;
    }

    // helpers

    private void recalcTotals(int studentId) throws SQLException {
        String sql =
                "UPDATE student_marks " +
                        "SET total = IFNULL(mathematics,0) + IFNULL(english,0) + IFNULL(science,0) + IFNULL(craft,0) + IFNULL(languages,0), " +
                        "    average = ROUND((IFNULL(mathematics,0) + IFNULL(english,0) + IFNULL(science,0) + IFNULL(craft,0) + IFNULL(languages,0)) / 5, 2) " +
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
                        "SET total = IFNULL(mathematics,0) + IFNULL(english,0) + IFNULL(science,0) + IFNULL(craft,0) + IFNULL(languages,0), " +
                        "    average = ROUND((IFNULL(mathematics,0) + IFNULL(english,0) + IFNULL(science,0) + IFNULL(craft,0) + IFNULL(languages,0)) / 5, 2)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    private String mapSubjectToColumn(String subject) {
        if (subject == null) return "mathematics";

        String s = subject.trim().toLowerCase();

        if (s.contains("math")) return "mathematics";
        if (s.contains("eng")) return "english";
        if (s.contains("sci")) return "science";
        if (s.contains("craft")) return "craft";
        if (s.contains("lang")) return "languages";

        return "mathematics";
    }
}