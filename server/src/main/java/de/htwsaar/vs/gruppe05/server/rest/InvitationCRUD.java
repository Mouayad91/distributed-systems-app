package de.htwsaar.vs.gruppe05.server.rest;

import de.htwsaar.vs.gruppe05.server.DTO.AppointmentDto;
import de.htwsaar.vs.gruppe05.server.DTO.InvitationDto;
import de.htwsaar.vs.gruppe05.server.DTO.UserDto;
import de.htwsaar.vs.gruppe05.server.mapper.AppointmentMapper;
import de.htwsaar.vs.gruppe05.server.mapper.InvitationMapper;
import de.htwsaar.vs.gruppe05.server.mapper.UserMapper;
import de.htwsaar.vs.gruppe05.server.model.Appointment;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.service.InvitationService;
import de.htwsaar.vs.gruppe05.server.service.LogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Invitation REST Endpoints (CRUD)
 *
 * @version 20.02.2023
 */
@RestController
@RequestMapping("/api/invitations")
@SecurityRequirement(name = "Bearer Authentication")
public class InvitationCRUD {

    private final InvitationService invitationService;

    private final InvitationMapper invitationMapper;

    private final UserMapper userMapper;

    private final LogService logService;

    public InvitationCRUD(InvitationMapper invitationMapper, InvitationService invitationService, UserMapper userMapper, AppointmentMapper appointmentMapper, LogService logService) {
        this.invitationMapper = invitationMapper;
        this.invitationService = invitationService;
        this.userMapper = userMapper;
        this.logService = logService;
    }

    /**
     * [ADMIN] - Get all user invitations from user
     *
     * @param userId - the id of the user the invitations should be shown
     * @return Optional
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InvitationDto>> getUserInvitations(@PathVariable Long userId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logService.createInvitationLog("User with "+ user.getId()+" requested Invitation information");
        return new ResponseEntity<>(invitationService.getInvitationsByUserId(userId)
                .stream()
                .map(invitationMapper::toDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all user invitations from user. User needs to be authenticated
     *
     * @return Optional empty list of invitations of the authenticated user
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InvitationDto>> getAuthenticatedUserInvitations() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logService.createInvitationLog("User with "+ user.getId()+" requested all Invitations from user");
        return new ResponseEntity<>(invitationService.getInvitationsByUserId(user.getId())
                .stream()
                .map(invitationMapper::toDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    /**
     * Get an invitation by its id
     *
     * @param id the id of the invitation
     * @return the invitation
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InvitationDto> getInvitationById(@PathVariable Long id) {
        Invitation invitation = invitationService.getInvitationById(id);
        return new ResponseEntity<>(invitationMapper.toDto(invitation), HttpStatus.OK);
    }


    /**
     * Get user by invitationId
     *
     * @param id the invitationId
     * @return the UserDto
     */
    @GetMapping("/{id}/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> getUserByInvitationById(@PathVariable Long id) {
        User user = invitationService.getUserByInvitationId(id);
        logService.createInvitationLog("User with "+ user.getId()+" requested user by invitation id");

        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
    }

    /**
     * Get an appointment by invitationId
     *
     * @param id the invitationId
     * @return the appointment
     */
    @GetMapping("/{id}/appointments")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AppointmentDto> getAppointmentByInvitationById(@PathVariable Long id) {
        Appointment appointment = invitationService.getAppointmentByInvitationId(id);
        return new ResponseEntity<>(AppointmentMapper.toDto(appointment), HttpStatus.OK);
    }

    /**
     * Get Users by appointmentId
     *
     * @param appointmentId the invitationId
     * @return the appointment
     */
    /*
    @GetMapping("/appointments/{appointmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDto>> getUsersByAppointmentId(@PathVariable Long appointmentId) {
        List<Invitation> invitations = invitationService.getInvitationsByAppointmentId(appointmentId);
        List<User> userList = new ArrayList<>();
        invitations.forEach(invitation -> userList.add(invitation.getUser()));
        return new ResponseEntity<>(userList.stream().map(userMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }*/

    /**
     * Update an Invitation
     *
     * @param invitationDto - the invitation object
     * @return the updated invitation object
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InvitationDto> createInvitation(@RequestBody InvitationDto invitationDto) {
        Invitation invitation = invitationService.createInvitation(invitationMapper.toEntity(invitationDto));
        return new ResponseEntity<>(invitationMapper.toDto(invitation), HttpStatus.CREATED);
    }

    /**
     * Create an invitation
     *
     * @param invitationDto the invitationDto
     * @param id            the user id to invite
     * @return the invitation object
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<InvitationDto> createInvitation(@RequestBody InvitationDto invitationDto, @PathVariable Long id) {
        Invitation invitation = invitationService.updateInvitation(invitationMapper.toEntity(invitationDto), id);
        return new ResponseEntity<>(invitationMapper.toDto(invitation), HttpStatus.ACCEPTED);
    }


    /**
     * Delete an invitation by its id
     *
     * @param id the invitationId
     * @return the deleted invitation object
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<InvitationDto> deleteInvitation(@PathVariable Long id) {
        Invitation invitation = invitationService.deleteInvitation(id);
        return new ResponseEntity<>(invitationMapper.toDto(invitation), HttpStatus.NO_CONTENT);
    }
}
