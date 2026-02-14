package Backend.model.dto;

import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;

public class StudentDetailsDTO {

    private Student student;
    private StudentMarks marks;

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public StudentMarks getMarks() { return marks; }
    public void setMarks(StudentMarks marks) { this.marks = marks; }
}
