package backend.model.dto;

import backend.model.entity.Student;
import backend.model.entity.StudentMarks;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentDetailsDTOTest {

    @Test
    void gettersAndSetters_shouldWork() {
        StudentDetailsDTO dto = new StudentDetailsDTO();

        Student student = new Student();
        student.setStudentId(1);
        student.setFirstName("Ali");

        StudentMarks marks = new StudentMarks();
        marks.setTotal(400);
        marks.setAverage(80.0);

        dto.setStudent(student);
        dto.setMarks(marks);

        assertNotNull(dto.getStudent());
        assertNotNull(dto.getMarks());

        assertEquals(1, dto.getStudent().getStudentId());
        assertEquals("Ali", dto.getStudent().getFirstName());

        assertEquals(400, dto.getMarks().getTotal());
        assertEquals(80.0, dto.getMarks().getAverage());
    }

    @Test
    void getters_shouldReturnNull_whenNotSet() {
        StudentDetailsDTO dto = new StudentDetailsDTO();

        assertNull(dto.getStudent());
        assertNull(dto.getMarks());
    }
}