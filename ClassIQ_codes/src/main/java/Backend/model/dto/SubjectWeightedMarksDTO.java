package Backend.model.dto;

public class SubjectWeightedMarksDTO {
    private int studentId;
    private int subjectNo;

    private int assignmentMark;
    private int projectMark;
    private int finalExamMark;

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSubjectNo() { return subjectNo; }
    public void setSubjectNo(int subjectNo) { this.subjectNo = subjectNo; }

    public int getAssignmentMark() { return assignmentMark; }
    public void setAssignmentMark(int assignmentMark) { this.assignmentMark = assignmentMark; }

    public int getProjectMark() { return projectMark; }
    public void setProjectMark(int projectMark) { this.projectMark = projectMark; }

    public int getFinalExamMark() { return finalExamMark; }
    public void setFinalExamMark(int finalExamMark) { this.finalExamMark = finalExamMark; }
}
