package Frontend.student;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

public class ReportCardPdfExporter {

    public static File export(
            String studentNumber,
            String studentName,
            String studentClass,
            Map<String, SubjectLine> subjects,
            Integer total,
            Double average,
            String feedback
    ) throws Exception {

        String fileName = "ReportCard_" + studentNumber + "_" + LocalDate.now() + ".pdf";
        File outFile = new File(System.getProperty("user.home") + File.separator + "Downloads", fileName);

        Document doc = new Document(PageSize.A4, 48, 48, 40, 70);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outFile));

        // full page background
        writer.setPageEvent(new FullPageBackground("/Homepage.png"));

        doc.open();

        // LOGOLINE
        try (InputStream lineStream = ReportCardPdfExporter.class.getResourceAsStream("/logoline.png")) {
            if (lineStream != null) {
                Image line = Image.getInstance(lineStream.readAllBytes());
                line.scaleToFit(430, 130);
                line.setAlignment(Image.ALIGN_CENTER);
                line.setSpacingAfter(10);
                doc.add(line);
            }
        }

        // TITLE
        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
        Paragraph title = new Paragraph("Academic Report Card", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(14);
        doc.add(title);

        Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font valFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

        // TOP DETAILS (NO student ID, NO created at)
        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(80);
        info.setHorizontalAlignment(Element.ALIGN_CENTER);
        info.setSpacingAfter(16);
        info.setWidths(new float[]{1.4f, 2.6f});

        addInfoRow(info, "Student Number", safe(studentNumber), labelFont, valFont);
        addInfoRow(info, "Student Name", safe(studentName), labelFont, valFont);
        addInfoRow(info, "Class", safe(studentClass), labelFont, valFont);

        addInfoRow(info, "Total", total == null ? "-" : String.valueOf(total), labelFont, valFont);
        addInfoRow(info, "Average", average == null ? "-" : String.format("%.2f", average), labelFont, valFont);

        doc.add(info);

        // SUBJECT TABLE
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(80);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingAfter(14);
        table.setWidths(new float[]{2.4f, 1.0f, 1.0f});

        addHeaderCell(table, "Subject");
        addHeaderCell(table, "Mark");
        addHeaderCell(table, "Grade");

        for (Map.Entry<String, SubjectLine> e : subjects.entrySet()) {
            SubjectLine line = e.getValue();
            addBodyCell(table, e.getKey());
            addBodyCell(table, line.mark == null ? "-" : String.valueOf(line.mark));
            addBodyCell(table, (line.grade == null || line.grade.isBlank()) ? "-" : line.grade);
        }

        doc.add(table);

        // FEEDBACK (aligned under table)
        PdfPTable fbTable = new PdfPTable(2);
        fbTable.setWidthPercentage(80);
        fbTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        fbTable.setWidths(new float[]{1.4f, 2.6f});
        fbTable.setSpacingBefore(6);
        fbTable.setSpacingAfter(18);

        PdfPCell fbLabel = new PdfPCell(new Phrase("Feedback", labelFont));
        fbLabel.setBorder(Rectangle.NO_BORDER);
        fbLabel.setPadding(5);

        PdfPCell fbValue = new PdfPCell(new Phrase(
                (feedback == null || feedback.isBlank()) ? "No feedback has been added yet." : feedback,
                valFont
        ));
        fbValue.setBorder(Rectangle.NO_BORDER);
        fbValue.setPadding(5);

        fbTable.addCell(fbLabel);
        fbTable.addCell(fbValue);

        doc.add(fbTable);

        // GENERATED ON (BOTTOM RIGHT ONLY)
        Font footerFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
        Paragraph generated = new Paragraph("Generated on: " + LocalDate.now(), footerFont);
        generated.setAlignment(Element.ALIGN_RIGHT);
        generated.setSpacingBefore(10);
        doc.add(generated);

        doc.close();
        return outFile;
    }

    // helpers

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static void addInfoRow(PdfPTable t, String k, String v, Font kf, Font vf) {
        PdfPCell c1 = new PdfPCell(new Phrase(k, kf));
        PdfPCell c2 = new PdfPCell(new Phrase(v == null ? "" : v, vf));
        c1.setBorder(Rectangle.NO_BORDER);
        c2.setBorder(Rectangle.NO_BORDER);
        c1.setPadding(5);
        c2.setPadding(5);
        t.addCell(c1);
        t.addCell(c2);
    }

    private static void addHeaderCell(PdfPTable t, String text) {
        Font f = new Font(Font.HELVETICA, 12, Font.BOLD);
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setPadding(8);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c);
    }

    private static void addBodyCell(PdfPTable t, String text) {
        Font f = new Font(Font.HELVETICA, 12, Font.NORMAL);
        PdfPCell c = new PdfPCell(new Phrase(text == null ? "" : text, f));
        c.setPadding(8);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c);
    }

    public static class SubjectLine {
        public final Integer mark;
        public final String grade;

        public SubjectLine(Integer mark, String grade) {
            this.mark = mark;
            this.grade = grade;
        }
    }

    // FULL PAGE background image
    private static class FullPageBackground extends PdfPageEventHelper {
        private final String resourcePath;

        FullPageBackground(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try (InputStream bgStream = ReportCardPdfExporter.class.getResourceAsStream(resourcePath)) {
                if (bgStream == null) return;

                Image bg = Image.getInstance(bgStream.readAllBytes());
                float pageW = writer.getPageSize().getWidth();
                float pageH = writer.getPageSize().getHeight();

                bg.scaleAbsolute(pageW, pageH);
                bg.setAbsolutePosition(0, 0);

                writer.getDirectContentUnder().addImage(bg);
            } catch (Exception ignored) { }
        }
    }
}