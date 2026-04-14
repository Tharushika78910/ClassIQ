package backend.controller;

import backend.model.dao.StudentDao;
import backend.model.dao.impl.StudentDaoImpl;
import backend.model.dao.impl.UserProfileDaoImpl;
import backend.model.dto.StudentDetailsDTO;
import backend.service.StudentDetailsService;

import java.util.Locale;
import java.util.ResourceBundle;

public class StudentDetailsController {

    private final StudentDetailsService service;
    private final StudentDao studentDao;
    private final UserProfileDaoImpl userProfileDao;

    public StudentDetailsController() {
        this(new StudentDetailsService(), new StudentDaoImpl(), new UserProfileDaoImpl());
    }

    StudentDetailsController(
            StudentDetailsService service,
            StudentDao studentDao,
            UserProfileDaoImpl userProfileDao
    ) {
        this.service = service;
        this.studentDao = studentDao;
        this.userProfileDao = userProfileDao;
    }

    private ResourceBundle getBundle(String languageCode) {
        Locale locale = (languageCode == null || languageCode.isBlank())
                ? new Locale("en", "US")
                : new Locale(languageCode);
        return ResourceBundle.getBundle("messages", locale);
    }

    public StudentDetailsDTO getDetails(String studentNumber, String languageCode) throws StudentDetailsControllerException {
        try {
            int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
            return service.getStudentDetails(studentId, languageCode);
        } catch (Exception exception) {
            throw new StudentDetailsControllerException("Failed to get student details.", exception);
        }
    }

    public void saveFeedback(String studentNumber, String feedback, int teacherUserId, String languageCode)
            throws StudentDetailsControllerException {
        ResourceBundle bundle = getBundle(languageCode);

        var teacher = userProfileDao.findTeacherByUserId(teacherUserId);
        if (teacher == null) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.teacherNotFound"));
        }

        String subject = teacher.subject == null ? "" : teacher.subject.trim();

        if (!isMathSubject(subject)) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.onlyClassTeacherSave"));
        }

        try {
            int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
            service.saveFeedback(studentId, feedback);
        } catch (Exception exception) {
            throw new StudentDetailsControllerException("Failed to save feedback.", exception);
        }
    }

    public String loadFeedback(String studentNumber) throws StudentDetailsControllerException {
        try {
            int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
            return service.getFeedback(studentId);
        } catch (Exception exception) {
            throw new StudentDetailsControllerException("Failed to load feedback.", exception);
        }
    }

    public void deleteFeedback(String studentNumber, int teacherUserId, String languageCode)
            throws StudentDetailsControllerException {
        ResourceBundle bundle = getBundle(languageCode);

        var teacher = userProfileDao.findTeacherByUserId(teacherUserId);
        if (teacher == null) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.teacherNotFound"));
        }

        String subject = teacher.subject == null ? "" : teacher.subject.trim();

        if (!isMathSubject(subject)) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.onlyClassTeacherDelete"));
        }

        try {
            int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
            service.deleteFeedback(studentId);
        } catch (Exception exception) {
            throw new StudentDetailsControllerException("Failed to delete feedback.", exception);
        }
    }

    private boolean isMathSubject(String subject) {
        if (subject == null) {
            return false;
        }

        String value = subject.trim().toLowerCase();
        return value.equals("maths")
                || value.contains("mathematics")
                || value.contains("math");
    }
}