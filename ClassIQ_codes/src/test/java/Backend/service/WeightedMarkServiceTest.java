package Backend.service;

import Backend.model.dto.SubjectWeightedMarksDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeightedMarkServiceTest {

    @Test
    void calculateWeightedMark_shouldApplyCorrectWeights() {
        WeightedMarkService service = new WeightedMarkService();

        SubjectWeightedMarksDTO dto = new SubjectWeightedMarksDTO();
        dto.setAssignmentMark(80);
        dto.setProjectMark(70);
        dto.setFinalExamMark(90);

        // 80*0.20 = 16
        // 70*0.30 = 21
        // 90*0.50 = 45
        // total = 82
        double weighted = service.calculateWeightedMark(dto);

        assertEquals(82.0, weighted, 0.0001);
    }

    @Test
    void gradeFromWeighted_shouldReturnCorrectGradeBoundaries() {
        WeightedMarkService service = new WeightedMarkService();

        assertEquals("A", service.gradeFromWeighted(75));
        assertEquals("A", service.gradeFromWeighted(100));

        assertEquals("B", service.gradeFromWeighted(65));
        assertEquals("B", service.gradeFromWeighted(74.999));

        assertEquals("C", service.gradeFromWeighted(55));
        assertEquals("C", service.gradeFromWeighted(64.999));

        assertEquals("S", service.gradeFromWeighted(35));
        assertEquals("S", service.gradeFromWeighted(54.999));

        assertEquals("F", service.gradeFromWeighted(34.999));
        assertEquals("F", service.gradeFromWeighted(0));
    }
}
