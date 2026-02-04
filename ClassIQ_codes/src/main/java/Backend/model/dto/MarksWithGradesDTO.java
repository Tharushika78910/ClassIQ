package Backend.model.dto;

import java.util.Map;

public class MarksWithGradesDTO {

    private final int studentId;
    private final int total;
    private final double average;
    private final Map<String, String> gradesPerSubject;

    public MarksWithGradesDTO(int studentId, int total, double average, Map<String, String> gradesPerSubject) {
        this.studentId = studentId;
        this.total = total;
        this.average = average;
        this.gradesPerSubject = gradesPerSubject;
    }

    public int getStudentId() { return studentId; }
    public int getTotal() { return total; }
    public double getAverage() { return average; }
    public Map<String, String> getGradesPerSubject() { return gradesPerSubject; }
}
