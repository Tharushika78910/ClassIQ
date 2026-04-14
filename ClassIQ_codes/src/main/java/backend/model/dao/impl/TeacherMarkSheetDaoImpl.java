package backend.model.dao.impl;

import backend.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TeacherMarkSheetDaoImpl {

    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_ASSIGNMENT = "assignment";
    private static final String COL_PROJECT = "project";
    private static final String COL_FINAL_EXAM = "final_exam";

    private static final String UPSERT_SQL =
            "INSERT INTO teacher_marksheet " +
                    "(student_id, student_name, assignment, project, final_exam, total, grade, teacher_id, subject) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "student_name = VALUES(student_name), " +
                    "assignment = VALUES(assignment), " +
                    "project = VALUES(project), " +
                    "final_exam = VALUES(final_exam), " +
                    "total = VALUES(total), " +
                    "grade = VALUES(grade), " +
                    "created_at = CURRENT_TIMESTAMP";

    private static final String DELETE_SQL =
            "DELETE FROM teacher_marksheet " +
                    "WHERE teacher_id = ? AND UPPER(subject) = UPPER(?)";

    private static final String LOAD_EXISTING_SQL =
            "SELECT student_id, assignment, project, final_exam " +
                    "FROM teacher_marksheet " +
                    "WHERE teacher_id = ? AND UPPER(subject) = UPPER(?)";

    public void saveTeacherMarkSheetRow(TeacherMarkSheetRow row) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPSERT_SQL)) {

            ps.setInt(1, row.getStudentId());
            ps.setString(2, row.getStudentName());
            ps.setInt(3, row.getAssignment());
            ps.setInt(4, row.getProject());
            ps.setInt(5, row.getFinalExam());
            ps.setInt(6, row.getTotal());
            ps.setString(7, row.getGrade());
            ps.setInt(8, row.getTeacherId());
            ps.setString(9, row.getSubject());

            ps.executeUpdate();
        }
    }

    public int deleteMarksForTeacherSubject(int teacherId, String subject) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, teacherId);
            ps.setString(2, subject);

            return ps.executeUpdate();
        }
    }

    public Map<Integer, int[]> loadExistingMarks(int teacherId, String subject) throws SQLException {
        Map<Integer, int[]> map = new HashMap<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(LOAD_EXISTING_SQL)) {

            ps.setInt(1, teacherId);
            ps.setString(2, subject);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int sid = rs.getInt(COL_STUDENT_ID);
                    int a = rs.getInt(COL_ASSIGNMENT);
                    int p = rs.getInt(COL_PROJECT);
                    int f = rs.getInt(COL_FINAL_EXAM);
                    map.put(sid, new int[]{a, p, f});
                }
            }
        }

        return map;
    }

    public static class TeacherMarkSheetRow {
        private int studentId;
        private String studentName;
        private int assignment;
        private int project;
        private int finalExam;
        private int total;
        private String grade;
        private int teacherId;
        private String subject;

        public TeacherMarkSheetRow() {
            /*
             * Default constructor intentionally left empty.
             * This object is populated later using setter methods.
             */
        }

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public int getAssignment() {
            return assignment;
        }

        public void setAssignment(int assignment) {
            this.assignment = assignment;
        }

        public int getProject() {
            return project;
        }

        public void setProject(int project) {
            this.project = project;
        }

        public int getFinalExam() {
            return finalExam;
        }

        public void setFinalExam(int finalExam) {
            this.finalExam = finalExam;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(int teacherId) {
            this.teacherId = teacherId;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }
}