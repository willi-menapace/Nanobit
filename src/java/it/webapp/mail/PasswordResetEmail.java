/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author:
 * 
 ******************************************************************************/

package it.webapp.mail;

import it.webapp.db.entities.UserEntity;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Mail for User password reset
 */
public class PasswordResetEmail implements MailMessage {
    
    private static final String EMAIL_SUBJECT = "Nanobit | Link reset password";
    private static final String EMAIL_TEXT = "Gentile %s,<br><br>Il link per la pagina di reset "
                                           + "della password &egrave; <b><a href='%s'>link<a></b>";
    
    private final String USER_EMAIL;
    private final String USERNAME;
    private final String PASSWORD_RESET_URL;
    
	/**
	 * Builds a new passowrd reset email for the specified user
	 * 
	 * @param user The destination user
     * @param resetPasswordUrl The reset password url
	 */
	public PasswordResetEmail(UserEntity user, String resetPasswordUrl) {
		if(user == null) {
			throw new IllegalArgumentException("User must not be null");
		}
        if(user.getUsername() == null || user.getUsername().isEmpty()){
            throw new IllegalArgumentException("User username must not be null or empty");
        }
        if(user.getEmail() == null || user.getEmail().isEmpty()){
            throw new IllegalArgumentException("User email must not be null or empty");
        }
        if(resetPasswordUrl == null || resetPasswordUrl.isEmpty()){
            throw new IllegalArgumentException("User reset password url must not be null or empty");
        }
        
        // Sets the parameter that are necessary
        this.USER_EMAIL = user.getEmail();
        this.USERNAME = user.getUsername();
		this.PASSWORD_RESET_URL = resetPasswordUrl;
	}
	
	@Override
	public Message getMessage(Session session) {
		if(session == null) {
			throw new IllegalArgumentException("Session must not be null");
		}
        
        // Prepare content of email message
        String emailContent = String.format(EMAIL_TEXT, USERNAME, PASSWORD_RESET_URL);
        
        // Creation of email message
        Message emailMessage = new MimeMessage(session);
        try {
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(USER_EMAIL));
            emailMessage.setSubject(EMAIL_SUBJECT);
            emailMessage.setContent(emailContent, "text/html; charset=utf-8");
        } catch (AddressException exception) {
            throw new IllegalArgumentException("User email address is not valid.", exception);
        } catch (MessagingException exception) {
            throw new IllegalArgumentException("Creation of email message has failed.", exception);
        }
        
        return emailMessage;
	}

}