package Backend.service;

import Backend.model.dto.SubjectWeightedMarksDTO;

public class WeightedMarkService {

    public double calculateWeightedMark(SubjectWeightedMarksDTO dto) {
        // 20% assignment, 30% project, 50% final exam
        return dto.getAssignmentMark() * 0.20
                + dto.getProjectMark() * 0.30
                + dto.getFinalExamMark() * 0.50;
    }

    public String gradeFromWeighted(double weighted) {
        if (weighted >= 75) return "A";
        if (weighted >= 65) return "B";
        if (weighted >= 55) return "C";
        if (weighted >= 35) return "S";
        return "F";
    }
}

