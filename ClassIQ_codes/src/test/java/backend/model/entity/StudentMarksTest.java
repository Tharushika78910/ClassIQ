package backend.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentMarksTest {

    @Test
    void gettersAndSetters_shouldWork() {
        StudentMarks marks = new StudentMarks();

        marks.setMarksId(1);
        marks.setStudentId(100);

        marks.setSubject1(80);
        marks.setSubject2(75);
        marks.setSubject3(90);
        marks.setSubject4(85);
        marks.setSubject5(70);

        marks.setTotal(400);
        marks.setAverage(80.0);

        marks.setFeedback("Good performance");

        assertEquals(1, marks.getMarksId());
        assertEquals(100, marks.getStudentId());

        assertEquals(80, marks.getSubject1());
        assertEquals(75, marks.getSubject2());
        assertEquals(90, marks.getSubject3());
        assertEquals(85, marks.getSubject4());
        assertEquals(70, marks.getSubject5());

        assertEquals(400, marks.getTotal());
        assertEquals(80.0, marks.getAverage());

        assertEquals("Good performance", marks.getFeedback());
    }

    @Test
    void defaultConstructor_shouldInitializeObject() {
        StudentMarks marks = new StudentMarks();

        assertNotNull(marks);
        assertEquals(0, marks.getMarksId());
        assertEquals(0, marks.getStudentId());
        assertEquals(0, marks.getSubject1());
        assertEquals(0, marks.getSubject2());
        assertEquals(0, marks.getSubject3());
        assertEquals(0, marks.getSubject4());
        assertEquals(0, marks.getSubject5());
        assertEquals(0, marks.getTotal());
        assertEquals(0.0, marks.getAverage());
        assertNull(marks.getFeedback());
    }
}