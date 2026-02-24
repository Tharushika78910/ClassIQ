package Backend.controller;

import Backend.model.entity.Student;
import Backend.service.StudentInfoService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
            @Override public Student loadFirstStudent() { return new Student(); }
            @Override public Student searchByStudentNumber(String studentNumber) { return new Student(); }
            @Override public List<Student> loadAllStudentsBasic() { return List.of(new Student()); }
        };

        setFinalField(c, "service", fakeService);

        assertNotNull(c.getInitialStudent());
        assertNotNull(c.findStudentByNumber("S001"));
        assertEquals(1, c.getAllStudentsBasic().size());
    }
}