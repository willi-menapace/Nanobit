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
 * Preprocessor for a logout request
 */
public class LogoutPreprocessor implements RequestPreprocessor {
	
	public static final String REDIRECT_PARAMETER = "logout_preprocessor_redirect";
	
	public static final String REDIRECT_ATTRIBUTE = "logout_preprocessor_redirect";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		String redirectUrl = request.getParameter(REDIRECT_PARAMETER);
		
		if(redirectUrl == null) {
			return false;
		}
		
		request.setAttribute(REDIRECT_ATTRIBUTE, redirectUrl);
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		//Only an authenticated user can log out
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("Authentication info must always be present");
		}
		if(authenticationInfo.isAuthenticated() == false) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization check required, every logged user can log out
		return true;
	}

}
