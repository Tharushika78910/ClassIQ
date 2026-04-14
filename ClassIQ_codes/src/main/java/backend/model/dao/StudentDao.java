package backend.model.dao;

import backend.model.entity.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDao {

    void create(Student student) throws SQLException;

    Student findById(int studentId) throws SQLException;
    Student findById(int studentId, String languageCode) throws SQLException;

    List<Student> findAll() throws SQLException;
    List<Student> findAll(String languageCode) throws SQLException;

    void update(Student student) throws SQLException;

    void delete(int studentId) throws SQLException;

    boolean existsById(int studentId) throws SQLException;

    Student findByStudentNumber(String studentNumber) throws SQLException;
    Student findByStudentNumber(String studentNumber, String languageCode) throws SQLException;

    int findStudentIdByStudentNumber(String studentNumber) throws SQLException;

    Student findFirstStudent() throws SQLException;
    Student findFirstStudent(String languageCode) throws SQLException;

    List<Student> findAllBasic() throws SQLException;
    List<Student> findAllBasic(String languageCode) throws SQLException;
}