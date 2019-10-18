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
 * Preprocesses a request for the user profile
 */
public class UserProfilePreprocessor implements RequestPreprocessor {

	public static final String MESSAGE_PARAM = "user_profile_message";
	
	public static final String USER_ID_ATTRIBUTE = "user_profile_user_id";
	public static final String MESSAGE_ATTRIBUTE = "user_profile_message";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		String message = request.getParameter(MESSAGE_PARAM);
		request.setAttribute(MESSAGE_PARAM, message);
		
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("Authentication info must always be present");
		}
		
		if(authenticationInfo.isAuthenticated()) {
			AuthenticatedUserInfo authUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			request.setAttribute(USER_ID_ATTRIBUTE, authUserInfo.getUserId());
			
			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required
		return true;
	}

}
