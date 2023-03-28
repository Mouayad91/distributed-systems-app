package de.htwsaar.vs.gruppe05.server.mapper;

import de.htwsaar.vs.gruppe05.server.DTO.UserDto;
import de.htwsaar.vs.gruppe05.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public User toEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setUserName(userDto.getUserName());
        user.setRole(userDto.getRole());
        user.setEnabled(userDto.isEnabled());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        return user;
    }

    public UserDto toDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setRole(user.getRole());
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setCreationDate(user.getCreationDate());
        userDto.setPassword("how dare you");
        userDto.setEnabled(user.isEnabled());
        userDto.setLastLoginDate(user.getLastLoginDate());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        return userDto;
    }

}
