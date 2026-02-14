package Backend.controller;

import Backend.model.dto.StudentDetailsDTO;
import Backend.service.StudentDetailsService;

import java.sql.SQLException;

public class StudentDetailsController {

    private final StudentDetailsService service = new StudentDetailsService();

    public StudentDetailsDTO getDetails(String studentNumber) throws SQLException {
        return service.loadDetailsByStudentNumber(studentNumber);
    }

    public void saveFeedback(String studentNumber, String feedback) throws SQLException {
        service.saveFeedback(studentNumber, feedback);
    }
}

