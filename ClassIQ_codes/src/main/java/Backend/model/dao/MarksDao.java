package Backend.model.dao;

import Backend.model.entity.StudentMarks;
import java.sql.SQLException;

public interface MarksDao {

    StudentMarks findByStudentId(int studentId) throws SQLException;

    void saveMarks(StudentMarks marks) throws SQLException;   // ✅ add this

    void saveOrUpdateFeedback(int studentId, String feedback) throws SQLException;
}
