package springbootapp.minivms.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import springbootapp.minivms.model.dto.paymentdto.ExchangeRateResponse;
import springbootapp.minivms.model.dto.paymentdto.InvoiceConversionDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

@Service
public class ExchangeRateService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);

    private final RestClient restClient;

    public ExchangeRateService(
            RestClient.Builder restClientBuilder,
            @Value("${app.exchange-rate.base-url:https://api.frankfurter.dev}") String baseUrl) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public InvoiceConversionDto convertInvoiceAmount(BigDecimal amount, String sourceCurrency, String targetCurrency) {
        if (amount == null || sourceCurrency == null || targetCurrency == null) {
            return InvoiceConversionDto.unavailable(targetCurrency);
        }

        String from = sourceCurrency.trim().toLowerCase(Locale.ROOT);
        String to = targetCurrency.trim().toLowerCase(Locale.ROOT);

        if (from.equals(to)) {
            return new InvoiceConversionDto(
                    to.toUpperCase(Locale.ROOT),
                    BigDecimal.ONE,
                    amount.setScale(2, RoundingMode.HALF_UP),
                    null,
                    true
            );
        }

        try {
            ExchangeRateResponse response = restClient.get()
                    .uri("/v2/rate/{from}/{to}", from, to)
                    .retrieve()
                    .body(ExchangeRateResponse.class);

            if (response == null || response.rate() == null) {
                return InvoiceConversionDto.unavailable(to.toUpperCase(Locale.ROOT));
            }

            BigDecimal converted = amount.multiply(response.rate())
                    .setScale(2, RoundingMode.HALF_UP);

            return new InvoiceConversionDto(
                    to.toUpperCase(Locale.ROOT),
                    response.rate(),
                    converted,
                    response.date(),
                    true
            );
        } catch (Exception ex) {
            log.warn("Failed to fetch exchange rate for {} -> {}: {}", from, to, ex.getMessage());
            return InvoiceConversionDto.unavailable(to.toUpperCase(Locale.ROOT));
        }
    }
}