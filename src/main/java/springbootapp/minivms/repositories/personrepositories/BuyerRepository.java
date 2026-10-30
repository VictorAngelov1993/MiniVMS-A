package springbootapp.minivms.repositories.personrepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springbootapp.minivms.model.entities.persons.Buyer;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, UUID> {

    int countAllByUsername(String username);

   Optional<Buyer> getBuyerByUsername(String username);

    Optional<Buyer> getBuyerByUuid(UUID uuid);

}
