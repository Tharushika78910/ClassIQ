package Backend.service;

import Backend.model.dao.StudentDao;
import Backend.model.entity.Student;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
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
    void searchByStudentNumber_shouldThrowOnNullOrBlank() throws Exception {
        StudentInfoService s = new StudentInfoService();

        assertThrows(IllegalArgumentException.class, () -> s.searchByStudentNumber(null));
        assertThrows(IllegalArgumentException.class, () -> s.searchByStudentNumber("   "));
    }

    @Test
    void searchByStudentNumber_shouldTrimAndCallDao() throws Exception {
        StudentInfoService s = new StudentInfoService();

        StudentDao fakeDao = new StudentDao() {
            @Override public Student findByStudentNumber(String studentNumber) {
                assertEquals("S001", studentNumber); // trimmed
                return new Student();
            }
            @Override public Student findFirstStudent() { return new Student(); }
            @Override public List<Student> findAllBasic() { return List.of(new Student()); }

            // unused methods (unit test scope)
            @Override public void create(Student student) {}
            @Override public Student findById(int studentId) { return null; }
            @Override public List<Student> findAll() { return List.of(); }
            @Override public void update(Student student) {}
            @Override public void delete(int studentId) {}
            @Override public boolean existsById(int studentId) { return false; }
            @Override public int findStudentIdByStudentNumber(String studentNumber) { return 0; }
        };

        setFinalField(s, "studentDao", fakeDao);

        assertNotNull(s.searchByStudentNumber("  S001  "));
    }

    @Test
    void loadFirstStudent_and_loadAllStudentsBasic_shouldCallDao() throws Exception {
        StudentInfoService s = new StudentInfoService();

        StudentDao fakeDao = new StudentDao() {
            @Override public Student findFirstStudent() { return new Student(); }
            @Override public List<Student> findAllBasic() { return List.of(new Student(), new Student()); }

            @Override public Student findByStudentNumber(String studentNumber) { return null; }
            @Override public void create(Student student) {}
            @Override public Student findById(int studentId) { return null; }
            @Override public List<Student> findAll() { return List.of(); }
            @Override public void update(Student student) {}
            @Override public void delete(int studentId) {}
            @Override public boolean existsById(int studentId) { return false; }
            @Override public int findStudentIdByStudentNumber(String studentNumber) { return 0; }
        };

        setFinalField(s, "studentDao", fakeDao);

        assertNotNull(s.loadFirstStudent());
        assertEquals(2, s.loadAllStudentsBasic().size());
    }
}