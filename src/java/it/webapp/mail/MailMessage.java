/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author: Willi Menapace <willi.menapace@gmail.com>
 * 
 ******************************************************************************/

package it.webapp.mail;

import javax.mail.Session;

/**
 * An email message
 */
public interface MailMessage {
	
	/**
	 * Gets the associated Message
	 * @param session The session that will be used to send the message
	 * @return Message representation of the MailMessage
	 */
	javax.mail.Message getMessage(Session session);
	
}
