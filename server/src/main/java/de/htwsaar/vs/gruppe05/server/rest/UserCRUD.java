package de.htwsaar.vs.gruppe05.server.rest;

import de.htwsaar.vs.gruppe05.server.DTO.UserDto;
import de.htwsaar.vs.gruppe05.server.mapper.UserMapper;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.service.LogService;
import de.htwsaar.vs.gruppe05.server.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Users Endpoint
 * - CRUD Users
 * - Admin User Endpoints
 *
 * @version 20.02.2023
 */
@RequestMapping("/api/users")
@RestController()
@SecurityRequirement(name = "Bearer Authentication")
public class UserCRUD {

    private final UserService userService;
    private final UserMapper userMapper;
    private final LogService logService;


    public UserCRUD(UserService userService, UserMapper userMapper, LogService logService) {
        this.userService = userService;
        this.userMapper = userMapper;
		this.logService = logService;
    }

    /**
     * Get User by ID
     *
     * @param id
     * @return the specific UserDto
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        logService.createUserLog("User with "+ user.getId()+" requested User by ID");
        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
    }

    /**
     * 
     * Get every registered user in the system
     *
     * @return the list of registered users
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDto>> getUsers() {
    	logService.createUserLog("Admin requested every registered user in the system");
        return new ResponseEntity<>(userService.getAllUsers()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * [ADMIN]
     * Let an admin create an account
     *
     * @param userDto - the new user
     * @return the specific UserDto
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userMapper.toEntity(userDto));
    	logService.createUserLog("Admin created an User account");

        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.CREATED);
    }

    /**
     * [ADMIN]
     * Let an admin update a user account
     *
     * @param id
     * @param userDto
     * @return the updated user account
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userService.updateUser(userMapper.toEntity(userDto), id);
    	logService.createUserLog("Admin updated an User account");
    	return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.ACCEPTED);
    }

    /**
     * Let a user update his/her account
     *
     * @param userDto - the new data
     * @return the updated account
     */
    @PutMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userService.updateUser(userMapper.toEntity(userDto), user.getId());
        logService.createUserLog("User with "+ user.getId()+" updated account");
        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.ACCEPTED);
    }

    /**
     * Let a user delete his/her account
     *
     * @return the deleted account
     */
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<UserDto> deleteUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User deletedUser = userService.deleteUserById(user.getId());
        logService.createUserLog("User with "+ user.getId()+" deleted account");
        return new ResponseEntity<>(userMapper.toDto(deletedUser), HttpStatus.NO_CONTENT);
    }

    /**
     * [ADMIN]
     * Delete a user by its id
     *
     * @param id the userId
     * @return the deleted account
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        User deletedUser = userService.deleteUserById(id);
        logService.createUserLog("Admin deleted user account");
        return new ResponseEntity<>(userMapper.toDto(deletedUser), HttpStatus.NO_CONTENT);
    }


}
