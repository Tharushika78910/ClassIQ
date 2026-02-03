package Backend.model.dao;

import Backend.model.entity.StudentMarks;

import java.sql.SQLException;

public interface MarksDao {
    void saveMarks(StudentMarks marks) throws SQLException;
}

