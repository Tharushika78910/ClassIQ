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

    public StudentDetailsDTO getStudentDetails(int studentId, String languageCode) throws StudentDetailsServiceException {
        try {
            return marksDao.findStudentDetails(studentId, languageCode);
        } catch (Exception exception) {
            throw new StudentDetailsServiceException("Failed to load student details.", exception);
        }
    }

    public void saveFeedback(int studentId, String feedback) throws StudentDetailsServiceException {
        try {
            marksDao.saveOrUpdateFeedback(studentId, feedback);
        } catch (Exception exception) {
            throw new StudentDetailsServiceException("Failed to save feedback.", exception);
        }
    }

    public String getFeedback(int studentId) throws StudentDetailsServiceException {
        try {
            return marksDao.findFeedback(studentId);
        } catch (Exception exception) {
            throw new StudentDetailsServiceException("Failed to load feedback.", exception);
        }
    }

    public void deleteFeedback(int studentId) throws StudentDetailsServiceException {
        try {
            marksDao.deleteFeedback(studentId);
        } catch (Exception exception) {
            throw new StudentDetailsServiceException("Failed to delete feedback.", exception);
        }
    }
}