package springbootapp.minivms.config.security;

import org.springframework.stereotype.Component;
import springbootapp.minivms.repositories.workitems.TimeSheetRepository;

@Component
public class TimeSheetSecurity {
    private final TimeSheetRepository timeSheetRepository;

    public TimeSheetSecurity(TimeSheetRepository timeSheetRepository) {
        this.timeSheetRepository = timeSheetRepository;
    }

    // Below method will check if the logged user is the owner of the Time Sheet
    public boolean isOwned(String timeSheetId, String usernameOfLoggedUser) {
        return this.timeSheetRepository.findByTimeSheetId(timeSheetId).map(
                ts -> ts.getWorkOrder().getWorker().getUsername().equals(usernameOfLoggedUser))
                .orElse(false);

    }
}
