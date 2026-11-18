package springbootapp.minivms.model.dto.persondto;

import java.util.UUID;

public class SupplierDropDownDto {
    private UUID uuid;
    private String fullName;

    public SupplierDropDownDto() {

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
