package springbootapp.minivms.services.personservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.mappers.UserMapper;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.persondto.ProfileDetailDto;
import springbootapp.minivms.repositories.personrepositories.BuyerRepository;
import springbootapp.minivms.repositories.personrepositories.SupplierRepository;
import springbootapp.minivms.repositories.personrepositories.WorkerRepository;

import java.util.UUID;

@Service
public class ProfileService {

    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final WorkerRepository workerRepository;
    private final UserMapper userMapper;


    @Autowired
    public ProfileService(BuyerRepository buyerRepository, SupplierRepository supplierRepository,
                          WorkerRepository workerRepository,
                          UserMapper userMapper) {
        this.buyerRepository = buyerRepository;
        this.supplierRepository = supplierRepository;
        this.workerRepository = workerRepository;
        this.userMapper = userMapper;
    }

    public ProfileDetailDto getLoggedUserDetails(LoggedUserDto loggedUserUuid) {
        return this.userMapper.mapLoggedUserToProfileDto(loggedUserUuid);
    }
}
