package Backend.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GradeServiceTest {

    @Test
    void calculateGrade_shouldReturnCorrectGrades() {
        GradeService s = new GradeService();

        assertEquals("A", s.calculateGrade(80));
        assertEquals("B", s.calculateGrade(70));
        assertEquals("C", s.calculateGrade(60));
        assertEquals("S", s.calculateGrade(40));
        assertEquals("F", s.calculateGrade(10));
    }

    @Test
    void calculateGrade_shouldReturnINVALID_forOutOfRange() {
        GradeService s = new GradeService();

        assertEquals("INVALID", s.calculateGrade(-1));
        assertEquals("INVALID", s.calculateGrade(101));
    }
}
