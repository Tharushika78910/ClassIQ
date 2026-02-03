package Backend.service;

import Backend.model.entity.StudentMarks;

public class MarksService {

    public void calculateTotalAndAverage(StudentMarks marks) {
        int total = marks.getSubject1()
                + marks.getSubject2()
                + marks.getSubject3()
                + marks.getSubject4()
                + marks.getSubject5();

        marks.setTotal(total);
        marks.setAverage(total / 5.0);
    }
}
