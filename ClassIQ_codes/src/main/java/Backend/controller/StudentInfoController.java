package Backend.controller;

import Backend.model.entity.Student;
import Backend.service.StudentInfoService;

import java.sql.SQLException;
import java.util.List;

public class StudentInfoController {

    private final StudentInfoService service = new StudentInfoService();

    public Student getInitialStudent(String languageCode) throws SQLException {
        return service.loadFirstStudent(languageCode);
    }

    public Student findStudentByNumber(String studentNumber, String languageCode) throws SQLException {
        return service.searchByStudentNumber(studentNumber, languageCode);
    }

    public List<Student> getAllStudentsBasic(String languageCode) throws SQLException {
        return service.loadAllStudentsBasic(languageCode);
    }
}