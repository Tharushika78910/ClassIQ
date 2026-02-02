package backend.model.entity;

public class StudentMarks {

    private int studentId; // link to students table

    private int subject1, subject2, subject3, subject4, subject5;
    private int total;
    private double average;

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSubject1() { return subject1; }
    public void setSubject1(int subject1) { this.subject1 = subject1; }

    public int getSubject2() { return subject2; }
    public void setSubject2(int subject2) { this.subject2 = subject2; }

    public int getSubject3() { return subject3; }
    public void setSubject3(int subject3) { this.subject3 = subject3; }

    public int getSubject4() { return subject4; }
    public void setSubject4(int subject4) { this.subject4 = subject4; }

    public int getSubject5() { return subject5; }
    public void setSubject5(int subject5) { this.subject5 = subject5; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public double getAverage() { return average; }
    public void setAverage(double average) { this.average = average; }
}
