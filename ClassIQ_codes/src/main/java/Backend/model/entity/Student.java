package Backend.model.entity;

public class Student {
    private int studentId;
    private String firstName;
    private String lastName;
    private String studentNumber;
    private String email;
    private int userId;

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
