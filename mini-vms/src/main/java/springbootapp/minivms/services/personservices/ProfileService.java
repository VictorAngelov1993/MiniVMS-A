package springbootapp.minivms.services.personservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootapp.minivms.exception.custom.UserNotFoundException;
import springbootapp.minivms.mappers.UserMapper;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.persondto.ProfileDetailDto;
import springbootapp.minivms.model.dto.persondto.ProfileEditDto;
import springbootapp.minivms.model.entities.enums.Role;
import springbootapp.minivms.model.entities.persons.*;
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

    public void editProfile(LoggedUserDto loggedUser, ProfileEditDto editDto) {
        Person userToUpdate;
        if(loggedUser.getRole().equals(Role.BUYER)) {
            userToUpdate = this.buyerRepository.getBuyerByUuid(loggedUser.getUuid())
                    .orElseThrow(() -> new UserNotFoundException("User Not found"));

            this.getDataFromDto(userToUpdate, editDto);
            this.buyerRepository.save((Buyer) userToUpdate);
            this.updateLoggedUserDtoToReflectTheProfileUpdate(loggedUser, userToUpdate);
        }
        if(loggedUser.getRole().equals(Role.SUPPLIER)) {
            userToUpdate = this.supplierRepository.getSupplierByUuid(loggedUser.getUuid())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            this.getDataFromDto(userToUpdate, editDto);
            this.supplierRepository.save((Supplier) userToUpdate);
            this.updateLoggedUserDtoToReflectTheProfileUpdate(loggedUser, userToUpdate);
        }
        if(loggedUser.getRole().equals(Role.WORKER)) {
            userToUpdate = this.workerRepository.getWorkerByUuid(loggedUser.getUuid())
                    .orElseThrow(() -> new UserNotFoundException("User Does Not Exist"));

            this.getDataFromDto(userToUpdate, editDto);
            this.workerRepository.save((Worker) userToUpdate);
            this.updateLoggedUserDtoToReflectTheProfileUpdate(loggedUser, userToUpdate);
        }


    }
    private boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9]+([._-][A-Za-z0-9]+)*@[A-Za-z]+(-[A-Za-z]+)*(\\.[A-Za-z]+(-[A-Za-z]+)*)+$";
         return email.matches(regex);
    }

    private void getDataFromDto(Person userToUpdate, ProfileEditDto editDto) {
        if(!editDto.getFirstName().isEmpty()) {
            userToUpdate.setFirstName(editDto.getFirstName());
        }
        if(!editDto.getLastName().isEmpty()) {
            userToUpdate.setLastName(editDto.getLastName());
        }
        if (!editDto.getEmail().isEmpty()) {
            if(!this.validateEmail(editDto.getEmail())) {
                // TODO Also custom exception for email validation should be created
                throw new IllegalArgumentException("Email not valid");
            } else {
                userToUpdate.setEmail(editDto.getEmail());
            }
        }
    }

    private void updateLoggedUserDtoToReflectTheProfileUpdate(LoggedUserDto loggedUser, Person person) {
        loggedUser.setFullName(person.getFullName());
        loggedUser.setEmail(person.getEmail());
    }




}
