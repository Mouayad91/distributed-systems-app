package de.htwsaar.vs.gruppe05.server.appointment;

import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.model.Appointment;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.AppointmentRepository;
import de.htwsaar.vs.gruppe05.server.repository.InvitationRepository;
import de.htwsaar.vs.gruppe05.server.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for AppointmentService => Inject and Mock repositories
 */
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private InvitationRepository invitationRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    /**
     * Test Creation of Appointment
     */
    @Test
    void testCreateAppointment() {
        User max = new User("Max", "max@mail.com", "Max", "Mustermann");
        Appointment appointment = new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Invitation invitation = new Invitation(1, (int) max.getId(), (int) appointment.getId());
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(invitationRepository.save(any())).thenReturn(invitation);

        Appointment savedAppointment = appointmentService.createAppointment(appointment);

        assertThat(savedAppointment.getId()).isEqualTo(appointment.getId());
    }

    /**
     * Test finding all appointments
     */
    @Test
    void testFindAll() {
        User max = new User("Max", "max@mail.com", "Max", "Mustermann");
        List<Appointment> apps = Arrays.asList(new Appointment("Meeting with Elon Musk", "Buy Twitter from him", max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)), new Appointment("Meeting with Elon Musk", "Buy Twitter from him", max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)), new Appointment("Meeting with Elon Musk", "Buy Twitter from him", max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)), new Appointment("Meeting with Elon Musk", "Buy Twitter from him", max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        );

        when(appointmentRepository.findAll()).thenReturn(apps);

        List<Appointment> listOfApps = appointmentService.findAll();

        assertThat(listOfApps.size()).isEqualTo(apps.size());
    }

    /**
     * Test finding by id
     */
    @Test
    void testFindById() {
        User max = new User("Max", "max@mail.com", "Max", "Mustermann");
        Appointment appointment1 = new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(appointmentRepository.findAppointmentById(appointment1.getId())).thenReturn(Optional.of(appointment1));

        Appointment appointment = appointmentService.findAppointmentById(appointment1.getId());

        assertThat(appointment.getId()).isEqualTo(appointment1.getId());
    }

    /**
     * Test updating appointment
     */
    @Test
    void testUpdateAppointment() {
        User max = new User("Max", "max@mail.com", "Max", "Mustermann");
        Appointment myAppointment = new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(appointmentRepository.findById(myAppointment.getId())).thenReturn(Optional.of(myAppointment));
        when(appointmentRepository.save(any())).then(returnsFirstArg());

        myAppointment.setTitle("UPDATE");
        Appointment updateAppointment = appointmentService.updateAppointment(myAppointment, myAppointment.getId());
        assertThat(myAppointment.getTitle()).isEqualTo(updateAppointment.getTitle());
    }

    /**
     * Test delete appointment
     */
    @Test()
    void testDelete() {
        User max = new User("Max", "max@mail.com", "Max", "Mustermann");
        Appointment myAppointment = new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(appointmentRepository.findById(myAppointment.getId())).thenReturn(Optional.of(myAppointment));

        Appointment deleted = appointmentService.deleteAppointmentById(myAppointment.getId());
        assertThat(deleted.getId()).isEqualTo(myAppointment.getId());
    }

    /**
     * Test getting all appointments for user
     */
    @Test()
    void testGetAllForUser() {
        User max = new User(1111, "Max", "max@mail.com", "Max", "Mustermann", "password", LocalDateTime.now(), LocalDateTime.now(), true, StatusEnums.Role.USER);

        List<Appointment> apps = Arrays.asList(new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)), new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)), new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)), new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)), new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)), new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1)));

        when(appointmentRepository.getAppointmentsOfUser(max.getId())).thenReturn(Optional.of(apps));

        List<Appointment> userApps = appointmentService.getAllAppointmentsOfUser(max.getId());

        assertThat(userApps.size()).isEqualTo(apps.size());
    }
}


