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
import de.htwsaar.vs.gruppe05.server.service.AppointmentService;
import de.htwsaar.vs.gruppe05.server.service.InvitationService;
import de.htwsaar.vs.gruppe05.server.service.LogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Appointment REST Controller
 * <p>
 * Including all endpoints for CRUD-Operations on appointments
 *
 * @version 0.0.1
 */
@RestController
@RequestMapping("/api/appointments")
@SecurityRequirement(name = "Bearer Authentication")
public class AppointmentCRUD {


    private final AppointmentService appointmentService;

    private final AppointmentMapper appointmentMapper;
    
    private final LogService logService;

    private final UserMapper userMapper;

    private final InvitationService invitationService;
    private InvitationMapper invitationMapper;

    public AppointmentCRUD(AppointmentService appointmentService, AppointmentMapper appointmentMapper,  LogService logService, UserMapper userMapper, InvitationService invitationService, InvitationMapper invitationMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
        this.logService = logService;
        this.userMapper = userMapper;
        this.invitationService = invitationService;
        this.invitationMapper = invitationMapper;
    }


    /**
     * [ADMIN]
     * Get all appointments
     *
     * @return A list of all appointments in datasource
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        logService.createAppointmentLog("Admin requested all appointments");
        return new ResponseEntity<>(appointmentService.findAll()
                .stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Creates a new appointment object
     *
     * @param appointmentDto - the to be created appointment
     * @return the saved appointment if successful
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        appointmentDto.setUserId(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        Appointment appointment = appointmentService.createAppointment(appointmentMapper.toEntity(appointmentDto));
        return new ResponseEntity<>(AppointmentMapper.toDto(appointment), HttpStatus.CREATED);
    }

    /**
     * Update appointment details
     *
     * @param appointmentDto - the updated appointment object
     * @return the updates appointment
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<AppointmentDto> updateAppointment(@RequestBody AppointmentDto appointmentDto, @PathVariable Long id) {
        Appointment appointment = appointmentService.updateAppointment(appointmentMapper.toEntity(appointmentDto), id);
        return new ResponseEntity<>(AppointmentMapper.toDto(appointment), HttpStatus.ACCEPTED);
    }


    /**
     * Get all appointments of an authenticated user
     *
     * @return the appointments of an authenticated user
     */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AppointmentDto>> getAllAppointmentsOfUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Appointment> appointmentDtos = appointmentService.getAllAppointmentsOfUser(user.getId());

        return new ResponseEntity<>((appointmentDtos).stream().map(AppointmentMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Admin Endpoint
     * Get all appointments of a specific user
     *
     * @param userId - the specific user
     * @return a list of the users appointments
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AppointmentDto>> getAllAppointmentsOfUser(@PathVariable Long userId,
                                                                         @RequestParam(value = "timestamp", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Appointment> appointmentList;

        if (date == null) {
            appointmentList = appointmentService.getAllAppointmentsOfUser(userId);
        } else {
            appointmentList = appointmentService.getAllAppointmentsOfUserSinceDate(date, userId);
        }
        return new ResponseEntity<>(appointmentList
                .stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get one appointment by id
     *
     * @param id the id of the appointment
     * @return the appointmentdto
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AppointmentDto> getOneAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.findAppointmentById(id);
        return new ResponseEntity<>(AppointmentMapper.toDto(appointment), HttpStatus.OK);
    }

    /**
     * delete appointment by id
     *
     * @param id the id of the appointment
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<AppointmentDto> deleteOneAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.deleteAppointmentById(id);
        return new ResponseEntity<>(AppointmentMapper.toDto(appointment), HttpStatus.NO_CONTENT);
    }

    /**
     * Get Users by appointmentId
     *
     * @param appointmentId the invitationId
     * @return the appointment
     */
    @GetMapping("/{appointmentId}/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDto>> getUsersByAppointmentId(@PathVariable Long appointmentId) {
        List<Invitation> invitations = invitationService.getInvitationsByAppointmentId(appointmentId);
        List<User> userList = new ArrayList<>();
        invitations.forEach(invitation -> userList.add(invitation.getUser()));
        return new ResponseEntity<>(userList.stream().map(userMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get Users by appointmentId
     *
     * @param appointmentId the invitationId
     * @return the appointment
     */
    @GetMapping("/{appointmentId}/invitations")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InvitationDto>> getInvitationsByAppointmentId(@PathVariable Long appointmentId) {
        List<Invitation> invitations = invitationService.getInvitationsByAppointmentId(appointmentId);
        return new ResponseEntity<>(invitations.stream().map(invitationMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }


}
