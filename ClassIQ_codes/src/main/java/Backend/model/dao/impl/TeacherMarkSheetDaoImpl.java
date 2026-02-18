package Backend.model.dao.impl;

import Backend.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TeacherMarkSheetDaoImpl {

    private static final String UPSERT_SQL =
            "INSERT INTO teacher_marksheet " +
                    "(student_id, student_name, assignment, project, final_exam, total, grade, teacher_id, subject) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "student_name = VALUES(student_name), " +
                    "assignment = VALUES(assignment), " +
                    "project = VALUES(project), " +
                    "final_exam = VALUES(final_exam), " +
                    "total = VALUES(total), " +
                    "grade = VALUES(grade), " +
                    "created_at = CURRENT_TIMESTAMP";

    private static final String DELETE_SQL =
            "DELETE FROM teacher_marksheet " +
                    "WHERE teacher_id = ? AND UPPER(subject) = UPPER(?)";

    private static final String LOAD_EXISTING_SQL =
            "SELECT student_id, assignment, project, final_exam " +
                    "FROM teacher_marksheet " +
                    "WHERE teacher_id = ? AND UPPER(subject) = UPPER(?)";

    public void saveTeacherMarkSheetRow(
            int studentId,
            String studentName,
            int assignment,
            int project,
            int finalExam,
            int total,
            String grade,
            int teacherId,
            String subject
    ) throws SQLException {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPSERT_SQL)) {

            ps.setInt(1, studentId);
            ps.setString(2, studentName);
            ps.setInt(3, assignment);
            ps.setInt(4, project);
            ps.setInt(5, finalExam);
            ps.setInt(6, total);
            ps.setString(7, grade);
            ps.setInt(8, teacherId);
            ps.setString(9, subject);

            ps.executeUpdate();
        }
    }

    // used by Clear button (returns how many rows were deleted)
    public int deleteMarksForTeacherSubject(int teacherId, String subject) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, teacherId);
            ps.setString(2, subject);

            return ps.executeUpdate();
        }
    }

    // load existing marks so UI can pre-fill
    // returns: student_id -> [assignment, project, final_exam]
    public Map<Integer, int[]> loadExistingMarks(int teacherId, String subject) throws SQLException {

        Map<Integer, int[]> map = new HashMap<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(LOAD_EXISTING_SQL)) {

            ps.setInt(1, teacherId);
            ps.setString(2, subject);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int sid = rs.getInt("student_id");
                    int a = rs.getInt("assignment");
                    int p = rs.getInt("project");
                    int f = rs.getInt("final_exam");
                    map.put(sid, new int[]{a, p, f});
                }
            }
        }

        return map;
    }
}
