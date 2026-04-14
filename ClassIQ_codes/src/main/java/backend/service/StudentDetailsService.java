package backend.service;

import backend.model.dao.MarksDao;
import backend.model.dao.impl.MarksDaoImpl;
import backend.model.dto.StudentDetailsDTO;

public class StudentDetailsService {

    private final MarksDao marksDao;

    public StudentDetailsService() {
        this(new MarksDaoImpl());
    }

    StudentDetailsService(MarksDao marksDao) {
        this.marksDao = marksDao;
    }

    public StudentDetailsDTO getStudentDetails(int studentId, String languageCode) throws Exception {
        return marksDao.findStudentDetails(studentId, languageCode);
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