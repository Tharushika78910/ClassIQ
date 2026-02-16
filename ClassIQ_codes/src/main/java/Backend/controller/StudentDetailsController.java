package Backend.controller;

import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.dto.StudentDetailsDTO;
import Backend.service.StudentDetailsService;

public class StudentDetailsController {

    private final StudentDetailsService service = new StudentDetailsService();
    private final StudentDao studentDao = new StudentDaoImpl();

    public StudentDetailsDTO getDetails(String studentNumber) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        return service.getStudentDetails(studentId);
    }

    public void saveFeedback(String studentNumber, String feedback) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        service.saveFeedback(studentId, feedback);
    }

    public String loadFeedback(String studentNumber) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        return service.getFeedback(studentId);
    }
}
