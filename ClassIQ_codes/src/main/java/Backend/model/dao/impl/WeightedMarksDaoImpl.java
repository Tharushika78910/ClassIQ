package Backend.model.dao.impl;

import Backend.config.DatabaseConfig;
import Backend.model.dao.WeightedMarksDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WeightedMarksDaoImpl implements WeightedMarksDao {

    @Override
    public void upsertSubjectWeightedMarks(int studentId, int subjectNo,
                                           int assignment, int project, int finalExam,
                                           double weighted, String grade) throws SQLException {

        String sql = """
            INSERT INTO student_subject_weighted_marks
              (student_id, subject_no, assignment_mark, project_mark, final_exam_mark, weighted_mark, grade)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
              assignment_mark = VALUES(assignment_mark),
              project_mark = VALUES(project_mark),
              final_exam_mark = VALUES(final_exam_mark),
              weighted_mark = VALUES(weighted_mark),
              grade = VALUES(grade)
            """;

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, subjectNo);
            ps.setInt(3, assignment);
            ps.setInt(4, project);
            ps.setInt(5, finalExam);
            ps.setDouble(6, weighted);
            ps.setString(7, grade);

            ps.executeUpdate();
        }
    }
}
