package de.htwsaar.vs.gruppe05.server.repository;

import de.htwsaar.vs.gruppe05.server.model.Appointment;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<ArrayList<Appointment>> findAppointmentsByIdBetween(long start, long end);

    Optional<ArrayList<Appointment>> getAppointmentsByEndTimeLessThan(LocalDateTime endTime);

    @Query(value = "SELECT * FROM appointments WHERE Date between startDate=?1 AND endDate=?2", nativeQuery = true)
    Optional<ArrayList<Invitation>> findAppointmentsInRange(LocalDate start, LocalDate end);


    Optional<ArrayList<Appointment>> getAppointmentsByStartTime(LocalDateTime startTime);

    Optional<Appointment> findAppointmentById(Long id);

    //  @Query(value = "SELECT * FROM appointments a WHERE a.creator = ?1  OR a.appointmentId IN (SELECT * FROM invitations i WHERE i.userId=?1)",nativeQuery = true)
    @Query(value = "SELECT * FROM appointments a WHERE a.user_id=:id", nativeQuery = true)
    Optional<List<Appointment>> getAppointmentsOfUser(@Param("id") Long userId);

    @Query(value = "SELECT * FROM appointments a WHERE :dateTimeValue BETWEEN a.start_time AND a.end_time AND a.user_id=:id", nativeQuery = true)
    Optional<Appointment> findAppointmentsContainingDateTime(@Param("dateTimeValue") LocalDateTime dateTimeValue, @Param("id") Long userId);

}
