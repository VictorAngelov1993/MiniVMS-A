package springbootapp.minivms.repositories.personrepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springbootapp.minivms.model.entities.persons.Supplier;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    int countAllByUsername(String username);

    Optional<Supplier> getSupplierByUsername(String username);

    Optional<Supplier> getSupplierByUuid(UUID uuid);

}
