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

    private static final String FEEDBACK_SUBJECT = "Mathematics";

    public StudentDetailsDTO getDetails(String studentNumber) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        return service.getStudentDetails(studentId);
    }


    public void saveFeedback(String studentNumber, String feedback, int teacherUserId) throws Exception {

        // --- Authorization check (server-side enforcement)
        var teacher = userProfileDao.findTeacherByUserId(teacherUserId);
        if (teacher == null) {
            throw new SecurityException("Teacher profile not found.");
        }

        String subject = teacher.subject == null ? "" : teacher.subject.trim();
        if (!FEEDBACK_SUBJECT.equalsIgnoreCase(subject)) {
            throw new SecurityException("Only the Mathematics teacher can save feedback.");
        }

        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        service.saveFeedback(studentId, feedback);
    }

    public String loadFeedback(String studentNumber) throws Exception {
        int studentId = studentDao.findStudentIdByStudentNumber(studentNumber);
        return service.getFeedback(studentId);
    }
}