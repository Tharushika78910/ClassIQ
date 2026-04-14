package backend.service;

import backend.model.dao.StudentDao;
import backend.model.entity.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentInfoServiceTest {

    @Test
    void searchByStudentNumber_shouldThrowOnNullOrBlank() {
        StudentInfoService service = new StudentInfoService();

        assertThrows(IllegalArgumentException.class, () -> service.searchByStudentNumber(null, "en"));
        assertThrows(IllegalArgumentException.class, () -> service.searchByStudentNumber("", "en"));
        assertThrows(IllegalArgumentException.class, () -> service.searchByStudentNumber("   ", "en"));
    }

    @Test
    void searchByStudentNumber_shouldTrimAndCallDao() throws Exception {
        StudentDao fakeDao = new StudentDao() {
            @Override
            public Student findByStudentNumber(String studentNumber, String languageCode) {
                assertEquals("S001", studentNumber);
                assertEquals("en", languageCode);
                return new Student();
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
            public int findStudentIdByStudentNumber(String studentNumber) {
                return 0;
            }
        };

        StudentInfoService service = new StudentInfoService(fakeDao);

        assertNotNull(service.searchByStudentNumber("  S001  ", "en"));
    }

    @Test
    void loadFirstStudent_and_loadAllStudentsBasic_shouldCallDao() throws Exception {
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

            @Override
            public Student findByStudentNumber(String studentNumber) {
                return null;
            }

            @Override
            public Student findByStudentNumber(String studentNumber, String languageCode) {
                return null;
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
            public int findStudentIdByStudentNumber(String studentNumber) {
                return 0;
            }

            @Override
            public Student findFirstStudent() {
                return null;
            }

            @Override
            public List<Student> findAllBasic() {
                return List.of();
            }
        };

        StudentInfoService service = new StudentInfoService(fakeDao);

        assertNotNull(service.loadFirstStudent("en"));
        assertEquals(2, service.loadAllStudentsBasic("en").size());
    }
}