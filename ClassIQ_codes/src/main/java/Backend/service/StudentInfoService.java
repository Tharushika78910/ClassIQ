package Backend.service;

import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.entity.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentInfoService {

    private final StudentDao studentDao = new StudentDaoImpl();

    //  INITIAL DISPLAY
    public Student loadFirstStudent() throws SQLException {
        return studentDao.findFirstStudent();
    }

    //  SEARCH BY STUDENT NUMBER
    public Student searchByStudentNumber(String studentNumber) throws SQLException {

        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Student number is required.");
        }

        return studentDao.findByStudentNumber(studentNumber.trim());
    }

    //  LOAD ALL STUDENTS (FOR TABLE REFERENCE)
    public List<Student> loadAllStudentsBasic() throws SQLException {
        return studentDao.findAllBasic();
    }
}
