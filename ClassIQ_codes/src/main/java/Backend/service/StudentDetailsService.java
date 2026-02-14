package Backend.service;

import Backend.model.dao.MarksDao;
import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.MarksDaoImpl;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.dto.StudentDetailsDTO;
import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;

import java.sql.SQLException;

public class StudentDetailsService {

    private final StudentDao studentDao = new StudentDaoImpl();
    private final MarksDao marksDao = new MarksDaoImpl();

    // Load student + marks (if marks row missing -> return zeros)
    public StudentDetailsDTO loadDetailsByStudentNumber(String studentNumber) throws SQLException {

        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Student number is required.");
        }

        Student student = studentDao.findByStudentNumber(studentNumber.trim());
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentNumber);
        }

        StudentMarks marks = marksDao.findByStudentId(student.getStudentId());

        // Table is empty now -> safe default values
        if (marks == null) {
            marks = new StudentMarks();
            marks.setStudentId(student.getStudentId());
            marks.setSubject1(0);
            marks.setSubject2(0);
            marks.setSubject3(0);
            marks.setSubject4(0);
            marks.setSubject5(0);
            marks.setTotal(0);
            marks.setAverage(0);
            marks.setFeedback("");
        }

        StudentDetailsDTO dto = new StudentDetailsDTO();
        dto.setStudent(student);
        dto.setMarks(marks);
        return dto;
    }

    public void saveFeedback(String studentNumber, String feedback) throws SQLException {

        Student student = studentDao.findByStudentNumber(studentNumber.trim());
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentNumber);
        }

        marksDao.saveOrUpdateFeedback(student.getStudentId(), feedback == null ? "" : feedback);
    }
}
