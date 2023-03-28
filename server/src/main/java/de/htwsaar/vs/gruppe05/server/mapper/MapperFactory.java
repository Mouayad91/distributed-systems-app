package de.htwsaar.vs.gruppe05.server.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperFactory {

    @Bean
    public AppointmentMapper appointmentMapper(){
        return new AppointmentMapper();
    }

    @Bean
    public InvitationMapper invitationMapper(){
        return new InvitationMapper();
    }

    @Bean
    public UserMapper userMapper(){
        return new UserMapper();
    }
}
