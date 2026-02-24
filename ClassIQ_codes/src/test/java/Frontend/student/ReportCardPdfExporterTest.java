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
        String originalHome = System.getProperty("user.home");

        File tempHome = Files.createTempDirectory("classiq-home").toFile();
        File downloads = new File(tempHome, "Downloads");
        assertTrue(downloads.mkdirs() || downloads.exists(), "Downloads folder should exist");

        System.setProperty("user.home", tempHome.getAbsolutePath());

        try {
            Map<String, ReportCardPdfExporter.SubjectLine> subjects = new LinkedHashMap<>();
            subjects.put("Mathematics", new ReportCardPdfExporter.SubjectLine(80, "A"));
            subjects.put("English", new ReportCardPdfExporter.SubjectLine(65, "B"));

            File pdf = ReportCardPdfExporter.export(
                    "S001",
                    "John Doe",
                    "Grade 2",
                    subjects,
                    145,
                    72.5,
                    "Keep it up!"
            );

            assertNotNull(pdf, "Returned file should not be null");
            assertTrue(pdf.exists(), "PDF file should exist");
            assertTrue(pdf.length() > 0, "PDF file should not be empty");

        } finally {
            System.setProperty("user.home", originalHome);
        }
    }
}