package springbootapp.minivms.model.dto.paymentdto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceConversionDto(
        String targetCurrency,
        BigDecimal rate,
        BigDecimal convertedAmount,
        LocalDate rateDate,
        boolean available
) {
    public static InvoiceConversionDto unavailable(String targetCurrency) {
        return new InvoiceConversionDto(targetCurrency, null, null, null, false);
    }
}
