package Backend.model.dao.impl;

import Backend.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TeacherMarkSheetDaoImpl {

    private static final String SQL =
            "INSERT INTO teacher_marksheet " +
                    "(student_id, student_name, assignment, project, final_exam, total, grade) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public void saveTeacherMarkSheetRow(
            int studentId,
            String studentName,
            int assignment,
            int project,
            int finalExam,
            int total,
            String grade
    ) throws SQLException {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, studentId);
            ps.setString(2, studentName);
            ps.setInt(3, assignment);
            ps.setInt(4, project);
            ps.setInt(5, finalExam);
            ps.setInt(6, total);
            ps.setString(7, grade);

            ps.executeUpdate();
        }
    }
}
