package Backend.service;

import Backend.model.dao.MarksDao;
import Backend.model.dao.impl.MarksDaoImpl;
import Backend.model.dto.StudentDetailsDTO;

public class StudentDetailsService {

    private final MarksDao marksDao = new MarksDaoImpl();

    public StudentDetailsDTO getStudentDetails(int studentId) throws Exception {
        return marksDao.findStudentDetails(studentId);
    }

    public void saveFeedback(int studentId, String feedback) throws Exception {
        marksDao.saveOrUpdateFeedback(studentId, feedback);
    }

    public String getFeedback(int studentId) throws Exception {
        return marksDao.findFeedback(studentId);
    }


    public void deleteFeedback(int studentId) throws Exception {
        marksDao.deleteFeedback(studentId);
    }
}