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
import it.webapp.db.entities.UserEntity;
import it.webapp.spatial.GeocodingManager;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.UpdateUserPreprocessor;
import it.webapp.requestprocessor.UserProfilePreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.RedirectView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to update an user
 */
public class UpdateUserController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" +  "UpdateUser";
	
	private JdbcUserDao userDao;
	private JdbcAddressDao addressDao;
	
	public UpdateUserController() throws ApplicationControllerException {
		super(new UpdateUserPreprocessor());
		
		try {
			userDao = JdbcDaoFactory.getUserDao();
			addressDao = JdbcDaoFactory.getAddressDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(UpdateUserPreprocessor.USER_ID_ATTRIBUTE);
		String password = (String) request.getAttribute(UpdateUserPreprocessor.PASSWORD_ATTRIBUTE);
		String email = (String) request.getAttribute(UpdateUserPreprocessor.EMAIL_ATTRIBUTE);
		String streetName = (String) request.getAttribute(UpdateUserPreprocessor.STREET_NAME_ATTRIBUTE);
		String streetNumber = (String) request.getAttribute(UpdateUserPreprocessor.STREET_NUMBER_ATTRIBUTE);
		String city = (String) request.getAttribute(UpdateUserPreprocessor.CITY_ATTRIBUTE);
		String district = (String) request.getAttribute(UpdateUserPreprocessor.DISTRICT_ATTRIBUTE);
		String zip = (String) request.getAttribute(UpdateUserPreprocessor.ZIP_ATTRIBUTE);
		String country = (String) request.getAttribute(UpdateUserPreprocessor.COUNTRY_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			View view;
			try {
				UserEntity user = userDao.getById(userId, transaction);
				AddressEntity address = addressDao.getByUserId(userId, transaction);
				
				//Updates non null values
				if(password != null) {
					user.setPasswordHash(new HashDigest(password));
				}
				if(email != null) {
					user.setEmail(email);
				}
				if(streetName != null) {
					address.setStreet(streetName);
				}
				if(streetNumber != null) {
					address.setStreetNumber(streetNumber);
				}
				if(city != null) {
					address.setCity(city);
				}
				if(district != null) {
					address.setDistrict(district);
				}
				if(zip != null) {
					address.setZip(zip);
				}
				if(country != null) {
					address.setCountry(country);
				}
				
				//Geocodes the address coordinates and sets them
				LatLng addressCoordinates = GeocodingManager.getLatLngFromAddress(address.getStreet(), address.getStreetNumber(), address.getZip(), address.getCity(), address.getDistrict(), address.getCountry());
				if(addressCoordinates != null) {
					address.setLatitude(new BigDecimal(addressCoordinates.lat));
					address.setLongitude(new BigDecimal(addressCoordinates.lng));

					userDao.update(user, transaction);
					addressDao.update(address, transaction);
					
					RedirectView.setRequestParameters(request.getContextPath() + UserProfileController.URL, UserProfilePreprocessor.MESSAGE_PARAM, "User profile successfully updated", request);
					view = ViewFactory.getView(RedirectView.class);
				} else {
					transaction.rollback();
					Logger.log("Could not update user account due to address geocoding error");
					
					RedirectView.setRequestParameters(request.getContextPath() + UserProfileController.URL, UserProfilePreprocessor.MESSAGE_PARAM, "User profile could not be updated, address is not valid", request);
					view = ViewFactory.getView(RedirectView.class);
				}
				
			} catch(DaoException e) { 
				transaction.rollback();

				Logger.log("Could not add a new issue due to database error", e);

				ErrorView.setRequestParameters("Internal server error", "Could not process request due to a database error", request);
				view = ViewFactory.getView(ErrorView.class);
			}

			view.view(request, response);
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}

}
