package de.htwsaar.vs.gruppe05.server.repository;

import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    @Override
    List<Invitation> findAll();

    Optional<Invitation> findById(long id);

    // Generates us sql query for finding invitation by user id
    Optional<List<Invitation>> findByUserId(long userId);

    // Generates us sql query for finding invitation by termin id
    Optional<List<Invitation>> findInvitationsByAppointmentId(long terminId);



    Optional<List<Invitation>> findAllByUserIdAndStatus(long userId, StatusEnums.InvitationStatus status);

    @Query(value = "select * from invitations",nativeQuery = true)
    Optional<List<Invitation>> findPendingInvitationsOf(long userId);

}
