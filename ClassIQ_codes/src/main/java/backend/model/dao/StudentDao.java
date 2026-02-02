package backend.model.dao;

import backend.model.entity.Student;

import java.sql.SQLException;

public interface StudentDao {
    void saveStudent(Student student) throws SQLException;
    boolean existsById(int studentId) throws SQLException;
}

