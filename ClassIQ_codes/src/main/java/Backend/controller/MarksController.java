package Backend.controller;

import Backend.model.dao.MarksDao;
import Backend.model.dao.StudentDao;
import Backend.model.dao.impl.MarksDaoImpl;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.entity.StudentMarks;
import Backend.service.MarksService;

import java.sql.SQLException;

public class MarksController {

    private final MarksService marksService = new MarksService();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final MarksDao marksDao = new MarksDaoImpl();

    public void submitMarks(StudentMarks marks) throws SQLException {

        // ensure student exists (because student_id is FK)
        if (!studentDao.existsById(marks.getStudentId())) {
            throw new IllegalArgumentException("Student ID not found: " + marks.getStudentId());
        }

        // calculate automatically
        marksService.calculateTotalAndAverage(marks);

        // save to database
        marksDao.saveMarks(marks);
    }
}
