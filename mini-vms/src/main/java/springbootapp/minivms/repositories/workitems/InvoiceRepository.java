package springbootapp.minivms.repositories.workitems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springbootapp.minivms.model.entities.workitems.Invoice;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> getAllByBuyer_Uuid(UUID buyerUuid);

    Invoice getInvoiceByInvoiceId(String invoiceId);

    BigDecimal getAmountByInvoiceId(String invoiceId);

    String getSupplierNameByInvoiceId(String invoiceId);
}
