package Backend.service;

import Backend.model.entity.StudentMarks;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GradeServiceTest {

    @Test
    void calculateGrade_shouldReturnCorrectGrades() {
        GradeService service = new GradeService();

        assertEquals("A", service.calculateGrade(75));
        assertEquals("B", service.calculateGrade(65));
        assertEquals("C", service.calculateGrade(55));
        assertEquals("S", service.calculateGrade(35));
        assertEquals("F", service.calculateGrade(0));

        assertEquals("INVALID", service.calculateGrade(-1));
        assertEquals("INVALID", service.calculateGrade(101));
    }

    @Test
    void calculateGradesPerSubject_shouldReturnGradesForAllSubjects() {
        GradeService service = new GradeService();

        StudentMarks marks = new StudentMarks();
        marks.setSubject1(80); // A
        marks.setSubject2(70); // B
        marks.setSubject3(60); // C
        marks.setSubject4(40); // S
        marks.setSubject5(10); // F

        Map<String, String> result = service.calculateGradesPerSubject(marks);

        assertEquals(5, result.size());
        assertEquals("A", result.get("subject1"));
        assertEquals("B", result.get("subject2"));
        assertEquals("C", result.get("subject3"));
        assertEquals("S", result.get("subject4"));
        assertEquals("F", result.get("subject5"));
    }
}
