package de.htwsaar.vs.gruppe05.server.invitation;

import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.exceptions.EntityAlreadyExistsException;
import de.htwsaar.vs.gruppe05.server.model.Appointment;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.InvitationRepository;
import de.htwsaar.vs.gruppe05.server.service.InvitationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for InvitationServiceTest => Inject and Mock repositories
 */
@ExtendWith(MockitoExtension.class)
public class InvitationServiceTest {
    @Mock
    private InvitationRepository invitationRepository;
    @InjectMocks
    private InvitationService invitationService;

    /**
     * Test get invitation by id
     */
    @Test
    void testGetInvitiationById() {
        Invitation invitation = new Invitation(1, 1, 1);
        when(invitationRepository.findById(invitation.getId())).thenReturn(Optional.of(invitation));

        Invitation invitation1 = invitationService.getInvitationById(invitation.getId());

        assertThat(invitation).isEqualTo(invitation1);
    }

    /**
     * Test get invitations by user id
     */
    @Test
    void testGetInvitationByUserId() {
        User max = new User(1111, "Max", "max@mail.com", "Max", "Mustermann", "password", LocalDateTime.now(), LocalDateTime.now(), true, StatusEnums.Role.USER);
        Appointment appointment = new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        List<Invitation> invitations = Arrays.asList(new Invitation(1, (int) max.getId(), (int) appointment.getId()));

        when(invitationRepository.findByUserId(max.getId())).thenReturn(Optional.of(invitations));

        List<Invitation> invs = invitationService.getInvitationsByUserId(max.getId());

        assertThat(invitations.size()).isEqualTo(invs.size());
    }

    /**
     * Test get invitations by pending status and user
     */
    @Test
    void testFindPendingInvitationsOfUser() {
        User max = new User(1111, "Max", "max@mail.com", "Max", "Mustermann", "password", LocalDateTime.now(), LocalDateTime.now(), true, StatusEnums.Role.USER);
        Appointment appointment = new Appointment(123234, "Meeting with Elon Musk", "Buy Twitter from him", LocalDateTime.now(), LocalDateTime.now(), max, "Silicon Valley", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        List<Invitation> invitations = Arrays.asList(new Invitation(StatusEnums.InvitationStatus.PENDING.ordinal(), (int) max.getId(), (int) appointment.getId()));

        when(invitationRepository.findAllByUserIdAndStatus(max.getId(), StatusEnums.InvitationStatus.PENDING)).thenReturn(Optional.of(invitations));

        Optional<List<Invitation>> invs = invitationService.findPendingInvitationsOf(max.getId());

        assertThat(invitations.size()).isEqualTo(invs.get().size());
    }

    /**
     * Test duplicate error thrown when creating invitation
     */
    @Test()
    void testCreateDuplicateError() {
        Invitation invitation = new Invitation(StatusEnums.InvitationStatus.PENDING.ordinal(), 20, 10);

        when(invitationRepository.findById(invitation.getId())).thenReturn(Optional.of(invitation));

        Throwable exception = assertThrows(EntityAlreadyExistsException.class, () -> invitationService.createInvitation(invitation));
        assertEquals("Invitation with id:{ " + invitation.getId() + " } already exists", exception.getMessage());
    }

    /**
     * Test exception being thrown when invitation not exist
     */
    @Test
    void testExceptionInDelete() {
        Invitation invitation = new Invitation(StatusEnums.InvitationStatus.PENDING.ordinal(), 20, 10);

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> invitationService.deleteInvitation(invitation.getId()));
        assertEquals("No Invitation found with {" + invitation.getId() + "}", exception.getMessage());
    }

}


