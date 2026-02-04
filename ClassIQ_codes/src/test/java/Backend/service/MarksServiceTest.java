package Backend.service;

import Backend.model.entity.StudentMarks;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MarksServiceTest {

    @Test
    void calculateTotalAndAverage_shouldSetTotalAndAverage() {
        StudentMarks m = new StudentMarks();
        m.setSubject1(80);
        m.setSubject2(70);
        m.setSubject3(60);
        m.setSubject4(50);
        m.setSubject5(40);

        MarksService s = new MarksService();
        s.calculateTotalAndAverage(m);

        assertEquals(300, m.getTotal());
        assertEquals(60.0, m.getAverage(), 0.0001);
    }
}
