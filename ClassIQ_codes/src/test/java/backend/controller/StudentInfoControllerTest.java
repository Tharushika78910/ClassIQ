package backend.controller;

import backend.model.entity.Student;
import backend.service.StudentInfoService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}