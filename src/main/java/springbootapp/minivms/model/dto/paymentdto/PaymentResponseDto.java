package springbootapp.minivms.model.dto.paymentdto;

import springbootapp.minivms.model.entities.enums.PaymentStatus;

public class PaymentResponseDto {
    private String invoiceId;
    private PaymentStatus status;

    public PaymentResponseDto() {

    }

    public PaymentResponseDto(String invoiceId, PaymentStatus status) {
        setInvoiceId(invoiceId);
        setStatus(status);
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
