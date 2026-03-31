package Backend.controller;

import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.UserProfileDaoImpl;
import Backend.service.StudentDetailsService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDetailsControllerTest {

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);

        Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        long offset = unsafe.objectFieldOffset(f);
        unsafe.putObject(target, offset, value);
    }

    @Test
    void saveFeedback_shouldThrow_whenTeacherProfileNull() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfile = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return null;
            }
        };

        setField(c, "userProfileDao", fakeProfile);

        SecurityException ex = assertThrows(SecurityException.class,
                () -> c.saveFeedback("S001", "x", 99));
        assertEquals("Teacher profile not found.", ex.getMessage());
    }

    @Test
    void saveFeedback_shouldThrow_whenTeacherNotMath() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfile = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "T", "t@x.com", "English");
            }
        };

        setField(c, "userProfileDao", fakeProfile);

        SecurityException ex = assertThrows(SecurityException.class,
                () -> c.saveFeedback("S001", "x", 10));
        assertEquals("Only the class teacher can save feedback.", ex.getMessage());
    }

    @Test
    void saveFeedback_shouldThrow_whenTeacherSubjectNull() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfile = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "T", "t@x.com", null);
            }
        };

        setField(c, "userProfileDao", fakeProfile);

        SecurityException ex = assertThrows(SecurityException.class,
                () -> c.saveFeedback("S001", "x", 10));
        assertEquals("Only the class teacher can save feedback.", ex.getMessage());
    }

    @Test
    void saveFeedback_shouldWork_whenTeacherSubjectIsMathematics() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfileMath = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "T", "t@x.com", "Mathematics");
            }
        };

        StudentDao fakeStudentDao = new StudentDao() {
            @Override
            public int findStudentIdByStudentNumber(String studentNumber) {
                assertEquals("S001", studentNumber);
                return 10;
            }

            @Override public void create(Backend.model.entity.Student student) {}
            @Override public Backend.model.entity.Student findById(int studentId) { return null; }
            @Override public java.util.List<Backend.model.entity.Student> findAll() { return java.util.List.of(); }
            @Override public void update(Backend.model.entity.Student student) {}
            @Override public void delete(int studentId) {}
            @Override public boolean existsById(int studentId) { return false; }
            @Override public Backend.model.entity.Student findByStudentNumber(String studentNumber) { return null; }
            @Override public Backend.model.entity.Student findFirstStudent() { return null; }
            @Override public java.util.List<Backend.model.entity.Student> findAllBasic() { return java.util.List.of(); }
        };

        StudentDetailsService fakeService = new StudentDetailsService() {
            @Override
            public void saveFeedback(int studentId, String feedback) {
                assertEquals(10, studentId);
                assertEquals("Good", feedback);
            }
        };

        setField(c, "userProfileDao", fakeProfileMath);
        setField(c, "studentDao", fakeStudentDao);
        setField(c, "service", fakeService);

        c.saveFeedback("S001", "Good", 10);
    }

    @Test
    void saveFeedback_shouldWork_whenTeacherSubjectIsMaths() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfileMath = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "T", "t@x.com", "Maths");
            }
        };

        StudentDao fakeStudentDao = new StudentDao() {
            @Override
            public int findStudentIdByStudentNumber(String studentNumber) {
                return 20;
            }

            @Override public void create(Backend.model.entity.Student student) {}
            @Override public Backend.model.entity.Student findById(int studentId) { return null; }
            @Override public java.util.List<Backend.model.entity.Student> findAll() { return java.util.List.of(); }
            @Override public void update(Backend.model.entity.Student student) {}
            @Override public void delete(int studentId) {}
            @Override public boolean existsById(int studentId) { return false; }
            @Override public Backend.model.entity.Student findByStudentNumber(String studentNumber) { return null; }
            @Override public Backend.model.entity.Student findFirstStudent() { return null; }
            @Override public java.util.List<Backend.model.entity.Student> findAllBasic() { return java.util.List.of(); }
        };

        StudentDetailsService fakeService = new StudentDetailsService() {
            @Override
            public void saveFeedback(int studentId, String feedback) {
                assertEquals(20, studentId);
            }
        };

        setField(c, "userProfileDao", fakeProfileMath);
        setField(c, "studentDao", fakeStudentDao);
        setField(c, "service", fakeService);

        c.saveFeedback("S001", "Good", 10);
    }

    @Test
    void deleteFeedback_shouldThrow_whenTeacherProfileNull() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfile = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return null;
            }
        };

        setField(c, "userProfileDao", fakeProfile);

        SecurityException ex = assertThrows(SecurityException.class,
                () -> c.deleteFeedback("S001", 99));
        assertEquals("Teacher profile not found.", ex.getMessage());
    }

    @Test
    void deleteFeedback_shouldThrow_whenTeacherSubjectNull() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfile = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "T", "t@x.com", null);
            }
        };

        setField(c, "userProfileDao", fakeProfile);

        SecurityException ex = assertThrows(SecurityException.class,
                () -> c.deleteFeedback("S001", 10));
        assertEquals("Only the class teacher can delete feedback.", ex.getMessage());
    }

    @Test
    void deleteFeedback_shouldThrow_whenTeacherNotMath() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfile = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "T", "t@x.com", "English");
            }
        };

        setField(c, "userProfileDao", fakeProfile);

        SecurityException ex = assertThrows(SecurityException.class,
                () -> c.deleteFeedback("S001", 10));
        assertEquals("Only the class teacher can delete feedback.", ex.getMessage());
    }

    @Test
    void deleteFeedback_shouldWork_whenTeacherSubjectContainsMath() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        UserProfileDaoImpl fakeProfileMath = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "T", "t@x.com", "Math");
            }
        };

        StudentDao fakeStudentDao = new StudentDao() {
            @Override
            public int findStudentIdByStudentNumber(String studentNumber) {
                assertEquals("S001", studentNumber);
                return 10;
            }

            @Override public void create(Backend.model.entity.Student student) {}
            @Override public Backend.model.entity.Student findById(int studentId) { return null; }
            @Override public java.util.List<Backend.model.entity.Student> findAll() { return java.util.List.of(); }
            @Override public void update(Backend.model.entity.Student student) {}
            @Override public void delete(int studentId) {}
            @Override public boolean existsById(int studentId) { return false; }
            @Override public Backend.model.entity.Student findByStudentNumber(String studentNumber) { return null; }
            @Override public Backend.model.entity.Student findFirstStudent() { return null; }
            @Override public java.util.List<Backend.model.entity.Student> findAllBasic() { return java.util.List.of(); }
        };

        StudentDetailsService fakeService = new StudentDetailsService() {
            @Override
            public void deleteFeedback(int studentId) {
                assertEquals(10, studentId);
            }
        };

        setField(c, "userProfileDao", fakeProfileMath);
        setField(c, "studentDao", fakeStudentDao);
        setField(c, "service", fakeService);

        c.deleteFeedback("S001", 10);
    }

    @Test
    void getDetails_and_loadFeedback_shouldCallService() throws Exception {
        StudentDetailsController c = new StudentDetailsController();

        StudentDao fakeStudentDao = new StudentDao() {
            @Override
            public int findStudentIdByStudentNumber(String studentNumber) {
                return 10;
            }

            @Override public void create(Backend.model.entity.Student student) {}
            @Override public Backend.model.entity.Student findById(int studentId) { return null; }
            @Override public java.util.List<Backend.model.entity.Student> findAll() { return java.util.List.of(); }
            @Override public void update(Backend.model.entity.Student student) {}
            @Override public void delete(int studentId) {}
            @Override public boolean existsById(int studentId) { return false; }
            @Override public Backend.model.entity.Student findByStudentNumber(String studentNumber) { return null; }
            @Override public Backend.model.entity.Student findFirstStudent() { return null; }
            @Override public java.util.List<Backend.model.entity.Student> findAllBasic() { return java.util.List.of(); }
        };

        StudentDetailsService fakeService = new StudentDetailsService() {
            @Override
            public Backend.model.dto.StudentDetailsDTO getStudentDetails(int studentId) {
                assertEquals(10, studentId);
                return new Backend.model.dto.StudentDetailsDTO();
            }

            @Override
            public String getFeedback(int studentId) {
                assertEquals(10, studentId);
                return "OK";
            }
        };

        setField(c, "studentDao", fakeStudentDao);
        setField(c, "service", fakeService);

        assertNotNull(c.getDetails("S001"));
        assertEquals("OK", c.loadFeedback("S001"));
    }
}