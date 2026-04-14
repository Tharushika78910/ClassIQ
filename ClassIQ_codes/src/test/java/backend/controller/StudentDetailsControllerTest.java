package backend.controller;

import backend.model.dao.StudentDao;
import backend.model.dao.impl.UserProfileDaoImpl;
import backend.model.dto.StudentDetailsDTO;
import backend.model.entity.Student;
import backend.service.StudentDetailsService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentDetailsControllerTest {

    private StudentDao createFakeStudentDao(int studentId) {
        return new StudentDao() {
            @Override
            public int findStudentIdByStudentNumber(String studentNumber) {
                assertEquals("S001", studentNumber);
                return studentId;
            }

            @Override
            public void create(Student student) {
                throw new UnsupportedOperationException("Not needed for this test");
            }

            @Override
            public Student findById(int studentId) {
                return null;
            }

            @Override
            public Student findById(int studentId, String languageCode) {
                return null;
            }

            @Override
            public List<Student> findAll() {
                return List.of();
            }

            @Override
            public List<Student> findAll(String languageCode) {
                return List.of();
            }

            @Override
            public void update(Student student) {
                throw new UnsupportedOperationException("Not needed for this test");
            }

            @Override
            public void delete(int studentId) {
                throw new UnsupportedOperationException("Not needed for this test");
            }

            @Override
            public boolean existsById(int studentId) {
                return false;
            }

            @Override
            public Student findByStudentNumber(String studentNumber) {
                return null;
            }

            @Override
            public Student findByStudentNumber(String studentNumber, String languageCode) {
                return null;
            }

            @Override
            public Student findFirstStudent() {
                return null;
            }

            @Override
            public Student findFirstStudent(String languageCode) {
                return null;
            }

            @Override
            public List<Student> findAllBasic() {
                return List.of();
            }

            @Override
            public List<Student> findAllBasic(String languageCode) {
                return List.of();
            }
        };
    }

    @Test
    void saveFeedback_shouldThrow_whenTeacherProfileNull() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return null;
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                new StudentDetailsService(),
                new StudentDao() {
                    @Override public int findStudentIdByStudentNumber(String studentNumber) { return 0; }
                    @Override public void create(Student student) { throw new UnsupportedOperationException("Not needed for this test"); }
                    @Override public Student findById(int studentId) { return null; }
                    @Override public Student findById(int studentId, String languageCode) { return null; }
                    @Override public List<Student> findAll() { return List.of(); }
                    @Override public List<Student> findAll(String languageCode) { return List.of(); }
                    @Override public void update(Student student) { throw new UnsupportedOperationException("Not needed for this test"); }
                    @Override public void delete(int studentId) { throw new UnsupportedOperationException("Not needed for this test"); }
                    @Override public boolean existsById(int studentId) { return false; }
                    @Override public Student findByStudentNumber(String studentNumber) { return null; }
                    @Override public Student findByStudentNumber(String studentNumber, String languageCode) { return null; }
                    @Override public Student findFirstStudent() { return null; }
                    @Override public Student findFirstStudent(String languageCode) { return null; }
                    @Override public List<Student> findAllBasic() { return List.of(); }
                    @Override public List<Student> findAllBasic(String languageCode) { return List.of(); }
                },
                fakeProfileDao
        );

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> controller.saveFeedback("S001", "Good", 99, "en")
        );

        assertEquals("Teacher profile not found.", exception.getMessage());
    }

    @Test
    void saveFeedback_shouldThrow_whenTeacherNotMath() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "Teacher", "teacher@test.com", "English");
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                new StudentDetailsService(),
                createFakeStudentDao(10),
                fakeProfileDao
        );

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> controller.saveFeedback("S001", "Good", 10, "en")
        );

        assertEquals("Only the class teacher can save feedback.", exception.getMessage());
    }

    @Test
    void saveFeedback_shouldThrow_whenTeacherSubjectNull() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "Teacher", "teacher@test.com", null);
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                new StudentDetailsService(),
                createFakeStudentDao(10),
                fakeProfileDao
        );

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> controller.saveFeedback("S001", "Good", 10, "en")
        );

        assertEquals("Only the class teacher can save feedback.", exception.getMessage());
    }

    @Test
    void saveFeedback_shouldWork_whenTeacherSubjectIsMathematics() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "Teacher", "teacher@test.com", "Mathematics");
            }
        };

        StudentDao fakeStudentDao = createFakeStudentDao(10);

        StudentDetailsService fakeService = new StudentDetailsService() {
            @Override
            public void saveFeedback(int studentId, String feedback) {
                assertEquals(10, studentId);
                assertEquals("Good", feedback);
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                fakeService,
                fakeStudentDao,
                fakeProfileDao
        );

        assertDoesNotThrow(() -> controller.saveFeedback("S001", "Good", 10, "en"));
    }

    @Test
    void saveFeedback_shouldWork_whenTeacherSubjectIsMaths() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "Teacher", "teacher@test.com", "Maths");
            }
        };

        StudentDao fakeStudentDao = createFakeStudentDao(20);

        StudentDetailsService fakeService = new StudentDetailsService() {
            @Override
            public void saveFeedback(int studentId, String feedback) {
                assertEquals(20, studentId);
                assertEquals("Well done", feedback);
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                fakeService,
                fakeStudentDao,
                fakeProfileDao
        );

        assertDoesNotThrow(() -> controller.saveFeedback("S001", "Well done", 10, "en"));
    }

    @Test
    void deleteFeedback_shouldThrow_whenTeacherProfileNull() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return null;
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                new StudentDetailsService(),
                createFakeStudentDao(10),
                fakeProfileDao
        );

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> controller.deleteFeedback("S001", 99, "en")
        );

        assertEquals("Teacher profile not found.", exception.getMessage());
    }

    @Test
    void deleteFeedback_shouldThrow_whenTeacherSubjectNull() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "Teacher", "teacher@test.com", null);
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                new StudentDetailsService(),
                createFakeStudentDao(10),
                fakeProfileDao
        );

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> controller.deleteFeedback("S001", 10, "en")
        );

        assertEquals("Only the class teacher can delete feedback.", exception.getMessage());
    }

    @Test
    void deleteFeedback_shouldThrow_whenTeacherNotMath() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "Teacher", "teacher@test.com", "Science");
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                new StudentDetailsService(),
                createFakeStudentDao(10),
                fakeProfileDao
        );

        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> controller.deleteFeedback("S001", 10, "en")
        );

        assertEquals("Only the class teacher can delete feedback.", exception.getMessage());
    }

    @Test
    void deleteFeedback_shouldWork_whenTeacherSubjectContainsMath() {
        UserProfileDaoImpl fakeProfileDao = new UserProfileDaoImpl() {
            @Override
            public TeacherProfile findTeacherByUserId(int userId) {
                return new TeacherProfile(1, "Teacher", "teacher@test.com", "Math");
            }
        };

        StudentDao fakeStudentDao = createFakeStudentDao(30);

        StudentDetailsService fakeService = new StudentDetailsService() {
            @Override
            public void deleteFeedback(int studentId) {
                assertEquals(30, studentId);
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                fakeService,
                fakeStudentDao,
                fakeProfileDao
        );

        assertDoesNotThrow(() -> controller.deleteFeedback("S001", 10, "en"));
    }

    @Test
    void getDetails_and_loadFeedback_shouldCallService() throws Exception {
        StudentDao fakeStudentDao = createFakeStudentDao(40);

        StudentDetailsService fakeService = new StudentDetailsService() {
            @Override
            public StudentDetailsDTO getStudentDetails(int studentId, String languageCode) {
                assertEquals(40, studentId);
                assertEquals("si", languageCode);
                return new StudentDetailsDTO();
            }

            @Override
            public String getFeedback(int studentId) {
                assertEquals(40, studentId);
                return "OK";
            }
        };

        StudentDetailsController controller = new StudentDetailsController(
                fakeService,
                fakeStudentDao,
                new UserProfileDaoImpl()
        );

        assertNotNull(controller.getDetails("S001", "si"));
        assertEquals("OK", controller.loadFeedback("S001"));
    }
}