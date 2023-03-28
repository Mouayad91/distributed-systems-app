package de.htwsaar.vs.gruppe05.server.mapper;

import de.htwsaar.vs.gruppe05.server.DTO.InvitationDto;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import de.htwsaar.vs.gruppe05.server.service.AppointmentService;
import de.htwsaar.vs.gruppe05.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class InvitationMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    public Invitation toEntity(InvitationDto invitationDto){
        Invitation invitation = new Invitation();
        invitation.setId(invitationDto.getId());
        invitation.setStatus(invitationDto.getStatus());
        invitation.setUser(userService.getUserById(invitationDto.getUserId()));
        invitation.setAppointment(appointmentService.findAppointmentById(invitationDto.getAppointmentId()));
        return invitation;
    }

    public InvitationDto toDto(Invitation invitation){
        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setId(invitation.getId());
        invitationDto.setStatus(invitation.getStatus());
        invitationDto.setCreationDate(invitation.getCreationDate());
        invitationDto.setAppointmentId(invitation.getAppointment().getId());
        invitationDto.setUserId(invitation.getUser().getId());
        return invitationDto;
    }

}
