package Backend.controller;

import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.dao.impl.UserProfileDaoImpl;
import Backend.model.dto.StudentDetailsDTO;
import Backend.service.StudentDetailsService;

import java.util.Locale;
import java.util.ResourceBundle;

public class StudentDetailsController {

    private final StudentDetailsService service = new StudentDetailsService();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final UserProfileDaoImpl userProfileDao = new UserProfileDaoImpl();

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

        String subject = (teacher.subject == null) ? "" : teacher.subject.trim();

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

        String subject = (teacher.subject == null) ? "" : teacher.subject.trim();

        if (!isMathSubject(subject)) {
            throw new SecurityException(bundle.getString("teacher.studentDetails.error.onlyClassTeacherDelete"));
        }

        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        service.deleteFeedback(studentId);
    }

    private boolean isMathSubject(String subject) {
        if (subject == null) return false;
        String s = subject.trim().toLowerCase();
        return s.equals("maths")
                || s.contains("mathematics")
                || s.contains("math");
    }
}