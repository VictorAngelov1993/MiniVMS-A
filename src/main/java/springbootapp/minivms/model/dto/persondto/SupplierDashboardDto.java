package springbootapp.minivms.model.dto.persondto;

public class SupplierDashboardDto {
    private long jobSeekerCount;
    private long submittedCandidateCount;
    private long activeAssignmentCount;

    public SupplierDashboardDto() {

    }

    public long getJobSeekerCount() {
        return jobSeekerCount;
    }

    public void setJobSeekerCount(long jobSeekerCount) {
        this.jobSeekerCount = jobSeekerCount;
    }

    public long getSubmittedCandidateCount() {
        return submittedCandidateCount;
    }

    public void setSubmittedCandidateCount(long submittedCandidateCount) {
        this.submittedCandidateCount = submittedCandidateCount;
    }

    public long getActiveAssignmentCount() {
        return activeAssignmentCount;
    }

    public void setActiveAssignmentCount(long activeAssignmentCount) {
        this.activeAssignmentCount = activeAssignmentCount;
    }
}
