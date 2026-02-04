package Backend.service;

import Backend.model.entity.StudentMarks;
import java.util.HashMap;
import java.util.Map;

public class GradeService {

    public String calculateGrade(int mark) {
        if (mark < 0 || mark > 100) return "INVALID";
        if (mark >= 75) return "A";
        if (mark >= 65) return "B";
        if (mark >= 55) return "C";
        if (mark >= 35) return "S";
        return "F";
    }

    public Map<String, String> calculateGradesPerSubject(StudentMarks marks) {
        Map<String, String> grades = new HashMap<>();

        grades.put("subject1", calculateGrade(marks.getSubject1()));
        grades.put("subject2", calculateGrade(marks.getSubject2()));
        grades.put("subject3", calculateGrade(marks.getSubject3()));
        grades.put("subject4", calculateGrade(marks.getSubject4()));
        grades.put("subject5", calculateGrade(marks.getSubject5()));

        return grades;
    }
}
