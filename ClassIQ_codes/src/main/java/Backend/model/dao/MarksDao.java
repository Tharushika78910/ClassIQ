package Backend.model.dao;

import Backend.model.dto.StudentDetailsDTO;
import Backend.model.entity.StudentMarks;

import java.sql.SQLException;

public interface MarksDao {

    // Used by MarksController
    void saveMarks(StudentMarks marks) throws SQLException;

    StudentMarks findByStudentId(int studentId) throws SQLException;

    // Used by StudentDetailsPage
    StudentDetailsDTO findStudentDetails(int studentId) throws SQLException;

    void saveOrUpdateFeedback(int studentId, String feedback) throws SQLException;

    String findFeedback(int studentId) throws SQLException;

    //  delete/clear feedback only (do NOT delete marks row)
    void deleteFeedback(int studentId) throws SQLException;
}