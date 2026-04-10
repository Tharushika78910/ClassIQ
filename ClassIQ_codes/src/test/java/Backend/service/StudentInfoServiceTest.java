package Backend.service;

import Backend.model.dao.StudentDao;
import Backend.model.entity.Student;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentInfoServiceTest {

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
    void searchByStudentNumber_shouldThrowOnNullOrBlank() {
        StudentInfoService s = new StudentInfoService();

        assertThrows(IllegalArgumentException.class, () -> s.searchByStudentNumber(null, "en"));
        assertThrows(IllegalArgumentException.class, () -> s.searchByStudentNumber("", "en"));
        assertThrows(IllegalArgumentException.class, () -> s.searchByStudentNumber("   ", "en"));
    }

    @Test
    void searchByStudentNumber_shouldTrimAndCallDao() throws Exception {
        StudentInfoService s = new StudentInfoService();

        StudentDao fakeDao = new StudentDao() {
            @Override
            public Student findByStudentNumber(String studentNumber, String languageCode) {
                assertEquals("S001", studentNumber);
                assertEquals("en", languageCode);
                return new Student();
            }

            @Override public Student findFirstStudent() { return null; }
            @Override public Student findFirstStudent(String languageCode) { return null; }
            @Override public List<Student> findAllBasic() { return List.of(); }
            @Override public List<Student> findAllBasic(String languageCode) { return List.of(); }
            @Override public void create(Student student) {}
            @Override public Student findById(int studentId) { return null; }
            @Override public Student findById(int studentId, String languageCode) { return null; }
            @Override public List<Student> findAll() { return List.of(); }
            @Override public List<Student> findAll(String languageCode) { return List.of(); }
            @Override public void update(Student student) {}
            @Override public void delete(int studentId) {}
            @Override public boolean existsById(int studentId) { return false; }
            @Override public Student findByStudentNumber(String studentNumber) { return null; }
            @Override public int findStudentIdByStudentNumber(String studentNumber) { return 0; }
        };

        setFinalField(s, "studentDao", fakeDao);

        assertNotNull(s.searchByStudentNumber("  S001  ", "en"));
    }

    @Test
    void loadFirstStudent_and_loadAllStudentsBasic_shouldCallDao() throws Exception {
        StudentInfoService s = new StudentInfoService();

        StudentDao fakeDao = new StudentDao() {
            @Override
            public Student findFirstStudent(String languageCode) {
                assertEquals("en", languageCode);
                return new Student();
            }

            @Override
            public List<Student> findAllBasic(String languageCode) {
                assertEquals("en", languageCode);
                return List.of(new Student(), new Student());
            }

            @Override public Student findByStudentNumber(String studentNumber) { return null; }
            @Override public Student findByStudentNumber(String studentNumber, String languageCode) { return null; }
            @Override public void create(Student student) {}
            @Override public Student findById(int studentId) { return null; }
            @Override public Student findById(int studentId, String languageCode) { return null; }
            @Override public List<Student> findAll() { return List.of(); }
            @Override public List<Student> findAll(String languageCode) { return List.of(); }
            @Override public void update(Student student) {}
            @Override public void delete(int studentId) {}
            @Override public boolean existsById(int studentId) { return false; }
            @Override public int findStudentIdByStudentNumber(String studentNumber) { return 0; }
            @Override public Student findFirstStudent() { return null; }
            @Override public List<Student> findAllBasic() { return List.of(); }
        };

        setFinalField(s, "studentDao", fakeDao);

        assertNotNull(s.loadFirstStudent("en"));
        assertEquals(2, s.loadAllStudentsBasic("en").size());
    }
}