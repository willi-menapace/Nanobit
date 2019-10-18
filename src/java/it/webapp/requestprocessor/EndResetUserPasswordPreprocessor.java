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
 * Preprocesses a request to reset a user password given an authorization code
 */
public class EndResetUserPasswordPreprocessor implements RequestPreprocessor {

	public static final String USER_ID_PARAM = "end_reset_user_password_preprocessor_user_id";
	public static final String PASSWORD_RESET_CODE_PARAM = "end_reset_user_password_preprocessor_password_reset_code";
	public static final String NEW_PASSWORD_PARAM = "end_reset_user_password_preprocessor_new_password";
	
	public static final String USER_ID_ATTRIBUTE = "end_reset_user_password_preprocessor_user_id";
	public static final String PASSWORD_RESET_CODE_ATTRIBUTE = "end_reset_user_password_preprocessor_password_reset_code";
	public static final String NEW_PASSWORD_ATTRIBUTE = "end_reset_user_password_preprocessor_new_password";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int userId = Integer.parseInt(request.getParameter(USER_ID_PARAM));
			String passwordResetCodeString = (String) request.getParameter(PASSWORD_RESET_CODE_PARAM);
			String newPassword = (String) request.getParameter(NEW_PASSWORD_PARAM);
			
			if(passwordResetCodeString == null || newPassword == null) {
				return false;
			}
			if(passwordResetCodeString.isEmpty() || newPassword.isEmpty()) {
				return false;
			}
			
			SecurityCode passwordResetCode = new SecurityCode(passwordResetCodeString);
			
			request.setAttribute(USER_ID_ATTRIBUTE, userId);
			request.setAttribute(PASSWORD_RESET_CODE_ATTRIBUTE, passwordResetCode);
			request.setAttribute(NEW_PASSWORD_ATTRIBUTE, newPassword);
			
			return true;
			
		} catch(NumberFormatException e) {
			//User id was not formatted correctly
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		//Anyone with a valid authorization code can perform the password reset
		return true;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//Anyone with a valid authorization code can perform the password reset
		return true;
	}

}
