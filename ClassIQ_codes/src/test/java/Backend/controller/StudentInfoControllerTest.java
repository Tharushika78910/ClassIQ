package Backend.controller;

import Backend.model.entity.Student;
import Backend.service.StudentInfoService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudentInfoControllerTest {

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
    void controllerMethods_shouldDelegateToService() throws Exception {
        StudentInfoController c = new StudentInfoController();

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

        setFinalField(c, "service", fakeService);

        assertNotNull(c.getInitialStudent("en"));
        assertNotNull(c.findStudentByNumber("S001", "en"));
        assertEquals(1, c.getAllStudentsBasic("en").size());
    }
}