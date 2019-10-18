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

import it.webapp.applicationcontroller.AuthenticationInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Preprocesses a request to add a new user
 */
public class AddUserPreprocessor implements RequestPreprocessor {

	public static final String FIRST_NAME_PARAM = "add_user_preprocessor_first_name";
	public static final String LAST_NAME_PARAM = "add_user_preprocessor_last_name";
	public static final String USERNAME_PARAM = "add_user_preprocessor_username";
	public static final String PASSWORD_PARAM = "add_user_preprocessor_password";
	public static final String PASSWORD_CONFIRM_PARAM = "add_user_preprocessor_password_confirm";
	public static final String EMAIL_PARAM = "add_user_preprocessor_email";
	public static final String EMAIL_CONFIRM_PARAM = "add_user_preprocessor_email_confirm";
	public static final String STREET_NAME_PARAM = "add_user_preprocessor_street";
	public static final String STREET_NUMBER_PARAM = "add_user_preprocessor_street_number";
	public static final String CITY_PARAM = "add_user_preprocessor_city";
	public static final String DISTRICT_PARAM = "add_user_preprocessor_district";
	public static final String ZIP_PARAM = "add_user_preprocessor_zip";
	public static final String COUNTRY_PARAM = "add_user_preprocessor_country";
	
	public static final String FIRST_NAME_ATTRIBUTE = "add_user_preprocessor_first_name";
	public static final String LAST_NAME_ATTRIBUTE = "add_user_preprocessor_last_name";
	public static final String USERNAME_ATTRIBUTE = "add_user_preprocessor_username";
	public static final String PASSWORD_ATTRIBUTE = "add_user_preprocessor_password";
	public static final String EMAIL_ATTRIBUTE = "add_user_preprocessor_email";
	public static final String STREET_NAME_ATTRIBUTE = "add_user_preprocessor_street";
	public static final String STREET_NUMBER_ATTRIBUTE = "add_user_preprocessor_street_number";
	public static final String CITY_ATTRIBUTE = "add_user_preprocessor_city";
	public static final String DISTRICT_ATTRIBUTE = "add_user_preprocessor_district";
	public static final String ZIP_ATTRIBUTE = "add_user_preprocessor_zip";
	public static final String COUNTRY_ATTRIBUTE = "add_user_preprocessor_country";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		String firstName = request.getParameter(FIRST_NAME_PARAM);
		String lastName = request.getParameter(LAST_NAME_PARAM);
		String username = request.getParameter(USERNAME_PARAM);
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
		
		//Parameters must be not null
		if(firstName == null || lastName == null || username == null ||  password == null || 
		   passwordConfirm == null || email == null || emailConfirm == null || streetName == null || 
		   streetNumber == null || city == null || district == null || zip == null || 
		   country == null) {
			return false;
		}
		
		//Parameters must be not empty
		if(firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||  password.isEmpty() || 
		   passwordConfirm.isEmpty() || email.isEmpty() || emailConfirm.isEmpty() || streetName.isEmpty() || 
		   streetNumber.isEmpty() || city.isEmpty() || district.isEmpty() || zip.isEmpty() || 
		   country.isEmpty()) {
			return false;
		}
		
		//Passwords and emails must match
		if(!password.equals(passwordConfirm)) {
			return false;
		}
		if(!email.equals(email)) {
			return false;
		}
		
		request.setAttribute(FIRST_NAME_ATTRIBUTE, firstName);
		request.setAttribute(LAST_NAME_ATTRIBUTE, lastName);
		request.setAttribute(USERNAME_ATTRIBUTE, username);
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
		
		//Only non authenticated users can register
		if(authenticationInfo.isAuthenticated()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required
		return true;
	}

}
