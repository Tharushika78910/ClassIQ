package Backend.model.dao.impl;

import Backend.db.DBConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMarkSheetDaoImplTest {

    @Test
    void saveTeacherMarkSheetRow_insertsRow_andWeCanReadItBack() throws Exception {

        TeacherMarkSheetDaoImpl dao = new TeacherMarkSheetDaoImpl();


        int studentId = 202;
        String name = "Kumudu Nalleperuma";

        int assignment = 10;
        int project = 10;
        int finalExam = 10;
        int total = assignment + project + finalExam;
        String grade = "F";


        assertDoesNotThrow(() ->
                dao.saveTeacherMarkSheetRow(studentId, name, assignment, project, finalExam, total, grade)
        );


        int lastId;
        String checkSql = """
            SELECT id, student_id, student_name, assignment, project, final_exam, total, grade
            FROM teacher_marksheet
            ORDER BY id DESC
            LIMIT 1
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(checkSql);
             ResultSet rs = ps.executeQuery()) {

            assertTrue(rs.next(), "No row found in teacher_marksheet after insert");

            lastId = rs.getInt("id");
            assertEquals(studentId, rs.getInt("student_id"));
            assertEquals(name, rs.getString("student_name"));
            assertEquals(assignment, rs.getInt("assignment"));
            assertEquals(project, rs.getInt("project"));
            assertEquals(finalExam, rs.getInt("final_exam"));
            assertEquals(total, rs.getInt("total"));
            assertEquals(grade, rs.getString("grade"));
        }


        try (Connection con = DBConnection.getConnection();
             PreparedStatement del = con.prepareStatement("DELETE FROM teacher_marksheet WHERE id=?")) {
            del.setInt(1, lastId);
            del.executeUpdate();
        }
    }
}
