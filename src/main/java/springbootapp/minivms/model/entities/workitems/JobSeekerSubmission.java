package springbootapp.minivms.model.entities.workitems;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class JobSeekerSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    private JobPosting jobPosting;

    @ManyToOne
    private JobSeeker jobSeeker;

    public JobSeekerSubmission() {

    }

    public UUID getUuid() {
        return uuid;
    }


    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }

    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }
}
