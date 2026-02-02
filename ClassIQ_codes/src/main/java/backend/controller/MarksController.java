package backend.controller;

import backend.model.dao.MarksDao;
import backend.model.dao.StudentDao;
import backend.model.dao.impl.MarksDaoImpl;
import backend.model.dao.impl.StudentDaoImpl;
import backend.model.entity.StudentMarks;
import backend.service.MarksService;

import java.sql.SQLException;

public class MarksController {

    private final MarksService marksService = new MarksService();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final MarksDao marksDao = new MarksDaoImpl();

    public void submitMarks(StudentMarks marks) throws SQLException {

        // 1) ensure student exists (because student_id is FK)
        if (!studentDao.existsById(marks.getStudentId())) {
            throw new IllegalArgumentException("Student ID not found: " + marks.getStudentId());
        }

        // 2) calculate automatically
        marksService.calculateTotalAndAverage(marks);

        // 3) save to database
        marksDao.saveMarks(marks);
    }
}
