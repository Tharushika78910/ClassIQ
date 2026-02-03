package Backend.model.dao;

import Backend.model.entity.Student;

import java.sql.SQLException;

public interface StudentDao {
    void saveStudent(Student student) throws SQLException;
    boolean existsById(int studentId) throws SQLException;
}

