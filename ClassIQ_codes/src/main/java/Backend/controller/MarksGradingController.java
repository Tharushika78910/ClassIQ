package Backend.controller;

import Backend.model.dto.MarksWithGradesDTO;
import Backend.model.entity.StudentMarks;
import Backend.service.GradeService;

import java.sql.SQLException;
import java.util.Map;

public class MarksGradingController {

    private final MarksController marksController = new MarksController(); // existing
    private final GradeService gradeService = new GradeService();          // new

    public MarksWithGradesDTO submitMarksAndGetGrades(StudentMarks marks) throws SQLException {

        // validate student, calc total/avg, save to DB
        marksController.submitMarks(marks);

        // calculate grades
        Map<String, String> grades = gradeService.calculateGradesPerSubject(marks);

        return new MarksWithGradesDTO(
                marks.getStudentId(),
                marks.getTotal(),
                marks.getAverage(),
                grades
        );
    }
}
