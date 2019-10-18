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
import it.webapp.db.entities.HashDigest;
import it.webapp.db.entities.SecurityCode;
import it.webapp.db.entities.UserEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.EndResetUserPasswordPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.EndResetUserPasswordSuccessfulView;
import it.webapp.view.ErrorView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to reset a user password given an authorization code
 */
public class EndResetUserPasswordController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "EndResetUserPassword";
	
	private JdbcUserDao userDao;
	
	public EndResetUserPasswordController() throws ApplicationControllerException {
		super(new EndResetUserPasswordPreprocessor());
		
		try {
			userDao = JdbcDaoFactory.getUserDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize Dao", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(EndResetUserPasswordPreprocessor.USER_ID_ATTRIBUTE);
		SecurityCode passwordResetCode = (SecurityCode) request.getAttribute(EndResetUserPasswordPreprocessor.PASSWORD_RESET_CODE_ATTRIBUTE);
		String newPassword = (String) request.getAttribute(EndResetUserPasswordPreprocessor.NEW_PASSWORD_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			try {
				UserEntity user = userDao.getById(userId, transaction);

				View view;

				if(user == null) {
					ErrorView.setRequestParameters("Cannot reset password", "User no longer exists", request);
					view = ViewFactory.getView(ErrorView.class);
				} else {
					SecurityCode originalPasswordResetCode = user.getPasswordResetCode();

					if(originalPasswordResetCode.match(passwordResetCode)) {

						//Updates the user record with the new password and erases the current code
						user.setPasswordResetCode(null);
						user.setPasswordHash(new HashDigest(newPassword));
						userDao.update(user, transaction);
						view = ViewFactory.getView(EndResetUserPasswordSuccessfulView.class);
					} else {
						ErrorView.setRequestParameters("Cannot reset password", "Corrupted request", request);
						view = ViewFactory.getView(ErrorView.class);
					}
				}

				view.view(request, response);
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not reset user password due to database error", e);
				
				ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
				//Sets the parameters and send the error
				ErrorView.setRequestParameters("Internal server error", "Database could not process request", request);
				errorView.view(request, response);
			}
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}

}
