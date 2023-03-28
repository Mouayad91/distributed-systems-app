package de.htwsaar.vs.gruppe05.server.service;

import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.exceptions.AppointmentCollisionException;
import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.exceptions.AppointmentCollisionException;
import de.htwsaar.vs.gruppe05.server.exceptions.EntityAlreadyExistsException;
import de.htwsaar.vs.gruppe05.server.model.Appointment;
import de.htwsaar.vs.gruppe05.server.model.Invitation;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.InvitationRepository;
import jakarta.mail.Address;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @RequiredArgsConstructor needed for unit testing
 */
@Service
public class InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private LogService logService;


    public Invitation getInvitationById(long id) {
        return invitationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Invitation found with {" + id + "}"));
    }

    public Appointment getAppointmentByInvitationId(long id) {
        Invitation inv = invitationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Invitation found with {" + id + "}"));
        return inv.getAppointment();
    }

    public User getUserByInvitationId(long id) {
        Invitation inv = invitationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Invitation found with {" + id + "}"));
        return inv.getUser();
    }


    // Get list of invitation by user id
    public List<Invitation> getInvitationsByUserId(long userId) {
        return invitationRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("No Invitation found for User with id:{" + userId + "}"));
    }

    // Get list of invitiation by termin id
    public List<Invitation> getInvitationsByAppointmentId(long terminId) {
            return invitationRepository.findInvitationsByAppointmentId(terminId).orElseThrow(() -> new EntityNotFoundException("No Invitation found for Appointment with id:{" + terminId + "}"));
    }


    public Optional<List<Invitation>> findPendingInvitationsOf(long userId) {
        return invitationRepository.findAllByUserIdAndStatus(userId, StatusEnums.InvitationStatus.PENDING);
    }

    public void deletePastInvitations() {
   //     invitationRepository.deletePastInvitations();
    }

    public Invitation createInvitation(Invitation invitation) {
        if((invitation.getAppointment().getCreator() == invitation.getUser())){
            throw new AppointmentCollisionException(invitation.toString() + " creator can not be invitee",invitation.getAppointment(),invitation.getAppointment());
        }
        getInvitationsByUserId(invitation.getUser().getId()).forEach(invitation1 -> {
/**            if(invitation1.getAppointment().checkCollision(invitation.getAppointment())){
                throw new AppointmentCollisionException(invitation.toString() + " collides with appointment of user with {" + invitation.getUser().getId(), invitation.getAppointment(), invitation1.getAppointment());
            }**/
        });
        invitation.setStatus(StatusEnums.InvitationStatus.PENDING);

        Invitation newInvitation = invitationRepository.save(invitation);
        sendInvitationMail(newInvitation);
        return newInvitation;
    }

    public Invitation updateInvitation(Invitation newInvitation, Long id) {
        return invitationRepository.findById(id).map(
                invitation -> {
                    invitation.setUser(newInvitation.getUser());
                    invitation.setAppointment(newInvitation.getAppointment());
                    invitation.setStatus(newInvitation.getStatus());
                    return invitationRepository.save(invitation);
                }).orElseThrow(() -> new EntityNotFoundException("No Invitation found with {" + id + "}"));
    }

    public Invitation deleteInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Invitation found with {" + id + "}"));
        invitationRepository.deleteById(id);
        return invitation;
    }




    /**
     *  Method that constructs text from invitation object and sends email to user
     * @param invitation Invitation Object
     */
    public void sendInvitationMail(Invitation invitation) {
        try {


            String to = invitation.getUser().getEmail();
            String text = "<!doctype html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <title>EmailTemplate-Responsive</title>\n" +
                    "    <style type=\"text/css\">\n" +
                    "html,  body {\n" +
                    "\tmargin: 0 !important;\n" +
                    "\tpadding: 0 !important;\n" +
                    "\theight: 100% !important;\n" +
                    "\twidth: 100% !important;\n" +
                    "}\n" +
                    "* {\n" +
                    "\t-ms-text-size-adjust: 100%;\n" +
                    "\t-webkit-text-size-adjust: 100%;\n" +
                    "}\n" +
                    "\n" +
                    "table,  td {\n" +
                    "\tmso-table-lspace: 0pt !important;\n" +
                    "\tmso-table-rspace: 0pt !important;\n" +
                    "}\n" +
                    "table {\n" +
                    "\tborder-spacing: 0 !important;\n" +
                    "\tborder-collapse: collapse !important;\n" +
                    "\ttable-layout: fixed !important;\n" +
                    "\tmargin: 0 auto !important;\n" +
                    "}\n" +
                    "table table table {\n" +
                    "\ttable-layout: auto;\n" +
                    "}\n" +
                    "</style>\n" +
                    "\n" +
                    "    <style type=\"text/css\">\n" +
                    "        \n" +
                    "        .button-td,\n" +
                    "        .button-a {\n" +
                    "            transition: all 100ms ease-in;\n" +
                    "        }\n" +
                    "        .button-td:hover,\n" +
                    "        .button-a:hover {\n" +
                    "            background: #555555 !important;\n" +
                    "            border-color: #555555 !important;\n" +
                    "        }\n" +
                    "        @media screen and (max-width: 600px) {\n" +
                    "\n" +
                    "            .email-container {\n" +
                    "                width: 100% !important;\n" +
                    "            }\n" +
                    "            .fluid,\n" +
                    "            .fluid-centered {\n" +
                    "                max-width: 100% !important;\n" +
                    "                height: auto !important;\n" +
                    "                margin-left: auto !important;\n" +
                    "                margin-right: auto !important;\n" +
                    "            }\n" +
                    "            .fluid-centered {\n" +
                    "                margin-left: auto !important;\n" +
                    "                margin-right: auto !important;\n" +
                    "            }\n" +
                    "            .stack-column,\n" +
                    "            .stack-column-center {\n" +
                    "                display: block !important;\n" +
                    "                width: 100% !important;\n" +
                    "                max-width: 100% !important;\n" +
                    "                direction: ltr !important;\n" +
                    "            }\n" +
                    "            .stack-column-center {\n" +
                    "                text-align: center !important;\n" +
                    "            }\n" +
                    "        \n" +
                    "            .center-on-narrow {\n" +
                    "                text-align: center !important;\n" +
                    "                display: block !important;\n" +
                    "                margin-left: auto !important;\n" +
                    "                margin-right: auto !important;\n" +
                    "                float: none !important;\n" +
                    "            }\n" +
                    "            table.center-on-narrow {\n" +
                    "                display: inline-block !important;\n" +
                    "            }\n" +
                    "                \n" +
                    "        }\n" +
                    "\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body bgcolor=\"#e0e0e0\" width=\"100%\" style=\"margin: 0;\" yahoo=\"yahoo\">\n" +
                    "    <table bgcolor=\"#e0e0e0\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" style=\"border-collapse:collapse;\">\n" +
                    "      <tr>\n" +
                    "        <td><center style=\"width: 100%;\">\n" +
                    "            <table align=\"center\" width=\"600\" class=\"email-container\">\n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 20px 0; text-align: center\">&nbsp;</td>\n" +
                    "              </tr>\n" +
                    "          </table>\n" +
                    "            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" class=\"email-container\">\n" +
                    " \n" +
                    "            <tr>\n" +
                    "                <td style=\"padding: 40px; text-align: center; font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #555555;\"> <p>Hello " + invitation.getUser().getFirstName() + " " + invitation.getUser().getLastName() + " (" + invitation.getUser().getUserName() + " )"  + ",<br>\n" +
                    "                </p>\n" +
                    "                  <p>you have received an invitation to the following appointment:</p>\n" +
                    "                  <p> Title:" + invitation.getAppointment().getTitle() + "</p>\n" +
                    "                  <p> Startzeit:" + invitation.getAppointment().getStartTime() + "</p>\n" +
                    "                  <p> Endzeit:" + invitation.getAppointment().getEndTime() + "</p>\n" +
                    "                  <p> Location: " + invitation.getAppointment().getLocation() + "</p>\n" +
                    "                  <p> Ersteller: " + invitation.getAppointment().getCreator().getFirstName() + " " + invitation.getAppointment().getCreator().getLastName() + "</p>\n" +
                    "                  <p> Beschreibung: " + invitation.getAppointment().getDescription() + "</p>\n" +
                    "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"margin: auto\">\n" +
                    "                  <tr> </tr>\n" +
                    "                  </table>\n" +
                    "\t\t\t\t</td>\n" +
                    "              </tr>\n" +
                    "          </table>\n" +
                    "<table align=\"center\" width=\"600\" class=\"email-container\">\n" +
                    "          <tr>\n" +
                    "                <td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\"><p> <br>\n" +
                    "                    <br>\n" +
                    "                    <span class=\"mobile-link--footer\">VS Gruppe 5</span></p>\n" +
                    "                  <p><span class=\"mobile-link--footer\">Hochschule für Technik und Wirtschaft des Saarlandes - PIB-VS&nbsp;&nbsp;</span> <br>\n" +
                    "                    <br>\n" +
                    "                  </p></td>\n" +
                    "              </tr>\n" +
                    "          </table>\n" +
                    "          </center></td>\n" +
                    "      </tr>\n" +
                    "    </table>\n" +
                    "</body>\n" +
                    "</html>\n";



            mailService.sendEmail("New Appointment Invitation", text, to);
            logService.createInvitationLog("Invitation email with ID:" + invitation.getAppointment().getId() + " sent to " + to);
        } catch(MailException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
