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

import it.webapp.db.entities.SecurityCode;
import javax.servlet.http.HttpServletRequest;

/**
 * Preprocessor for a user account confirmation request
 */
public class ConfirmUserPreprocessor implements RequestPreprocessor {

	public static final String USER_ID_PARAM = "confirm_user_preprocessor_user_id";
	public static final String REGISTRATION_CODE_PARAM = "confirm_user_preprocessor_registration_code";
	
	public static final String USER_ID_ATTRIBUTE = "confirm_user_preprocessor_user_id";
	public static final String REGISTRATION_CODE_ATTRIBUTE = "confirm_user_preprocessor_registration_code";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int userId = Integer.parseInt(request.getParameter(USER_ID_PARAM));
			SecurityCode registrationCode = new SecurityCode(request.getParameter(REGISTRATION_CODE_PARAM));
			
			request.setAttribute(USER_ID_ATTRIBUTE, userId);
			request.setAttribute(REGISTRATION_CODE_ATTRIBUTE, registrationCode);
			
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		//Both authenticated and not authenticated users can confirm arbitrary accounts
		return true;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required
		return true;
	}

}
