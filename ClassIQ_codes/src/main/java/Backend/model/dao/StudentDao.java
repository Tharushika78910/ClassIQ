package Backend.model.dao;

import Backend.model.entity.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDao {

    // Basic CRUD
    void create(Student student) throws SQLException;

    Student findById(int studentId) throws SQLException;

    List<Student> findAll() throws SQLException;

    void update(Student student) throws SQLException;

    void delete(int studentId) throws SQLException;

    boolean existsById(int studentId) throws SQLException;

    // Search by student_number
    Student findByStudentNumber(String studentNumber) throws SQLException;

    //  Needed by StudentDetailsController
    int findStudentIdByStudentNumber(String studentNumber) throws SQLException;

    // For initial display when page opens
    Student findFirstStudent() throws SQLException;

    // For the reference table (Student Number + Full Name)
    List<Student> findAllBasic() throws SQLException;
}
