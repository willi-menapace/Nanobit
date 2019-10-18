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

/**
 * Preprocessor for a login request
 */
public class LoginPreprocessor implements RequestPreprocessor {

	public static final String USERNAME_PARAMETER = "login_preprocessor_username";
	public static final String PASSWORD_PARAMETER = "login_preprocessor_password";
	public static final String REDIRECT_PARAMETER = "login_preprocessor_redirect";
	
	public static final String USERNAME_ATTRIBUTE = "login_preprocessor_username";
	public static final String PASSWORD_ATTRIBUTE = "login_preprocessor_password";
	public static final String REDIRECT_ATTRIBUTE = "login_preprocessor_redirect";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		String username = request.getParameter(USERNAME_PARAMETER);
		String password = request.getParameter(PASSWORD_PARAMETER);
		String redirectUrl = request.getParameter(REDIRECT_PARAMETER);
		
		//Validates input
		if(username == null || password == null || redirectUrl == null) {
			return false;
		}
		
		if(username.isEmpty() || password.isEmpty()) {
			return false;
		}
		
		//Sets request attributes
		request.setAttribute(USERNAME_ATTRIBUTE, username);
		request.setAttribute(PASSWORD_ATTRIBUTE, password);
		request.setAttribute(REDIRECT_ATTRIBUTE, redirectUrl);
		
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) request.getSession().getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		
		if(authenticationInfo == null) {
			throw new IllegalStateException("AuthenticationInfo must be always set");
		}
		
		//Only not authenticated users can log in
		return authenticationInfo.isAuthenticated() == false;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required
		return true;
	}

}
