package Backend.model.dao;

import Backend.model.entity.Student;
import java.sql.SQLException;
import java.util.List;

public interface StudentDao {
    void create(Student student) throws SQLException;
    Student findById(int studentId) throws SQLException;
    List<Student> findAll() throws SQLException;
    void update(Student student) throws SQLException;
    void delete(int studentId) throws SQLException;
    boolean existsById(int studentId) throws SQLException;
}
