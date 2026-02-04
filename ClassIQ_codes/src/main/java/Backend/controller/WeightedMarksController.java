package Backend.controller;

import Backend.model.dao.StudentDao;
import Backend.model.dao.WeightedMarksDao;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.dao.impl.WeightedMarksDaoImpl;
import Backend.model.dto.SubjectWeightedMarksDTO;
import Backend.service.WeightedMarkService;

import java.sql.SQLException;

public class WeightedMarksController {

    private final StudentDao studentDao = new StudentDaoImpl();
    private final WeightedMarksDao weightedMarksDao = new WeightedMarksDaoImpl();
    private final WeightedMarkService weightedMarkService = new WeightedMarkService();

    public void submitSubjectComponents(SubjectWeightedMarksDTO dto) throws SQLException {

        if (!studentDao.existsById(dto.getStudentId())) {
            throw new IllegalArgumentException("Student ID not found: " + dto.getStudentId());
        }

        double weighted = weightedMarkService.calculateWeightedMark(dto);
        String grade = weightedMarkService.gradeFromWeighted(weighted);

        weightedMarksDao.upsertSubjectWeightedMarks(
                dto.getStudentId(),
                dto.getSubjectNo(),
                dto.getAssignmentMark(),
                dto.getProjectMark(),
                dto.getFinalExamMark(),
                weighted,
                grade
        );
    }
}
