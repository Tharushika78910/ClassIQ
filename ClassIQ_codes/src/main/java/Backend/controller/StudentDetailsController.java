package Backend.controller;

import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.dao.impl.UserProfileDaoImpl;
import Backend.model.dto.StudentDetailsDTO;
import Backend.service.StudentDetailsService;

public class StudentDetailsController {

    private final StudentDetailsService service = new StudentDetailsService();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final UserProfileDaoImpl userProfileDao = new UserProfileDaoImpl();

    public StudentDetailsDTO getDetails(String studentNumber) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        return service.getStudentDetails(studentId);
    }

    public void saveFeedback(String studentNumber, String feedback, int teacherUserId) throws Exception {

        // Server-side enforcement: only Maths teacher can save
        var teacher = userProfileDao.findTeacherByUserId(teacherUserId);
        if (teacher == null) {
            throw new SecurityException("Teacher profile not found.");
        }

        String subject = (teacher.subject == null) ? "" : teacher.subject.trim();

        if (!isMathSubject(subject)) {
            throw new SecurityException("Only the class teacher can save feedback.");
        }

        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        service.saveFeedback(studentId, feedback);
    }

    public String loadFeedback(String studentNumber) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        return service.getFeedback(studentId);
    }

    //  delete feedback with same Maths-only rule
    public void deleteFeedback(String studentNumber, int teacherUserId) throws Exception {

        var teacher = userProfileDao.findTeacherByUserId(teacherUserId);
        if (teacher == null) {
            throw new SecurityException("Teacher profile not found.");
        }

        String subject = (teacher.subject == null) ? "" : teacher.subject.trim();

        if (!isMathSubject(subject)) {
            throw new SecurityException("Only the class teacher can delete feedback.");
        }

        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        service.deleteFeedback(studentId);
    }

    // Accept Mathematics / Math / Maths (and variants)
    private boolean isMathSubject(String subject) {
        if (subject == null) return false;
        String s = subject.trim().toLowerCase();
        return s.equals("maths")
                || s.contains("mathematics") || s.contains("math");
    }
}