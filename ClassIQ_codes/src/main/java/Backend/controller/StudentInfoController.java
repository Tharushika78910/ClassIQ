package Backend.controller;

import Backend.model.entity.Student;
import Backend.service.StudentInfoService;

import java.sql.SQLException;
import java.util.List;

public class StudentInfoController {

    private final StudentInfoService service = new StudentInfoService();

    // INITIAL DISPLAY
    public Student getInitialStudent() throws SQLException {
        return service.loadFirstStudent();
    }

    //  SEARCH BY STUDENT NUMBER
    public Student findStudentByNumber(String studentNumber) throws SQLException {
        return service.searchByStudentNumber(studentNumber);
    }

    //  LOAD ALL STUDENTS (FOR TABLE VIEW)
    public List<Student> getAllStudentsBasic() throws SQLException {
        return service.loadAllStudentsBasic();
    }
}
