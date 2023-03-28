package de.htwsaar.vs.gruppe05.server.sse;

import de.htwsaar.vs.gruppe05.server.DTO.InvitationDto;
import de.htwsaar.vs.gruppe05.server.mapper.InvitationMapper;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Invitation Notification SSE-Endpoints
 * @version 21.02.2023
 */
@RestController
public class InvitationNotification {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationMapper invitationMapper;

    /**
     * Server Sent Invitation Notification Event
     * @return A list with pending invitations of the authenticated user
     */

    @GetMapping("/api/invitations-sse")
    public Flux<ServerSentEvent<List<InvitationDto>>> streamEvents() {

                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                Optional<List<Invitation>> invitations = invitationService.findPendingInvitationsOf(user.getId());
        return invitations.map(invitationList -> Flux.interval(Duration.ofSeconds(10))
                .map(sequence -> ServerSentEvent.<List<InvitationDto>>builder()
                        .id(String.valueOf(sequence))
                        .event("invitation")
                        .data(invitationService.findPendingInvitationsOf(user.getId()).isPresent() ? invitationService.findPendingInvitationsOf(user.getId()).get().stream().map(e -> invitationMapper.toDto(e)).collect(Collectors.toList()) : new ArrayList<>())
                        .build())).orElseGet(() -> Flux.interval(Duration.ofSeconds(10)).map(sequence -> ServerSentEvent.<List<InvitationDto>>builder()
                .id(String.valueOf(sequence))
                .event("invitation")
                .data(new ArrayList<>())
                .build()));
           }

}
