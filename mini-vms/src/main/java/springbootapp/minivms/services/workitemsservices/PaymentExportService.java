package springbootapp.minivms.services.workitemsservices;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfTable;
import org.openpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import springbootapp.minivms.model.dto.paymentdto.PaymentResponseFullDataDto;

import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;

@Service
public class PaymentExportService {

    private static final Logger log = LoggerFactory.getLogger(PaymentExportService.class);

    public byte[] generatePaymentPdf(PaymentResponseFullDataDto payment) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Payment Details", titleFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            addRow(table, "Invoice ID", payment.getInvoiceId() != null ? payment.getInvoiceId() : "-");
            addRow(table, "Supplier", payment.getSupplierName() != null ? payment.getSupplierName() : "-");
            addRow(table, "Amount", payment.getAmount() != null
                    ? payment.getAmount().setScale(2, RoundingMode.HALF_UP).toPlainString()
                    : "-");
            addRow(table, "Status", payment.getStatus() != null ? payment.getStatus().name() : "-");
            addRow(table, "Paid At", payment.getPaidAt() != null ? payment.getPaidAt().toString() : "Not paid yet");

            document.add(table);
            document.close();

            log.info("Generated payment PDF for invoice {}", payment.getInvoiceId());
            return outputStream.toByteArray();
        } catch (DocumentException exception) {
            throw new IllegalStateException("Failed to generate payment PDF", exception);
        }

    }

    private void addRow(PdfPTable table, String field, String value) {
        table.addCell(field);
        table.addCell(value);
    }
}
