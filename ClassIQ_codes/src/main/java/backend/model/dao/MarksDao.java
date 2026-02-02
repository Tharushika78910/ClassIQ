package backend.model.dao;

import backend.model.entity.StudentMarks;

import java.sql.SQLException;

public interface MarksDao {
    void saveMarks(StudentMarks marks) throws SQLException;
}

