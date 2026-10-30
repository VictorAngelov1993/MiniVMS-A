package springbootapp.minivms.mappers;

import org.springframework.stereotype.Component;
import springbootapp.minivms.model.dto.persondto.LoggedUserDto;
import springbootapp.minivms.model.dto.persondto.ProfileDetailDto;

@Component
public class UserMapper {

    public ProfileDetailDto mapLoggedUserToProfileDto(LoggedUserDto loggedUser) {
        ProfileDetailDto profileDetailDto = new ProfileDetailDto();
        profileDetailDto.setFullName(loggedUser.getFullName());
        profileDetailDto.setEmail(loggedUser.getEmail());
        profileDetailDto.setRole(loggedUser.getRole());
        profileDetailDto.setRoleHomeUrl(loggedUser.getRoleHomeUrl());
        return profileDetailDto;
    }
}
