package springbootapp.minivms.exception.custom;

public class NotValidInvoiceId extends RuntimeException {
    public NotValidInvoiceId(String message) {
        super(message);
    }
}
