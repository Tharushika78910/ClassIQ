package Frontend.student;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReportCardPdfExporterTest {

    @Test
    void export_shouldCreatePdfFile() throws Exception {
        File tempDir = Files.createTempDirectory("classiq-pdf-test").toFile();
        File outFile = new File(tempDir, "test-report.pdf");

        Map<String, ReportCardPdfExporter.SubjectLine> subjects = new LinkedHashMap<>();
        subjects.put("Mathematics", new ReportCardPdfExporter.SubjectLine(80, "A"));
        subjects.put("English", new ReportCardPdfExporter.SubjectLine(65, "B"));

        File pdf = ReportCardPdfExporter.export(
                outFile,
                "S001",
                "John Doe",
                "Grade 2",
                subjects,
                145,
                72.5,
                "Keep it up!"
        );

        assertNotNull(pdf, "Returned file should not be null");
        assertEquals(outFile.getAbsolutePath(), pdf.getAbsolutePath(), "Returned file should match requested output file");
        assertTrue(pdf.exists(), "PDF file should exist");
        assertTrue(pdf.length() > 0, "PDF file should not be empty");
    }
}