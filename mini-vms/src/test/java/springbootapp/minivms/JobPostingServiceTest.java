package springbootapp.minivms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.workitems.JobPostingRepository;
import springbootapp.minivms.services.workitemsservices.JobPostingService;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobPostingServiceTest {
    @Mock
    private JobPostingRepository jobPostingRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @InjectMocks
    private JobPostingService jobPostingService;

    @Test
    void shouldReturnJobPostingCountForBuyer() {
        Buyer buyer = new Buyer();

        when(jobPostingRepository.countByBuyer(buyer)).thenReturn(5);

        int result = jobPostingService.countJobPostingsForBuyer(buyer);

        assertEquals(5, result);

        verify(jobPostingRepository).countByBuyer(buyer);
    }

}
