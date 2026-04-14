package backend.service;

import backend.model.dto.SubjectWeightedMarksDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeightedMarkServiceTest {

    @Test
    void calculateWeightedMark_shouldCalculateCorrectly() {
        WeightedMarkService service = new WeightedMarkService();

        SubjectWeightedMarksDTO dto = new SubjectWeightedMarksDTO();
        dto.setAssignmentMark(80);
        dto.setProjectMark(70);
        dto.setFinalExamMark(60);

        assertEquals(67.0, service.calculateWeightedMark(dto), 0.0001);
    }

    @Test
    void gradeFromWeighted_shouldCoverAllBranches() {
        WeightedMarkService service = new WeightedMarkService();

        assertEquals("A", service.gradeFromWeighted(75));
        assertEquals("B", service.gradeFromWeighted(65));
        assertEquals("C", service.gradeFromWeighted(55));
        assertEquals("S", service.gradeFromWeighted(35));
        assertEquals("F", service.gradeFromWeighted(34.999));
    }

    @Test
    void gradeFromComponents_shouldCoverAllBranches() {
        WeightedMarkService service = new WeightedMarkService();

        assertEquals("A", service.gradeFromComponents(25, 25, 25));
        assertEquals("B", service.gradeFromComponents(20, 20, 25));
        assertEquals("C", service.gradeFromComponents(20, 15, 20));
        assertEquals("S", service.gradeFromComponents(10, 10, 15));
        assertEquals("F", service.gradeFromComponents(10, 10, 10));
    }
}