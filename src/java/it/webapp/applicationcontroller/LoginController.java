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
import it.webapp.db.dao.jdbc.JdbcAdministratorDao;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcShopDao;
import it.webapp.db.dao.jdbc.JdbcUserDao;
import it.webapp.db.entities.AdministratorEntity;
import it.webapp.db.entities.HashDigest;
import it.webapp.db.entities.ShopEntity;
import it.webapp.db.entities.UserEntity;
import it.webapp.requestprocessor.LoginPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.HomepageView;
import it.webapp.view.LoginView;
import it.webapp.view.RedirectView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Performs user login
 */
public class LoginController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "Login";
	
	private JdbcUserDao userDao;
	private JdbcShopDao shopDao;
	private JdbcAdministratorDao administratorDao;
	
	public LoginController() throws ApplicationControllerException {
		super(new LoginPreprocessor());
		
		try {
			userDao = JdbcDaoFactory.getUserDao();
			shopDao = JdbcDaoFactory.getShopDao();
			administratorDao = JdbcDaoFactory.getAdministratorDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Cannot initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String username = (String) request.getAttribute(LoginPreprocessor.USERNAME_ATTRIBUTE);
		String password = (String) request.getAttribute(LoginPreprocessor.PASSWORD_ATTRIBUTE);
		String redirect = (String) request.getAttribute(LoginPreprocessor.REDIRECT_ATTRIBUTE);
		
		//Calculates the password digest
		HashDigest passwordDigest = new HashDigest(password);
		
		try {
			UserEntity user = userDao.getByUsernameAndPassword(username, passwordDigest);

			//Incorrect credentials
			if(user == null) {
				LoginView loginView = ViewFactory.getView(LoginView.class);

				//Redirect to the login form showing an error message
				LoginView.setRequestParameters(redirect, "Username or password do not match", request);
				loginView.view(request, response);
			} else if(user.isActivated() == false) {
				LoginView loginView = ViewFactory.getView(LoginView.class);

				//Redirect to the login form showing an error message
				LoginView.setRequestParameters(redirect, "User account must be confirmed first", request);
				loginView.view(request, response);
			} else {

				//Checks if the usr is also a shop or ad administrator
				ShopEntity shop = shopDao.getById(user.getUserId());
				AdministratorEntity administrator = administratorDao.getById(user.getUserId());

				boolean isShop = (shop != null);
				boolean isAdministrator = (administrator != null);

				AuthenticationInfo authenticationInfo = new AuthenticatedUserInfo(user.getUserId(), user.getUsername(), isShop, isAdministrator);

				//Updates the user session information
				HttpSession session = request.getSession();
				//Even not authenticated users must have authentication info already present
				if(session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE) == null) {
					throw new IllegalStateException("AuthenticationInfo must always be present in the session");
				}
				session.setAttribute(AuthenticationInfo.SESSION_ATTRIBUTE, authenticationInfo);

				//Redirects to the homepage or to the requested resource
				View view;
				if(redirect.isEmpty()) {
					view = ViewFactory.getView(HomepageView.class);
				} else {
					RedirectView.setRequestParameters(redirect, request);
					view = ViewFactory.getView(RedirectView.class);
				}

				view.view(request, response);
			}
		} catch(DaoException e) {
			ErrorView.setRequestParameters("Error while processing Login request", "The database encountered a problem", request);
			View errorView = ViewFactory.getView(ErrorView.class);
			errorView.view(request, response);
		}
	}
	
	@Override
	public void processParseOfValidationFailure(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Bad Parameters", "Request parameters were not set correctly", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authentication failure", "User must not be authenticated", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("Authorization check must never fail on Login");
	}

}
