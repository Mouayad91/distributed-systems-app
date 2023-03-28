package de.htwsaar.vs.gruppe05.server.mapper;

import de.htwsaar.vs.gruppe05.server.DTO.AppointmentDto;
import de.htwsaar.vs.gruppe05.server.model.Appointment;
import de.htwsaar.vs.gruppe05.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class AppointmentMapper {

    @Autowired
    private UserService userService;

    public Appointment toEntity(AppointmentDto appointmentDto){
        Appointment appointment = new Appointment();
        appointment.setTitle(appointmentDto.getTitle());
        appointment.setDescription(appointmentDto.getDescription());
        appointment.setCreator(userService.getUserById(appointmentDto.getUserId()));
        appointment.setLocation(appointmentDto.getLocation());
        appointment.setStartTime(appointmentDto.getStartTime());
        appointment.setEndTime(appointmentDto.getEndTime());
        return appointment;
    }

    public static AppointmentDto toDto(Appointment appointment){
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setCreationDate(appointment.getCreationDate());
        appointmentDto.setLastEditDate(appointment.getLastEditDate());
        appointmentDto.setUserId(appointment.getCreator().getId());
        appointmentDto.setLocation(appointment.getLocation());
        appointmentDto.setStartTime(appointment.getStartTime());
        appointmentDto.setEndTime(appointment.getEndTime());
        appointmentDto.setDescription(appointment.getDescription());
        appointmentDto.setTitle(appointment.getTitle());
        return appointmentDto;
    }

}
