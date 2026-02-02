package backend.model.dao.impl;

import backend.config.DatabaseConfig;
import backend.model.dao.MarksDao;
import backend.model.entity.StudentMarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MarksDaoImpl implements MarksDao {

    @Override
    public void saveMarks(StudentMarks marks) throws SQLException {

        String sql = """
            INSERT INTO student_marks
            (student_id, subject1, subject2, subject3, subject4, subject5, total, average)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, marks.getStudentId());
            ps.setInt(2, marks.getSubject1());
            ps.setInt(3, marks.getSubject2());
            ps.setInt(4, marks.getSubject3());
            ps.setInt(5, marks.getSubject4());
            ps.setInt(6, marks.getSubject5());
            ps.setInt(7, marks.getTotal());
            ps.setDouble(8, marks.getAverage());

            ps.executeUpdate();
        }
    }
}
