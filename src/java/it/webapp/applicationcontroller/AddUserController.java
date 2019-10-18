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

import com.google.maps.model.LatLng;
import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcAddressDao;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcUserDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.HashDigest;
import it.webapp.db.entities.SecurityCode;
import it.webapp.db.entities.UserEntity;
import it.webapp.spatial.GeocodingManager;
import it.webapp.logging.Logger;
import it.webapp.mail.ConfirmUserEmail;
import it.webapp.mail.MailManager;
import it.webapp.mail.MailMessage;
import it.webapp.requestprocessor.AddUserPreprocessor;
import it.webapp.requestprocessor.ConfirmUserPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.AddUserSuccessfulView;
import it.webapp.view.ErrorView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to add a new user
 */
public class AddUserController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddUser";
	
	private JdbcAddressDao addressDao;
	private JdbcUserDao userDao;
	
	public AddUserController() throws ApplicationControllerException {
		super(new AddUserPreprocessor());
		
		try {
			addressDao = JdbcDaoFactory.getAddressDao();
			userDao = JdbcDaoFactory.getUserDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
		
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String firstName = (String) request.getAttribute(AddUserPreprocessor.FIRST_NAME_ATTRIBUTE);
		String lastName = (String) request.getAttribute(AddUserPreprocessor.LAST_NAME_ATTRIBUTE);
		String username = (String) request.getAttribute(AddUserPreprocessor.USERNAME_ATTRIBUTE);
		String password = (String) request.getAttribute(AddUserPreprocessor.PASSWORD_ATTRIBUTE);
		String email = (String) request.getAttribute(AddUserPreprocessor.EMAIL_ATTRIBUTE);
		String streetName = (String) request.getAttribute(AddUserPreprocessor.STREET_NAME_ATTRIBUTE);
		String streetNumber = (String) request.getAttribute(AddUserPreprocessor.STREET_NUMBER_ATTRIBUTE);
		String city = (String) request.getAttribute(AddUserPreprocessor.CITY_ATTRIBUTE);
		String district = (String) request.getAttribute(AddUserPreprocessor.DISTRICT_ATTRIBUTE);
		String zip = (String) request.getAttribute(AddUserPreprocessor.ZIP_ATTRIBUTE);
		String country = (String) request.getAttribute(AddUserPreprocessor.COUNTRY_ATTRIBUTE);
		
		//Geocodes the address coordinates
		LatLng addressCoordinates = GeocodingManager.getLatLngFromAddress(streetName, streetNumber, zip, city, district, country);
		
		View view;
		
		if(addressCoordinates != null) {
			try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
				try {

					AddressEntity address = new AddressEntity(null, streetName, streetNumber, city, district, zip, country, new BigDecimal(addressCoordinates.lat), new BigDecimal(addressCoordinates.lng));
					int addressId = addressDao.insert(address, transaction);
					
					//Generates password hash and code for account activation
					HashDigest hashPassword = new HashDigest(password);
					SecurityCode accountActivationCode = new SecurityCode();
					
					boolean accountActivated = false;
					UserEntity user = new UserEntity(null, username, hashPassword, firstName, lastName, addressId, email, new Date(), accountActivationCode, accountActivated, null);

					int userId = userDao.insert(user, transaction);
					
                    // Prepare the base url for confirmation url
                    String requestUrl = request.getRequestURL().toString();
                    String webappBaseURL = requestUrl.substring(0, requestUrl.length() - request.getRequestURI().length()) + request.getContextPath();
                    
                    // Prepare the confirmation url
                    StringBuilder tempRegistrationConfirmationUrl = new StringBuilder();
                    tempRegistrationConfirmationUrl.append(webappBaseURL);
                    tempRegistrationConfirmationUrl.append(ConfirmUserController.URL);
                    tempRegistrationConfirmationUrl.append("?");
                    tempRegistrationConfirmationUrl.append(ConfirmUserPreprocessor.USER_ID_PARAM);
                    tempRegistrationConfirmationUrl.append("=");
                    tempRegistrationConfirmationUrl.append(userId);
                    tempRegistrationConfirmationUrl.append("&");
                    tempRegistrationConfirmationUrl.append(ConfirmUserPreprocessor.REGISTRATION_CODE_PARAM);
                    tempRegistrationConfirmationUrl.append("=");
                    tempRegistrationConfirmationUrl.append(user.getRegistrationCode().getString());
                    String registrationConfirmationUrl = tempRegistrationConfirmationUrl.toString();
                    
					//Sends an account confirmation mail
					MailMessage accountConfirmationMail = new ConfirmUserEmail(user, registrationConfirmationUrl);
					MailManager.send(accountConfirmationMail);

					view = ViewFactory.getView(AddUserSuccessfulView.class);
				} catch(DaoException e) { 
					transaction.rollback();

					Logger.log("Could not add a new issue due to database error", e);

					ErrorView.setRequestParameters("Internal server error", "Could not process request due to a database error", request);
					view = ViewFactory.getView(ErrorView.class);
				}  catch(MessagingException e) { 
					transaction.rollback();

					Logger.log("Could not send account activation mail", e);

					ErrorView.setRequestParameters("Internal server error", "The server was not able to send the account confirmation email", request);
					view = ViewFactory.getView(ErrorView.class);
				}
				
				view.view(request, response);
			} catch(SQLException e) {
				Logger.log("Could not close transaction", e);
			} catch(DaoFactoryException e) {
				Logger.log("Could not begin transaction", e);
			}
			
		} else {
			ErrorView.setRequestParameters("Invalid address", "The received address does not seem to be a valid address", request);
			view = ViewFactory.getView(ErrorView.class);
			view.view(request, response);
		}
	}

}
