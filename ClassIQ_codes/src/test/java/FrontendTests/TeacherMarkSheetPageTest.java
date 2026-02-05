package FrontendTests;

import Frontend.teacher.TeacherMarkSheetPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMarkSheetPageTest {

    @Test
    void total_isSumOfAssignmentProjectFinalExam() {
        TeacherMarkSheetPage.MarkRow row =
                new TeacherMarkSheetPage.MarkRow("S001", "Alex Johnson", 10, 25, 55);

        assertEquals(90, row.getTotal());
    }

    @Test
    void getters_returnCorrectValues() {
        TeacherMarkSheetPage.MarkRow row =
                new TeacherMarkSheetPage.MarkRow("S010", "Mia Tran", 9, 28, 60);

        assertEquals("S010", row.getStudentId());
        assertEquals("Mia Tran", row.getStudentName());
        assertEquals(9, row.getAssignment());
        assertEquals(28, row.getProject());
        assertEquals(60, row.getFinalExam());
    }

    @Test
    void grade_isA_whenTotalAtLeast75() {
        // 25 + 25 + 25 = 75
        TeacherMarkSheetPage.MarkRow row =
                new TeacherMarkSheetPage.MarkRow("S100", "A Student", 25, 25, 25);

        assertEquals(75, row.getTotal());
        assertEquals("A", row.getGrade());
    }

    @Test
    void grade_isB_whenTotalBetween65And74() {
        // 20 + 20 + 25 = 65
        TeacherMarkSheetPage.MarkRow row =
                new TeacherMarkSheetPage.MarkRow("S101", "B Student", 20, 20, 25);

        assertEquals(65, row.getTotal());
        assertEquals("B", row.getGrade());

        // 24 + 20 + 20 = 64 -> should be C, just to show boundary
        TeacherMarkSheetPage.MarkRow row2 =
                new TeacherMarkSheetPage.MarkRow("S102", "Boundary Student", 24, 20, 20);

        assertEquals(64, row2.getTotal());
        assertEquals("C", row2.getGrade());
    }

    @Test
    void grade_isC_whenTotalBetween55And64() {
        // 15 + 20 + 20 = 55
        TeacherMarkSheetPage.MarkRow row =
                new TeacherMarkSheetPage.MarkRow("S103", "C Student", 15, 20, 20);

        assertEquals(55, row.getTotal());
        assertEquals("C", row.getGrade());
    }

    @Test
    void grade_isS_whenTotalBetween35And54() {
        // 10 + 10 + 15 = 35
        TeacherMarkSheetPage.MarkRow row =
                new TeacherMarkSheetPage.MarkRow("S104", "S Student", 10, 10, 15);

        assertEquals(35, row.getTotal());
        assertEquals("S", row.getGrade());
    }

    @Test
    void grade_isF_whenTotalBelow35() {
        // 10 + 10 + 14 = 34
        TeacherMarkSheetPage.MarkRow row =
                new TeacherMarkSheetPage.MarkRow("S105", "F Student", 10, 10, 14);

        assertEquals(34, row.getTotal());
        assertEquals("F", row.getGrade());
    }
}
