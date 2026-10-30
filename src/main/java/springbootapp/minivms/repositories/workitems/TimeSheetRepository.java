package springbootapp.minivms.repositories.workitems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.workitems.timesheets.TimeSheet;

import java.util.List;
import java.util.UUID;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, UUID> {

    TimeSheet getTimeSheetByTimeSheetId(String timeSheetId);


    @Query("""
    select count(*)
    from TimeSheet as ts
    where ts.workOrder.worker = :worker
    and ts.status in ('DRAFT', 'REJECTED')
""")
    long countAllPendingWorkerTimeSheets(@Param("worker")Worker worker);

    @Query("""
    select ts from TimeSheet as ts
    where ts.workOrder.jobPosting.buyer.uuid = :buyerUuid
    and ts.status = 'SUBMITTED'
    order by ts.weekStartDay
""")
    List<TimeSheet> getAllPendingApprovalTimeSheetsForTheBuyer(@Param("buyerUuid") UUID buyerUuid);

    @Query("""
      select count(*) from TimeSheet as ts
      where ts.workOrder.jobPosting.buyer.uuid = :buyerUuid
      and ts.status = "Submitted"
""")
    long countPendingTimeSheetsForBuyer(UUID buyerUuid);
}
