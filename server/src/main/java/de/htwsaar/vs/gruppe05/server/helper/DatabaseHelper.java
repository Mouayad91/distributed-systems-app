package de.htwsaar.vs.gruppe05.server.helper;

import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.model.Appointment;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.AppointmentRepository;
import de.htwsaar.vs.gruppe05.server.repository.InvitationRepository;
import de.htwsaar.vs.gruppe05.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * Initialize the Database with default values
 */
@Component
public class DatabaseHelper {
    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    public void init() {
  /**      User admin = new User();
        admin.setId(1);
        admin.setEmail("test@test.de");
        admin.setEnabled(true);
        admin.setPassword("$2y$14$P87b/ewR8a8ZC1k9sC6BG.PELqqFgy5EJ19czIAfxt8.PzgbG37KK");
        admin.setUserName("admin");
        admin.setRole(StatusEnums.Role.ADMIN);
        admin.setFirstName("tete");
        admin.setLastName("tete");

        admin = userRepository.save(admin);

        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setTitle("Meeting");
        appointment.setDescription("blabla");
        appointment.setLocation("Room XYZ");
        LocalDateTime aDateTime = LocalDateTime.of(2023,
                Month.JULY, 29, 19, 30, 40);
        LocalDateTime bDateTime = LocalDateTime.of(2023,
                Month.JULY, 29, 22, 30, 40);
        appointment.setStartTime(aDateTime);
        appointment.setEndTime(bDateTime);
        appointment.setCreator(admin);
        appointment.setCreationDate(LocalDateTime.of(2023,
                Month.FEBRUARY, 9, 15, 30, 40));
        appointment.setLastEditDate(LocalDateTime.now());
        appointment = appointmentRepository.save(appointment);

        User testUser = new User();
        testUser.setId(2);
        testUser.setEmail("test2@test.de");
        testUser.setEnabled(true);
        testUser.setPassword("$2y$14$P87b/ewR8a8ZC1k9sC6BG.PELqqFgy5EJ19czIAfxt8.PzgbG37KK");
        testUser.setUserName("testUser");
        testUser.setRole(StatusEnums.Role.USER);
        testUser = userRepository.save(testUser);
        testUser.setFirstName("tete");
        testUser.setLastName("tete");

        Invitation inv = new Invitation();
        inv.setId(apppointment.id);
        inv.setStatus(StatusEnums.InvitationStatus.PENDING);
        inv.setUser(testUser);
        inv.setAppointment(appointment);

        invitationRepository.save(inv);**/

    }

}
