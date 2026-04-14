package backend.service;

import backend.model.dao.MarksDao;
import backend.model.dto.StudentDetailsDTO;
import backend.model.entity.StudentMarks;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}