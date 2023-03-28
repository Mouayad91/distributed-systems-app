package de.htwsaar.vs.gruppe05.server.service;

import de.htwsaar.vs.gruppe05.server.exceptions.EmptyResponseException;
import de.htwsaar.vs.gruppe05.server.exceptions.EntityAlreadyExistsException;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * UserService
 *
 * @version 20.02.2023
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get user by long id

    /**
     * get user by id
     *
     * @param id the user i d
     * @return the user
     */
    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw (new EntityNotFoundException("No User found with {" + id + "}"));
        }

    }

    /**
     * Get all available users from datasource
     *
     * @return all users
     */
    public List<User> getAllUsers() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new EmptyResponseException("No User Entry found in User-Table");
        }
        return userList;
    }


    /**
     * Update user
     *
     * @param newUser the user
     * @param id      the userId
     * @return the updated user
     */
    public User updateUser(User newUser, Long id) {
        return userRepository.findById(id).map(
                user -> {
                    user.setEnabled(newUser.isEnabled());
                    user.setUserName(newUser.getUserName());
                    user.setRole(newUser.getRole());
                    user.setEmail(newUser.getEmail());
                    if(!newUser.getPassword().equals("how dare you")){
                        user.setPassword(newUser.getPassword());
                    }
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(() -> new EntityNotFoundException("No User found with {" + id + "}"));
    }


    /**
     * Create user
     *
     * @param user the user
     * @return the created user
     */
    public User createUser(User user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent()){
            throw new EntityAlreadyExistsException("User with username:{ " + user.getUserName() + " } already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EntityAlreadyExistsException("User with email:{ " + user.getEmail() + " } already exists");
        }
        return userRepository.save(user);
    }

    /**
     * delete user by id
     *
     * @param id the userId
     * @return the deleted user object
     */
    public User deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No User found with {" + id + "}"));
        userRepository.deleteById(id);
        return user;
    }
}
