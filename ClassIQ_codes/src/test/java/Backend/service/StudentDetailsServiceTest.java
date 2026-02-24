package Backend.service;

import Backend.model.dao.MarksDao;
import Backend.model.dto.StudentDetailsDTO;
import Backend.model.entity.StudentMarks;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDetailsServiceTest {

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);

        Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        long offset = unsafe.objectFieldOffset(f);
        unsafe.putObject(target, offset, value);
    }

    @Test
    void getStudentDetails_saveFeedback_getFeedback_shouldCallDao() throws Exception {
        StudentDetailsService s = new StudentDetailsService();

        MarksDao fakeDao = new MarksDao() {
            @Override public StudentDetailsDTO findStudentDetails(int studentId) {
                assertEquals(10, studentId);
                return new StudentDetailsDTO();
            }
            @Override public void saveOrUpdateFeedback(int studentId, String feedback) {
                assertEquals(10, studentId);
                assertEquals("Nice", feedback);
            }
            @Override public String findFeedback(int studentId) {
                assertEquals(10, studentId);
                return "Saved";
            }

            // unused
            @Override public void saveMarks(StudentMarks marks) {}
            @Override public StudentMarks findByStudentId(int studentId) { return null; }
        };

        setFinalField(s, "marksDao", fakeDao);

        assertNotNull(s.getStudentDetails(10));
        s.saveFeedback(10, "Nice");
        assertEquals("Saved", s.getFeedback(10));
    }
}