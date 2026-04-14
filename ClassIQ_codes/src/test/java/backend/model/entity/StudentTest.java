package backend.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void gettersAndSetters_shouldWork() {
        Student student = new Student();

        student.setStudentId(1);
        student.setFirstName("Ali");
        student.setLastName("Khan");
        student.setStudentNumber("S001");
        student.setEmail("ali@test.com");
        student.setUserId(10);

        assertEquals(1, student.getStudentId());
        assertEquals("Ali", student.getFirstName());
        assertEquals("Khan", student.getLastName());
        assertEquals("S001", student.getStudentNumber());
        assertEquals("ali@test.com", student.getEmail());
        assertEquals(10, student.getUserId());
    }

    @Test
    void getFullName_shouldReturnFullName_whenNamesPresent() {
        Student student = new Student();
        student.setFirstName("Ali");
        student.setLastName("Khan");

        assertEquals("Ali Khan", student.getFullName());
    }

    @Test
    void getFullName_shouldHandleNullValues() {
        Student student = new Student();

        // both null
        assertEquals(" ", student.getFullName());

        // only first name
        student.setFirstName("Ali");
        student.setLastName(null);
        assertEquals("Ali ", student.getFullName());

        // only last name
        student.setFirstName(null);
        student.setLastName("Khan");
        assertEquals(" Khan", student.getFullName());
    }
}