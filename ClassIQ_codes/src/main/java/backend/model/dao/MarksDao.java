package backend.model.dao;

import backend.model.dto.StudentDetailsDTO;
import backend.model.entity.StudentMarks;

import java.sql.SQLException;

public interface MarksDao {

    void saveMarks(StudentMarks marks) throws SQLException;

    StudentMarks findByStudentId(int studentId) throws SQLException;

    // localized student details
    StudentDetailsDTO findStudentDetails(int studentId, String languageCode) throws SQLException;

    void saveOrUpdateFeedback(int studentId, String feedback) throws SQLException;

    String findFeedback(int studentId) throws SQLException;

    void deleteFeedback(int studentId) throws SQLException;
}