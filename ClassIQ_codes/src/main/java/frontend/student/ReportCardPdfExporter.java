package frontend.student;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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
import frontend.Session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class ReportCardPdfExporter {

    private static final String MESSAGE_BUNDLE = "messages";
    private static final String BACKGROUND_IMAGE = "/Homepage.png";
    private static final String HEADER_IMAGE = "/Logoline.png";
    private static final float PAGE_MARGIN_LEFT = 48f;
    private static final float PAGE_MARGIN_RIGHT = 48f;
    private static final float PAGE_MARGIN_TOP = 40f;
    private static final float PAGE_MARGIN_BOTTOM = 70f;
    private static final float CONTENT_WIDTH_PERCENT = 80f;

    private ReportCardPdfExporter() {
        // Utility class
    }

    public static File export(File outFile, ReportCardData data) throws ReportCardExportException {
        validateInputs(outFile, data);

        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_BUNDLE, Session.getCurrentLocale());
        Locale locale = Session.getCurrentLocale();
        boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

        BaseFont baseFont = loadBaseFont(locale);
        PdfFonts fonts = createFonts(baseFont);

        Document document = new Document(
                PageSize.A4,
                PAGE_MARGIN_LEFT,
                PAGE_MARGIN_RIGHT,
                PAGE_MARGIN_TOP,
                PAGE_MARGIN_BOTTOM
        );

        try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            writer.setPageEvent(new FullPageBackground(BACKGROUND_IMAGE));

            try {
                document.open();
                addHeaderImage(document);
                addTitle(document, bundle, fonts.titleFont);
                addStudentInfo(document, bundle, fonts, data, isArabic);
                addMarksTable(document, bundle, fonts, data.subjects(), isArabic);
                addFeedbackSection(document, bundle, fonts, data.feedback(), isArabic);
                addGeneratedDate(document, bundle, fonts.footerFont, isArabic);
            } finally {
                if (document.isOpen()) {
                    document.close();
                }
            }

            return outFile;
        } catch (IOException | DocumentException exception) {
            throw new ReportCardExportException(
                    "Failed to export report card PDF: " + outFile.getAbsolutePath(),
                    exception
            );
        }
    }

    private static void validateInputs(File outFile, ReportCardData data) throws ReportCardExportException {
        if (outFile == null) {
            throw new ReportCardExportException("Output file cannot be null.");
        }

        if (data == null) {
            throw new ReportCardExportException("Report card data cannot be null.");
        }

        if (data.subjects() == null) {
            throw new ReportCardExportException("Subjects map cannot be null.");
        }
    }

    private static PdfFonts createFonts(BaseFont baseFont) {
        return new PdfFonts(
                new Font(baseFont, 20, Font.BOLD),
                new Font(baseFont, 12, Font.BOLD),
                new Font(baseFont, 12, Font.NORMAL),
                new Font(baseFont, 12, Font.BOLD),
                new Font(baseFont, 12, Font.NORMAL),
                new Font(baseFont, 10, Font.NORMAL)
        );
    }

    private static void addHeaderImage(Document document) throws IOException, DocumentException {
        try (InputStream lineStream = ReportCardPdfExporter.class.getResourceAsStream(HEADER_IMAGE)) {
            if (lineStream == null) {
                return;
            }

            Image line = Image.getInstance(lineStream.readAllBytes());
            line.scaleToFit(430, 130);
            line.setAlignment(Element.ALIGN_CENTER);
            line.setSpacingAfter(10);
            document.add(line);
        }
    }

    private static void addTitle(Document document, ResourceBundle bundle, Font titleFont) throws DocumentException {
        Paragraph title = new Paragraph(bundle.getString("student.report.title"), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(14);
        document.add(title);
    }

    private static void addStudentInfo(
            Document document,
            ResourceBundle bundle,
            PdfFonts fonts,
            ReportCardData data,
            boolean isArabic
    ) throws DocumentException {
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(CONTENT_WIDTH_PERCENT);
        infoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        infoTable.setSpacingAfter(16);
        infoTable.setWidths(new float[]{1.4f, 2.6f});
        infoTable.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        addInfoRow(
                infoTable,
                bundle.getString("student.report.studentNumber"),
                safe(data.studentNumber()),
                fonts.labelFont,
                fonts.valueFont,
                isArabic
        );
        addInfoRow(
                infoTable,
                bundle.getString("student.report.studentName"),
                safe(data.studentName()),
                fonts.labelFont,
                fonts.valueFont,
                isArabic
        );
        addInfoRow(
                infoTable,
                bundle.getString("student.report.class"),
                safe(data.studentClass()),
                fonts.labelFont,
                fonts.valueFont,
                isArabic
        );
        addInfoRow(
                infoTable,
                bundle.getString("student.report.total"),
                data.total() == null ? "-" : String.valueOf(data.total()),
                fonts.labelFont,
                fonts.valueFont,
                isArabic
        );
        addInfoRow(
                infoTable,
                bundle.getString("student.report.average"),
                data.average() == null ? "-" : String.format("%.2f", data.average()),
                fonts.labelFont,
                fonts.valueFont,
                isArabic
        );

        document.add(infoTable);
    }

    private static void addMarksTable(
            Document document,
            ResourceBundle bundle,
            PdfFonts fonts,
            Map<String, SubjectLine> subjects,
            boolean isArabic
    ) throws DocumentException {
        PdfPTable marksTable = new PdfPTable(3);
        marksTable.setWidthPercentage(CONTENT_WIDTH_PERCENT);
        marksTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        marksTable.setSpacingAfter(14);
        marksTable.setWidths(new float[]{2.4f, 1.0f, 1.0f});
        marksTable.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        addHeaderCell(marksTable, bundle.getString("student.report.subject"), fonts.headerFont, isArabic);
        addHeaderCell(marksTable, bundle.getString("student.report.mark"), fonts.headerFont, isArabic);
        addHeaderCell(marksTable, bundle.getString("student.report.grade"), fonts.headerFont, isArabic);

        for (Map.Entry<String, SubjectLine> entry : subjects.entrySet()) {
            SubjectLine subjectLine = entry.getValue();

            addBodyCell(marksTable, entry.getKey(), fonts.bodyFont, isArabic);
            addBodyCell(
                    marksTable,
                    subjectLine.mark() == null ? "-" : String.valueOf(subjectLine.mark()),
                    fonts.bodyFont,
                    isArabic
            );
            addBodyCell(
                    marksTable,
                    isBlank(subjectLine.grade()) ? "-" : subjectLine.grade(),
                    fonts.bodyFont,
                    isArabic
            );
        }

        document.add(marksTable);
    }

    private static void addFeedbackSection(
            Document document,
            ResourceBundle bundle,
            PdfFonts fonts,
            String feedback,
            boolean isArabic
    ) throws DocumentException {
        PdfPTable feedbackTable = new PdfPTable(2);
        feedbackTable.setWidthPercentage(CONTENT_WIDTH_PERCENT);
        feedbackTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        feedbackTable.setWidths(new float[]{1.4f, 2.6f});
        feedbackTable.setSpacingBefore(6);
        feedbackTable.setSpacingAfter(18);
        feedbackTable.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);

        PdfPCell feedbackLabel = createBorderlessCell(
                bundle.getString("student.report.feedback"),
                fonts.labelFont,
                isArabic
        );

        String feedbackText = isBlank(feedback)
                ? bundle.getString("student.report.noFeedback")
                : feedback;

        PdfPCell feedbackValue = createBorderlessCell(feedbackText, fonts.valueFont, isArabic);

        feedbackTable.addCell(feedbackLabel);
        feedbackTable.addCell(feedbackValue);

        document.add(feedbackTable);
    }

    private static void addGeneratedDate(
            Document document,
            ResourceBundle bundle,
            Font footerFont,
            boolean isArabic
    ) throws DocumentException {
        Paragraph generated = new Paragraph(
                bundle.getString("student.report.generatedOn") + " " + LocalDate.now(),
                footerFont
        );
        generated.setAlignment(isArabic ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT);
        generated.setSpacingBefore(10);
        document.add(generated);
    }

    private static BaseFont loadBaseFont(Locale locale) throws ReportCardExportException {
        String fontPath = resolveFontPath(locale);

        try (InputStream inputStream = ReportCardPdfExporter.class.getResourceAsStream(fontPath)) {
            if (inputStream == null) {
                throw new ReportCardExportException("Font file not found: " + fontPath);
            }

            byte[] fontBytes = inputStream.readAllBytes();

            return BaseFont.createFont(
                    fontPath,
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED,
                    true,
                    fontBytes,
                    null
            );
        } catch (IOException | DocumentException exception) {
            throw new ReportCardExportException("Failed to load font: " + fontPath, exception);
        }
    }

    private static String resolveFontPath(Locale locale) {
        String language = locale.getLanguage();

        if ("si".equalsIgnoreCase(language)) {
            return "/fonts/NotoSansSinhala-Regular.ttf";
        }

        if ("ar".equalsIgnoreCase(language)) {
            return "/fonts/NotoNaskhArabic-Regular.ttf";
        }

        return "/fonts/NotoSans-Regular.ttf";
    }

    private static void addInfoRow(
            PdfPTable table,
            String key,
            String value,
            Font keyFont,
            Font valueFont,
            boolean isArabic
    ) {
        PdfPCell keyCell = createBorderlessCell(key, keyFont, isArabic);
        PdfPCell valueCell = createBorderlessCell(value == null ? "" : value, valueFont, isArabic);

        table.addCell(keyCell);
        table.addCell(valueCell);
    }

    private static PdfPCell createBorderlessCell(String text, Font font, boolean isArabic) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        cell.setHorizontalAlignment(isArabic ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
        cell.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);
        return cell;
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

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record SubjectLine(Integer mark, String grade) {
    }

    public record ReportCardData(
            String studentNumber,
            String studentName,
            String studentClass,
            Map<String, SubjectLine> subjects,
            Integer total,
            Double average,
            String feedback
    ) {
    }

    private record PdfFonts(
            Font titleFont,
            Font labelFont,
            Font valueFont,
            Font headerFont,
            Font bodyFont,
            Font footerFont
    ) {
    }

    public static class ReportCardExportException extends Exception {
        public ReportCardExportException(String message) {
            super(message);
        }

        public ReportCardExportException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static class FullPageBackground extends PdfPageEventHelper {
        private final String resourcePath;

        FullPageBackground(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try (InputStream backgroundStream =
                         ReportCardPdfExporter.class.getResourceAsStream(resourcePath)) {

                if (backgroundStream == null) {
                    return;
                }

                Image background = Image.getInstance(backgroundStream.readAllBytes());
                float pageWidth = writer.getPageSize().getWidth();
                float pageHeight = writer.getPageSize().getHeight();

                background.scaleAbsolute(pageWidth, pageHeight);
                background.setAbsolutePosition(0, 0);

                writer.getDirectContentUnder().addImage(background);
            } catch (IOException | DocumentException exception) {
                // Background image is optional. Ignore loading/rendering issues so PDF generation can continue.
            }
        }
    }
}