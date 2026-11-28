package springbootapp.minivms.repositories.personrepositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springbootapp.minivms.model.entities.persons.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    int countAllByUsername(String username);

    Optional<Supplier> getSupplierByUsername(String username);

    Optional<Supplier> getSupplierByUuid(UUID uuid);

    @Query("""
    select s from Supplier as s
    join s.workOrders as wo
    join wo.jobPosting as jp
    where jp.buyer.uuid = :buyerUuid
""")
    List<Supplier> findDistinctByWorkOrderBuyerUuid(@Param("buyerUuid") UUID buyerUuid);
}
