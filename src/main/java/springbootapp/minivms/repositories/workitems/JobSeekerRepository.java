package springbootapp.minivms.repositories.workitems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.workitems.JobSeeker;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker, UUID> {

    List<JobSeeker> getJobSeekerBySupplier(Supplier supplier);

    JobSeeker getJobSeekerByJobSeekerId(String jobSeekerId);

    long countBySupplier(Supplier supplier);

    List<JobSeeker> getAllBySupplier_Uuid(UUID supplierUuid);
}
