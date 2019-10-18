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

import javax.servlet.http.HttpServletRequest;

public class StartResetUserPasswordPreprocessor implements RequestPreprocessor {

	public static final String USERNAME_PARAM = "start_reset_user_password_preprocessor_username";
	
	public static final String USERNAME_ATTRIBUTE = "start_reset_user_password_preprocessor_username";

	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		String username = request.getParameter(USERNAME_PARAM);
		
		if(username == null || username.isEmpty()) {
			return false;
		}
		
		request.setAttribute(USERNAME_ATTRIBUTE, username);
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		//Both authenticated and not authenticated users can start a password reset for some user
		return true;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authentication required
		return true;
	}
	
	
	
}
