package Backend.model.dao;

import java.sql.SQLException;

public interface WeightedMarksDao {
    void upsertSubjectWeightedMarks(
            int studentId,
            int subjectNo,
            int assignment,
            int project,
            int finalExam,
            double weighted,
            String grade
    ) throws SQLException;
}

