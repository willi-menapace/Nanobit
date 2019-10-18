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
import it.webapp.requestprocessor.ConfirmUserPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ConfirmUserErrorView;
import it.webapp.view.ConfirmUserSuccessfulView;
import it.webapp.view.ErrorView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to confirm an user account
 */
public class ConfirmUserController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ConfirmUser";
	
	private JdbcUserDao userDao;
	
	public ConfirmUserController() throws ApplicationControllerException {
		super(new ConfirmUserPreprocessor());
		
		try {
			userDao = JdbcDaoFactory.getUserDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
		
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(ConfirmUserPreprocessor.USER_ID_ATTRIBUTE);
		SecurityCode registrationCode = (SecurityCode) request.getAttribute(ConfirmUserPreprocessor.REGISTRATION_CODE_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			try {
				UserEntity user = userDao.getById(userId, transaction);
				
				View view;
				
				if(user == null) {
					ConfirmUserErrorView.setRequestParameters("User does not exist", request);
					view = ViewFactory.getView(ConfirmUserErrorView.class);
				} else {
					//Check if the code is correct or not
					SecurityCode referenceCode = user.getRegistrationCode();
					//Sets the user as activated
					if(referenceCode.match(registrationCode)) {
						user.setActivated(true);
						userDao.update(user, transaction);
						view = ViewFactory.getView(ConfirmUserSuccessfulView.class);
					} else {
						ConfirmUserErrorView.setRequestParameters("Corrupted link", request);
						view = ViewFactory.getView(ConfirmUserErrorView.class);
					}
				}
				
				view.view(request, response);
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not confirm user account due to database error", e);
				
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
