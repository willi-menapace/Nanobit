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
import it.webapp.db.dao.jdbc.JdbcAddressDao;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcUserDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.UserEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.UserProfilePreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.UserProfileView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to view the user profile
 */
public class UserProfileController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UserProfile";
	
	private JdbcUserDao userDao;
	private JdbcAddressDao addressDao;
	
	public UserProfileController() throws ApplicationControllerException {
		super(new UserProfilePreprocessor());
		
		try {
			userDao = JdbcDaoFactory.getUserDao();
			addressDao = JdbcDaoFactory.getAddressDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize Dao", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(UserProfilePreprocessor.USER_ID_ATTRIBUTE);
		String message = (String) request.getAttribute(UserProfilePreprocessor.MESSAGE_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			try {
				UserEntity user = userDao.getById(userId, transaction);
				AddressEntity address = addressDao.getByUserId(userId, transaction);

				View view;
				//User does not exist
				if(user == null || address == null) { 
					ErrorView.setRequestParameters("Cannot retrieve profile info", "User does not exist anymore", request);
					view = ViewFactory.getView(ErrorView.class);

				//Send user profile as response
				} else {
					UserProfileView.setRequestParameters(user, address, message, request);
					view = ViewFactory.getView(UserProfileView.class);
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
