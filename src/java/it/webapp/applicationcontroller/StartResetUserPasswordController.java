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

package it.webapp.applicationcontroller;

import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcUserDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.SecurityCode;
import it.webapp.db.entities.UserEntity;
import it.webapp.logging.Logger;
import it.webapp.mail.MailManager;
import it.webapp.mail.MailMessage;
import it.webapp.mail.PasswordResetEmail;
import it.webapp.requestprocessor.ResetUserPasswordPreprocessor;
import it.webapp.requestprocessor.StartResetUserPasswordPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.StartResetUserPasswordSuccessfulView;
import it.webapp.view.StartResetUserPasswordView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages the request for a password reset on a user account
 */
public class StartResetUserPasswordController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "StartResetUserPassword";
	
	private JdbcUserDao userDao;
	
	public StartResetUserPasswordController() throws ApplicationControllerException {
		super(new StartResetUserPasswordPreprocessor());
		
		try {
			userDao = JdbcDaoFactory.getUserDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize Dao", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String username = (String) request.getAttribute(StartResetUserPasswordPreprocessor.USERNAME_ATTRIBUTE);
		
		//We will take multiple actions so a transaction is better suited
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			try {
				UserEntity user = userDao.getByUsername(username, transaction);

				View view;
				if(user == null) {
					StartResetUserPasswordView.setRequestParameters("Username does not exist", request);
					view = ViewFactory.getView(StartResetUserPasswordView.class);
				} else {
					//Generates a new random password reset code
					SecurityCode passwordResetCode = new SecurityCode();
					user.setPasswordResetCode(passwordResetCode);

					//Updates the user entry
					userDao.update(user, transaction);
				
                    // Prepare the base url for confirmation url
                    String requestUrl = request.getRequestURL().toString();
                    String webappBaseURL = requestUrl.substring(0, requestUrl.length() - request.getRequestURI().length()) + request.getContextPath();
                    
                    // Prepare the confirmation url
                    StringBuilder tempResetPasswordUrl = new StringBuilder();
                    tempResetPasswordUrl.append(webappBaseURL);
                    tempResetPasswordUrl.append(ResetUserPasswordController.URL);
                    tempResetPasswordUrl.append("?");
                    tempResetPasswordUrl.append(ResetUserPasswordPreprocessor.USER_ID_PARAM);
                    tempResetPasswordUrl.append("=");
                    tempResetPasswordUrl.append(user.getUserId());
                    tempResetPasswordUrl.append("&");
                    tempResetPasswordUrl.append(ResetUserPasswordPreprocessor.PASSWORD_RESET_CODE_PARAM);
                    tempResetPasswordUrl.append("=");
                    tempResetPasswordUrl.append(passwordResetCode.getString());
                    String resetPasswordURL = tempResetPasswordUrl.toString();
                    
					//sends a mail for continuing the password reset procedure
					MailMessage mail = new PasswordResetEmail(user, resetPasswordURL);
					MailManager.send(mail);
					
					view = ViewFactory.getView(StartResetUserPasswordSuccessfulView.class);
				}
				
				view.view(request, response);
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not complete start password reset request due to database error", e);
			
				ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
				//Sets the parameters and send the error
				ErrorView.setRequestParameters("Internal server error", "Database could not process request", request);
				errorView.view(request, response);
			}
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		} catch(MessagingException e) {
			Logger.log("Could not send password reset email", e);
		}
				
	}
}
