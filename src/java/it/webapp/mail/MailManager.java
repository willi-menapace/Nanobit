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

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

/**
 * Helper class that manages email operations
 */
public class MailManager {

	private static final String HOST_PROPERTY = "mail.smtp.host";
	private static final String PORT_PROPERTY = "mail.smtp.port";
	private static final String SOCKETFACTORY_PORT_PROPERTY = "mail.smtp.socketFactory.port";
	private static final String SOCKETFACTORY_CLASS_PROPERTY = "mail.smtp.socketFactory.class";
	private static final String AUTH_PROPERTY = "mail.smtp.auth";
	private static final String STARTTLS_PROPERTY = "mail.smtp.starttls.enable";
	private static final String DEBUG_PROPERTY = "mail.debug";
	
	private static final String HOST = "smtp.gmail.com";
	private static final String PORT = "465";
	private static final String SOCKETFACTORY_PORT = "465";
	private static final String SOCKETFACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";
	private static final String AUTH = "true";
	private static final String STARTTLS = "true";
	private static final String DEBUG = "true";
	
	private static Properties properties;
	private static String username;
	private static String password;
	
	private MailManager() {
		//Enforces non instantiability
	}
	
	/**
	 * Initializes the manager
	 * 
	 * @param usernameParam Username of the account to use for sending emails
	 * @param passwordParam Password for the account to use
	 */
	public static synchronized void init(String usernameParam, String passwordParam) {
		if(properties != null) {
			throw new IllegalStateException("MailManager must be initialized only once");
		}
		
		if(usernameParam == null || passwordParam == null || usernameParam.isEmpty() || passwordParam.isEmpty()) {
			throw new IllegalArgumentException("Parameters must be non null, non empty strings");
		}
		
		properties = new Properties();
		properties.setProperty(HOST_PROPERTY, HOST);
		properties.setProperty(PORT_PROPERTY, PORT);
		properties.setProperty(SOCKETFACTORY_PORT_PROPERTY, SOCKETFACTORY_PORT);
		properties.setProperty(SOCKETFACTORY_CLASS_PROPERTY, SOCKETFACTORY_CLASS);
		properties.setProperty(AUTH_PROPERTY, AUTH);
		properties.setProperty(STARTTLS_PROPERTY, STARTTLS);
		properties.setProperty(DEBUG_PROPERTY, DEBUG);
		
		username = usernameParam;
		password = passwordParam;
		
	}
	
	/**
	 * Sends an email message. Must be initialized before being called.
	 * @param mailMessage The message to send.
	 * @throws javax.mail.MessagingException In case it is not possible to send the message
	 */
	public static void send(MailMessage mailMessage) throws MessagingException {
		
		if(mailMessage == null) {
			throw new IllegalArgumentException("Message must not be null");
		}
		
		//Configures a new session object with authentication and connection information
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		Message message = mailMessage.getMessage(session);
		
		Transport.send(message);
	}
	
}
