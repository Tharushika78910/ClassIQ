package backend.service;

import backend.model.dao.MarksDao;
import backend.model.dto.StudentDetailsDTO;
import backend.model.entity.StudentMarks;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentDetailsServiceTest {

    @Test
    void getStudentDetails_saveFeedback_getFeedback_deleteFeedback_shouldCallDao() throws Exception {
        MarksDao fakeDao = new MarksDao() {
            @Override
            public StudentDetailsDTO findStudentDetails(int studentId, String languageCode) {
                assertEquals(10, studentId);
                assertEquals("en", languageCode);
                return new StudentDetailsDTO();
            }

            @Override
            public void saveOrUpdateFeedback(int studentId, String feedback) {
                assertEquals(10, studentId);
                assertEquals("Nice", feedback);
            }

            @Override
            public String findFeedback(int studentId) {
                assertEquals(10, studentId);
                return "Saved";
            }

            @Override
            public void deleteFeedback(int studentId) {
                assertEquals(10, studentId);
            }

            @Override
            public void saveMarks(StudentMarks marks) {
                throw new UnsupportedOperationException("Not needed for this test");
            }

            @Override
            public StudentMarks findByStudentId(int studentId) {
                return null;
            }
        };

        StudentDetailsService service = new StudentDetailsService(fakeDao);

        assertNotNull(service.getStudentDetails(10, "en"));
        service.saveFeedback(10, "Nice");
        assertEquals("Saved", service.getFeedback(10));
        service.deleteFeedback(10);
    }

    @Test
    void getStudentDetails_shouldWrapException() {
        MarksDao fakeDao = new MarksDao() {
            @Override
            public StudentDetailsDTO findStudentDetails(int studentId, String languageCode) {
                throw new RuntimeException("DAO failure");
            }

            @Override
            public void saveOrUpdateFeedback(int studentId, String feedback) {
            }

            @Override
            public String findFeedback(int studentId) {
                return null;
            }

            @Override
            public void deleteFeedback(int studentId) {
            }

            @Override
            public void saveMarks(StudentMarks marks) {
            }

            @Override
            public StudentMarks findByStudentId(int studentId) {
                return null;
            }
        };

        StudentDetailsService service = new StudentDetailsService(fakeDao);

        StudentDetailsServiceException exception = assertThrows(
                StudentDetailsServiceException.class,
                () -> service.getStudentDetails(10, "en")
        );

        assertEquals("Failed to load student details.", exception.getMessage());
        assertNotNull(exception.getCause());
    }

    @Test
    void saveFeedback_shouldWrapException() {
        MarksDao fakeDao = new MarksDao() {
            @Override
            public StudentDetailsDTO findStudentDetails(int studentId, String languageCode) {
                return null;
            }

            @Override
            public void saveOrUpdateFeedback(int studentId, String feedback) {
                throw new RuntimeException("DAO failure");
            }

            @Override
            public String findFeedback(int studentId) {
                return null;
            }

            @Override
            public void deleteFeedback(int studentId) {
            }

            @Override
            public void saveMarks(StudentMarks marks) {
            }

            @Override
            public StudentMarks findByStudentId(int studentId) {
                return null;
            }
        };

        StudentDetailsService service = new StudentDetailsService(fakeDao);

        StudentDetailsServiceException exception = assertThrows(
                StudentDetailsServiceException.class,
                () -> service.saveFeedback(10, "Nice")
        );

        assertEquals("Failed to save feedback.", exception.getMessage());
        assertNotNull(exception.getCause());
    }

    @Test
    void getFeedback_shouldWrapException() {
        MarksDao fakeDao = new MarksDao() {
            @Override
            public StudentDetailsDTO findStudentDetails(int studentId, String languageCode) {
                return null;
            }

            @Override
            public void saveOrUpdateFeedback(int studentId, String feedback) {
            }

            @Override
            public String findFeedback(int studentId) {
                throw new RuntimeException("DAO failure");
            }

            @Override
            public void deleteFeedback(int studentId) {
            }

            @Override
            public void saveMarks(StudentMarks marks) {
            }

            @Override
            public StudentMarks findByStudentId(int studentId) {
                return null;
            }
        };

        StudentDetailsService service = new StudentDetailsService(fakeDao);

        StudentDetailsServiceException exception = assertThrows(
                StudentDetailsServiceException.class,
                () -> service.getFeedback(10)
        );

        assertEquals("Failed to load feedback.", exception.getMessage());
        assertNotNull(exception.getCause());
    }

    @Test
    void deleteFeedback_shouldWrapException() {
        MarksDao fakeDao = new MarksDao() {
            @Override
            public StudentDetailsDTO findStudentDetails(int studentId, String languageCode) {
                return null;
            }

            @Override
            public void saveOrUpdateFeedback(int studentId, String feedback) {
            }

            @Override
            public String findFeedback(int studentId) {
                return null;
            }

            @Override
            public void deleteFeedback(int studentId) {
                throw new RuntimeException("DAO failure");
            }

            @Override
            public void saveMarks(StudentMarks marks) {
            }

            @Override
            public StudentMarks findByStudentId(int studentId) {
                return null;
            }
        };

        StudentDetailsService service = new StudentDetailsService(fakeDao);

        StudentDetailsServiceException exception = assertThrows(
                StudentDetailsServiceException.class,
                () -> service.deleteFeedback(10)
        );

        assertEquals("Failed to delete feedback.", exception.getMessage());
        assertNotNull(exception.getCause());
    }
}