package Backend.service;

import Backend.model.dto.SubjectWeightedMarksDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeightedMarkServiceTest {

    @Test
    void calculateWeightedMark_shouldCalculateCorrectly() {
        WeightedMarkService s = new WeightedMarkService();

        SubjectWeightedMarksDTO dto = new SubjectWeightedMarksDTO();
        dto.setAssignmentMark(80);
        dto.setProjectMark(70);
        dto.setFinalExamMark(60);

        assertEquals(67.0, s.calculateWeightedMark(dto), 0.0001);
    }

    @Test
    void gradeFromWeighted_shouldCoverAllBranches() {
        WeightedMarkService s = new WeightedMarkService();

        assertEquals("A", s.gradeFromWeighted(75));
        assertEquals("B", s.gradeFromWeighted(65));
        assertEquals("C", s.gradeFromWeighted(55));
        assertEquals("S", s.gradeFromWeighted(35));
        assertEquals("F", s.gradeFromWeighted(34.999));
    }
}