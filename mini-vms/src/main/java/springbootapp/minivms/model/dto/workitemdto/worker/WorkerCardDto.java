package springbootapp.minivms.model.dto.workitemdto.worker;

public class WorkerCardDto {
        private String id;
        private String fullName;
        private String contactInfo;
        private int assignedWorkOrderCount;

    public WorkerCardDto() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public int getAssignedWorkOrderCount() {
        return assignedWorkOrderCount;
    }

    public void setAssignedWorkOrderCount(int assignedWorkOrderCount) {
        this.assignedWorkOrderCount = assignedWorkOrderCount;
    }
}
