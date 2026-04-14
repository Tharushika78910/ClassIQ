package backend.controller;

import backend.model.entity.Student;
import backend.service.StudentInfoService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentInfoControllerTest {

    @Test
    void controllerMethods_shouldDelegateToService() throws Exception {
        StudentInfoService fakeService = new StudentInfoService() {
            @Override
            public Student loadFirstStudent(String languageCode) {
                assertEquals("en", languageCode);
                return new Student();
            }

            @Override
            public Student searchByStudentNumber(String studentNumber, String languageCode) {
                assertEquals("S001", studentNumber);
                assertEquals("en", languageCode);
                return new Student();
            }

            @Override
            public List<Student> loadAllStudentsBasic(String languageCode) {
                assertEquals("en", languageCode);
                return List.of(new Student());
            }
        };

        StudentInfoController controller = new StudentInfoController(fakeService);

        assertNotNull(controller.getInitialStudent("en"));
        assertNotNull(controller.findStudentByNumber("S001", "en"));
        assertEquals(1, controller.getAllStudentsBasic("en").size());
    }

    @Test
    void getInitialStudent_shouldPropagateSQLException() {
        StudentInfoService fakeService = new StudentInfoService() {
            @Override
            public Student loadFirstStudent(String languageCode) throws SQLException {
                throw new SQLException("DB error");
            }
        };

        StudentInfoController controller = new StudentInfoController(fakeService);

        SQLException exception = assertThrows(
                SQLException.class,
                () -> controller.getInitialStudent("en")
        );

        assertEquals("DB error", exception.getMessage());
    }

    @Test
    void findStudentByNumber_shouldPropagateSQLException() {
        StudentInfoService fakeService = new StudentInfoService() {
            @Override
            public Student searchByStudentNumber(String studentNumber, String languageCode) throws SQLException {
                throw new SQLException("Search failed");
            }
        };

        StudentInfoController controller = new StudentInfoController(fakeService);

        SQLException exception = assertThrows(
                SQLException.class,
                () -> controller.findStudentByNumber("S001", "en")
        );

        assertEquals("Search failed", exception.getMessage());
    }

    @Test
    void getAllStudentsBasic_shouldPropagateSQLException() {
        StudentInfoService fakeService = new StudentInfoService() {
            @Override
            public List<Student> loadAllStudentsBasic(String languageCode) throws SQLException {
                throw new SQLException("Load failed");
            }
        };

        StudentInfoController controller = new StudentInfoController(fakeService);

        SQLException exception = assertThrows(
                SQLException.class,
                () -> controller.getAllStudentsBasic("en")
        );

        assertEquals("Load failed", exception.getMessage());
    }
}