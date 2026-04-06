package Backend.service;

import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.entity.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentInfoService {

    private final StudentDao studentDao = new StudentDaoImpl();

    public Student loadFirstStudent(String languageCode) throws SQLException {
        return studentDao.findFirstStudent(languageCode);
    }

    public Student searchByStudentNumber(String studentNumber, String languageCode) throws SQLException {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Student number is required.");
        }
        return studentDao.findByStudentNumber(studentNumber.trim(), languageCode);
    }

    public List<Student> loadAllStudentsBasic(String languageCode) throws SQLException {
        return studentDao.findAllBasic(languageCode);
    }
}