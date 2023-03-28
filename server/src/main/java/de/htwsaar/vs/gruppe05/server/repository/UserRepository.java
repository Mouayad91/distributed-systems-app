package de.htwsaar.vs.gruppe05.server.repository;

import de.htwsaar.vs.gruppe05.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);


    Optional<User> findByUserName(String username);


    @Override
    List<User> findAll();

    Optional<User> findByEmail(String email);

}
