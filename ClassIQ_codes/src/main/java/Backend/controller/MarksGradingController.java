package Backend.controller;

import Backend.model.dto.MarksWithGradesDTO;
import Backend.model.entity.StudentMarks;
import Backend.service.GradeService;

import java.sql.SQLException;
import java.util.Map;

/**
 * Wraps existing MarksController without modifying it.
 * Call this from JavaFX when you want grades + saved marks.
 */
public class MarksGradingController {

    private final MarksController marksController = new MarksController(); // existing
    private final GradeService gradeService = new GradeService();          // new

    public MarksWithGradesDTO submitMarksAndGetGrades(StudentMarks marks) throws SQLException {

        // existing flow: validate student, calc total/avg, save to DB
        marksController.submitMarks(marks);

        // new: calculate grades
        Map<String, String> grades = gradeService.calculateGradesPerSubject(marks);

        return new MarksWithGradesDTO(
                marks.getStudentId(),
                marks.getTotal(),
                marks.getAverage(),
                grades
        );
    }
}
