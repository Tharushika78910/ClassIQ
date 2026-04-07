package Frontend.student;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import Frontend.Session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportCardPdfExporter {

    public static File export(
            File outFile,
            String studentNumber,
            String studentName,
            String studentClass,
            Map<String, SubjectLine> subjects,
            Integer total,
            Double average,
            String feedback
    ) throws Exception {

        ResourceBundle bundle = ResourceBundle.getBundle("messages", Session.getCurrentLocale());
        Locale locale = Session.getCurrentLocale();
        boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

        BaseFont baseFont = loadBaseFont(locale);

        Font titleFont = new Font(baseFont, 20, Font.BOLD);
        Font labelFont = new Font(baseFont, 12, Font.BOLD);
        Font valFont = new Font(baseFont, 12, Font.NORMAL);
        Font headerFont = new Font(baseFont, 12, Font.BOLD);
        Font bodyFont = new Font(baseFont, 12, Font.NORMAL);
        Font footerFont = new Font(baseFont, 10, Font.NORMAL);

        Document doc = new Document(PageSize.A4, 48, 48, 40, 70);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outFile));

        writer.setPageEvent(new FullPageBackground("/Homepage.png"));

        doc.open();

        try (InputStream lineStream = ReportCardPdfExporter.class.getResourceAsStream("/Logoline.png")) {
            if (lineStream != null) {
                Image line = Image.getInstance(lineStream.readAllBytes());
                line.scaleToFit(430, 130);
                line.setAlignment(Image.ALIGN_CENTER);
                line.setSpacingAfter(10);
                doc.add(line);
            }
        }

        Paragraph title = new Paragraph(bundle.getString("student.report.title"), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(14);
        doc.add(title);

        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(80);
        info.setHorizontalAlignment(Element.ALIGN_CENTER);
        info.setSpacingAfter(16);
        info.setWidths(new float[]{1.4f, 2.6f});
        info.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        addInfoRow(info, bundle.getString("student.report.studentNumber"), safe(studentNumber), labelFont, valFont, isArabic);
        addInfoRow(info, bundle.getString("student.report.studentName"), safe(studentName), labelFont, valFont, isArabic);
        addInfoRow(info, bundle.getString("student.report.class"), safe(studentClass), labelFont, valFont, isArabic);
        addInfoRow(info, bundle.getString("student.report.total"), total == null ? "-" : String.valueOf(total), labelFont, valFont, isArabic);
        addInfoRow(info, bundle.getString("student.report.average"), average == null ? "-" : String.format("%.2f", average), labelFont, valFont, isArabic);
        doc.add(info);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(80);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingAfter(14);
        table.setWidths(new float[]{2.4f, 1.0f, 1.0f});
        table.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        addHeaderCell(table, bundle.getString("student.report.subject"), headerFont, isArabic);
        addHeaderCell(table, bundle.getString("student.report.mark"), headerFont, isArabic);
        addHeaderCell(table, bundle.getString("student.report.grade"), headerFont, isArabic);

        for (Map.Entry<String, SubjectLine> entry : subjects.entrySet()) {
            SubjectLine line = entry.getValue();
            addBodyCell(table, entry.getKey(), bodyFont, isArabic);
            addBodyCell(table, line.mark == null ? "-" : String.valueOf(line.mark), bodyFont, isArabic);
            addBodyCell(table, (line.grade == null || line.grade.isBlank()) ? "-" : line.grade, bodyFont, isArabic);
        }

        doc.add(table);

        PdfPTable fbTable = new PdfPTable(2);
        fbTable.setWidthPercentage(80);
        fbTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        fbTable.setWidths(new float[]{1.4f, 2.6f});
        fbTable.setSpacingBefore(6);
        fbTable.setSpacingAfter(18);
        fbTable.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        PdfPCell fbLabel = new PdfPCell(new Phrase(bundle.getString("student.report.feedback"), labelFont));
        fbLabel.setBorder(Rectangle.NO_BORDER);
        fbLabel.setPadding(5);
        fbLabel.setHorizontalAlignment(isArabic ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
        fbLabel.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        PdfPCell fbValue = new PdfPCell(new Phrase(
                (feedback == null || feedback.isBlank()) ? bundle.getString("student.report.noFeedback") : feedback,
                valFont
        ));
        fbValue.setBorder(Rectangle.NO_BORDER);
        fbValue.setPadding(5);
        fbValue.setHorizontalAlignment(isArabic ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
        fbValue.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        fbTable.addCell(fbLabel);
        fbTable.addCell(fbValue);

        doc.add(fbTable);

        Paragraph generated = new Paragraph(
                bundle.getString("student.report.generatedOn") + " " + LocalDate.now(),
                footerFont
        );
        generated.setAlignment(isArabic ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT);
        generated.setSpacingBefore(10);
        doc.add(generated);

        doc.close();
        return outFile;
    }

    private static BaseFont loadBaseFont(Locale locale) throws Exception {
        String fontPath;

        if ("si".equalsIgnoreCase(locale.getLanguage())) {
            fontPath = "/fonts/NotoSansSinhala-Regular.ttf";
        } else if ("ar".equalsIgnoreCase(locale.getLanguage())) {
            fontPath = "/fonts/NotoNaskhArabic-Regular.ttf";
        } else {
            fontPath = "/fonts/NotoSans-Regular.ttf";
        }

        try (InputStream is = ReportCardPdfExporter.class.getResourceAsStream(fontPath)) {
            if (is == null) {
                throw new RuntimeException("Font file not found: " + fontPath);
            }

            byte[] fontBytes = is.readAllBytes();

            return BaseFont.createFont(
                    fontPath,
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED,
                    true,
                    fontBytes,
                    null
            );
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static void addInfoRow(
            PdfPTable table,
            String key,
            String value,
            Font keyFont,
            Font valueFont,
            boolean isArabic
    ) {
        PdfPCell c1 = new PdfPCell(new Phrase(key, keyFont));
        PdfPCell c2 = new PdfPCell(new Phrase(value == null ? "" : value, valueFont));

        c1.setBorder(Rectangle.NO_BORDER);
        c2.setBorder(Rectangle.NO_BORDER);

        c1.setPadding(5);
        c2.setPadding(5);

        c1.setHorizontalAlignment(isArabic ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
        c2.setHorizontalAlignment(isArabic ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);

        c1.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);
        c2.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        table.addCell(c1);
        table.addCell(c2);
    }

    private static void addHeaderCell(PdfPTable table, String text, Font font, boolean isArabic) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        cell.setHorizontalAlignment(isArabic ? Element.ALIGN_RIGHT : Element.ALIGN_CENTER);
        cell.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);
        table.addCell(cell);
    }

    private static void addBodyCell(PdfPTable table, String text, Font font, boolean isArabic) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setPadding(8);
        cell.setHorizontalAlignment(isArabic ? Element.ALIGN_RIGHT : Element.ALIGN_CENTER);
        cell.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);
        table.addCell(cell);
    }

    public static class SubjectLine {
        public final Integer mark;
        public final String grade;

        public SubjectLine(Integer mark, String grade) {
            this.mark = mark;
            this.grade = grade;
        }
    }

    private static class FullPageBackground extends PdfPageEventHelper {
        private final String resourcePath;

        FullPageBackground(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try (InputStream bgStream = ReportCardPdfExporter.class.getResourceAsStream(resourcePath)) {
                if (bgStream == null) {
                    return;
                }

                Image bg = Image.getInstance(bgStream.readAllBytes());
                float pageW = writer.getPageSize().getWidth();
                float pageH = writer.getPageSize().getHeight();

                bg.scaleAbsolute(pageW, pageH);
                bg.setAbsolutePosition(0, 0);

                writer.getDirectContentUnder().addImage(bg);
            } catch (Exception ignored) {
            }
        }
    }
}