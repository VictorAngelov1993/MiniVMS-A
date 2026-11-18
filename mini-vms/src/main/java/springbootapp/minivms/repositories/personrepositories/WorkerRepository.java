package springbootapp.minivms.repositories.personrepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springbootapp.minivms.model.entities.persons.Worker;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, UUID> {

    int countAllByUsername(String username);

    Optional<Worker> getWorkerByUsername(String username);

    Optional<Worker> getWorkerByUuid(UUID uuid);

    Optional<Worker> getWorkerByWorkerId(String workerId);
}
