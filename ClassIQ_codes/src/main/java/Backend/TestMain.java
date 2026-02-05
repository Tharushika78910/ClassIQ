package Backend;

import Backend.controller.MarksController;
import Backend.model.entity.StudentMarks;

public class TestMain {

    public static void main(String[] args) throws Exception {

        MarksController controller = new MarksController();

        StudentMarks m = new StudentMarks();
        m.setStudentId(201); // must exist in students table
        m.setSubject1(80);
        m.setSubject2(75);
        m.setSubject3(90);
        m.setSubject4(85);
        m.setSubject5(70);

        controller.submitMarks(m);

        System.out.println("Saved! Total=" + m.getTotal() + ", Avg=" + m.getAverage());
    }
}
