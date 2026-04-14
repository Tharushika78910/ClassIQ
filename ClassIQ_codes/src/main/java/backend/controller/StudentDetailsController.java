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

    public StudentDetailsDTO getDetails(String studentNumber, String languageCode) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        return service.getStudentDetails(studentId, languageCode);
    }

    public void saveFeedback(String studentNumber, String feedback, int teacherUserId, String languageCode) throws Exception {
        ResourceBundle bundle = getBundle(languageCode);

        var teacher = userProfileDao.findTeacherByUserId(teacherUserId);
        if (teacher == null) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.teacherNotFound"));
        }

        String subject = teacher.subject == null ? "" : teacher.subject.trim();

        if (!isMathSubject(subject)) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.onlyClassTeacherSave"));
        }

        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        service.saveFeedback(studentId, feedback);
    }

    public String loadFeedback(String studentNumber) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        return service.getFeedback(studentId);
    }

    public void deleteFeedback(String studentNumber, int teacherUserId, String languageCode) throws Exception {
        ResourceBundle bundle = getBundle(languageCode);

        var teacher = userProfileDao.findTeacherByUserId(teacherUserId);
        if (teacher == null) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.teacherNotFound"));
        }

        String subject = teacher.subject == null ? "" : teacher.subject.trim();

        if (!isMathSubject(subject)) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.onlyClassTeacherDelete"));
        }

        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        service.deleteFeedback(studentId);
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