package de.htwsaar.vs.gruppe05.server.service;

import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.exceptions.AppointmentCollisionException;
import de.htwsaar.vs.gruppe05.server.exceptions.EmptyResponseException;
import de.htwsaar.vs.gruppe05.server.exceptions.EntityAlreadyExistsException;
import de.htwsaar.vs.gruppe05.server.model.Appointment;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.AppointmentRepository;
import de.htwsaar.vs.gruppe05.server.repository.InvitationRepository;
import de.htwsaar.vs.gruppe05.server.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @RequiredArgsConstructor needed for unit testing
 */
@Service
@RequiredArgsConstructor
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private InvitationService invitationService;

    public List<Appointment> findAll() {
        List<Appointment> appointmentList = appointmentRepository.findAll();
        if (appointmentList.isEmpty()) {
            throw new EmptyResponseException("No Appointment Entry found in Appointment-Table");
        }
        return appointmentList;
    }

    public Appointment findAppointmentById(Long id) {
        return appointmentRepository.findAppointmentById(id).orElseThrow(() -> new EntityNotFoundException("No Appointment found with {" + id + "}"));
    }

    public Appointment createAppointment(Appointment appointment) {
        if (appointmentRepository.findById(appointment.getId()).isPresent()) {
            throw new EntityAlreadyExistsException("Appointment with id:{ " + appointment.getId() + " } already exists");
        }
        // Collision detection
        Optional<Appointment> appointmentOptional = appointmentRepository.findAppointmentsContainingDateTime(appointment.getStartTime(), appointment.getCreator().getId());
        if (appointmentOptional.isPresent()) {
            throw new AppointmentCollisionException("Appointment with id:{ " + appointment.getId() + " } collides with different appointment", appointment, appointmentOptional.get());
        }


        Appointment createdAppointment = appointmentRepository.save(appointment);

        Invitation invitation = new Invitation();
        invitation.setAppointment(createdAppointment);
        invitation.setStatus(StatusEnums.InvitationStatus.ACCEPTED);
        invitation.setUser(appointment.getCreator());
        invitationRepository.save(invitation);
        //TODO Setup SMTP-Server
        //invitationService.sendInvitationMail(invitation);
        return createdAppointment;
    }

    public Appointment updateAppointment(Appointment newAppointment, long id) {
        return appointmentRepository.findById(id).map(
                appointment -> {
                    appointment.setDescription(newAppointment.getDescription());
                    appointment.setEndTime(newAppointment.getEndTime());
                    appointment.setStartTime(newAppointment.getStartTime());
                    appointment.setLocation(newAppointment.getLocation());
                    appointment.setTitle(newAppointment.getTitle());
                    appointment.setLastEditDate(LocalDateTime.now());
                    return appointmentRepository.save(appointment);
                }).orElseThrow(() -> new EntityNotFoundException("No Appointment found with {" + id + "}"));
    }

    public Appointment deleteAppointmentById(long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Appointment found with {" + id + "}"));
        appointmentRepository.deleteById(id);
        return appointment;
    }

    public List<Appointment> getAllAppointmentsOfUser(Long userId) {
        return appointmentRepository.getAppointmentsOfUser(userId).orElseThrow(() -> new EntityNotFoundException("No Appointments found owned by {" + userId + "}"));
    }

    public List<Appointment> getAllAppointmentsOfUserSinceDate(LocalDateTime start, Long userId) {
        List<Appointment> list = getAllAppointmentsOfUser(userId);
        list.removeIf(appointment -> appointment.getStartTime().toLocalDate().isBefore(start.toLocalDate()));
        return list;
    }
}
