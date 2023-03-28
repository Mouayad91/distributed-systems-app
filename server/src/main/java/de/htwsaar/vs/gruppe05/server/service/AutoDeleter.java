package de.htwsaar.vs.gruppe05.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoDeleter {

    private static final Logger logger = LoggerFactory.getLogger(AutoDeleter.class);
    @Autowired
    InvitationService invitationService;

    @Scheduled(fixedRate = 300000) // Triggers after every 5 Minuten
    public void deletePastInvitations() {
        try {
            // Delete all invitations older than current date
            invitationService.deletePastInvitations();
            logger.info("Auto-Deletion of past invitation done");
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

}
