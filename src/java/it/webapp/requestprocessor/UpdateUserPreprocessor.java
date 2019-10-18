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

package it.webapp.requestprocessor;

import it.webapp.applicationcontroller.AuthenticatedUserInfo;
import it.webapp.applicationcontroller.AuthenticationInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Preprocesses a request to update an user
 */
public class UpdateUserPreprocessor implements RequestPreprocessor {
	
	public static final String PASSWORD_PARAM = "update_user_preprocessor_password";
	public static final String PASSWORD_CONFIRM_PARAM = "update_user_preprocessor_password_confirm";
	public static final String EMAIL_PARAM = "update_user_preprocessor_email";
	public static final String EMAIL_CONFIRM_PARAM = "update_user_preprocessor_email_confirm";
	public static final String STREET_NAME_PARAM = "update_user_preprocessor_street";
	public static final String STREET_NUMBER_PARAM = "update_user_preprocessor_street_number";
	public static final String CITY_PARAM = "update_user_preprocessor_city";
	public static final String DISTRICT_PARAM = "update_user_preprocessor_district";
	public static final String ZIP_PARAM = "update_user_preprocessor_zip";
	public static final String COUNTRY_PARAM = "update_user_preprocessor_country";
		
	public static final String USER_ID_ATTRIBUTE = "update_user_preprocessor_user_id";
	public static final String FIRST_NAME_ATTRIBUTE = "update_user_preprocessor_first_name";
	public static final String LAST_NAME_ATTRIBUTE = "update_user_preprocessor_last_name";
	public static final String USERNAME_ATTRIBUTE = "update_user_preprocessor_username";
	public static final String PASSWORD_ATTRIBUTE = "update_user_preprocessor_password";
	public static final String EMAIL_ATTRIBUTE = "update_user_preprocessor_email";
	public static final String STREET_NAME_ATTRIBUTE = "update_user_preprocessor_street";
	public static final String STREET_NUMBER_ATTRIBUTE = "update_user_preprocessor_street_number";
	public static final String CITY_ATTRIBUTE = "update_user_preprocessor_city";
	public static final String DISTRICT_ATTRIBUTE = "update_user_preprocessor_district";
	public static final String ZIP_ATTRIBUTE = "update_user_preprocessor_zip";
	public static final String COUNTRY_ATTRIBUTE = "update_user_preprocessor_country";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {

		String password = request.getParameter(PASSWORD_PARAM);
		String passwordConfirm = request.getParameter(PASSWORD_CONFIRM_PARAM);
		String email = request.getParameter(EMAIL_PARAM);
		String emailConfirm = request.getParameter(EMAIL_CONFIRM_PARAM);
		String streetName = request.getParameter(STREET_NAME_PARAM);
		String streetNumber = request.getParameter(STREET_NUMBER_PARAM);
		String city = request.getParameter(CITY_PARAM);
		String district = request.getParameter(DISTRICT_PARAM);
		String zip = request.getParameter(ZIP_PARAM);
		String country = request.getParameter(COUNTRY_PARAM);
		
		//If there is a password there must be a confirm equal to the password
		if(password != null && (passwordConfirm == null || !password.equals(passwordConfirm))) {
			return false;
		}
		
		//In case password are empty treat them as not set
		if(password != null && password.isEmpty()) {
			password = null;
			passwordConfirm = null;
		}
		
		//If there is an email there must be a confirm equal to the email
		if(email != null && (emailConfirm == null || !email.equals(emailConfirm))) {
			return false;
		}
		
		if(streetName != null && streetName.isEmpty()) {
			return false;
		}
		if(streetNumber != null && streetNumber.isEmpty()) {
			return false;
		}
		if(city != null && city.isEmpty()) {
			return false;
		}
		if(district != null && district.isEmpty()) {
			return false;
		}
		if(zip != null && zip.isEmpty()) {
			return false;
		}
		if(country != null && country.isEmpty()) {
			return false;
		}

		request.setAttribute(PASSWORD_ATTRIBUTE, password);
		request.setAttribute(EMAIL_ATTRIBUTE, email);
		request.setAttribute(STREET_NAME_ATTRIBUTE, streetName);
		request.setAttribute(STREET_NUMBER_ATTRIBUTE, streetNumber);
		request.setAttribute(CITY_ATTRIBUTE, city);
		request.setAttribute(DISTRICT_ATTRIBUTE, district);
		request.setAttribute(ZIP_ATTRIBUTE, zip);
		request.setAttribute(COUNTRY_ATTRIBUTE, country);
		
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("Authentication info must always be present");
		}
		
		//Only authenticated users can edit their information
		if(authenticationInfo.isAuthenticated()) {
			//Gets and sets the user id
			AuthenticatedUserInfo authUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			int userId = authUserInfo.getUserId();
			request.setAttribute(USER_ID_ATTRIBUTE, userId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required
		return true;
	}

}
