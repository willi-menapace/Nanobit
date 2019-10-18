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
 * Preprocesses a request for a password reset form
 */
public class ResetUserPasswordPreprocessor implements RequestPreprocessor {

	public static final String USER_ID_PARAM = "reset_user_password_preprocessor_user_id";
	public static final String PASSWORD_RESET_CODE_PARAM = "reset_user_password_preprocessor_password_reset_code";
	
	public static final String USER_ID_ATTRIBUTE = "reset_user_password_preprocessor_user_id";
	public static final String PASSWORD_RESET_CODE_ATTRIBUTE = "reset_user_password_preprocessor_password_reset_code";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
	
		try {
			int userId = (int) Integer.parseInt(request.getParameter(USER_ID_PARAM));
			
			String resetCodeString = request.getParameter(PASSWORD_RESET_CODE_PARAM);
			
			if(resetCodeString == null || resetCodeString.isEmpty()) {
				return false;
			}
			
			SecurityCode passwordResetCode = new SecurityCode(resetCodeString);
			
			request.setAttribute(USER_ID_ATTRIBUTE, userId);
			request.setAttribute(PASSWORD_RESET_CODE_ATTRIBUTE, passwordResetCode);
			
			return true;
			
		} catch(NumberFormatException e) {
			//Parameter was not valid
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		//Both logged and not logged users can reset a password for someone
		return true;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required
		return true;
	}

}
