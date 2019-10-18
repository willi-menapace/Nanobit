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
 * Preprocesses a reuqest for issues opened by every user
 */
public class AdminIssuePreprocessor implements RequestPreprocessor {
	
	public static final String OFFSET_PARAM = "admin_issue_offset";
	public static final String COUNT_PARAM = "admin_issue_count";
	
	public static final String ADMIN_ID_ATTRIBUTE = "admin_issue_admin_id";
	public static final String OFFSET_ATTRIBUTE = "admin_issue_offset";
	public static final String COUNT_ATTRIBUTE = "admin_issue_count";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int offset = Integer.parseInt(request.getParameter(OFFSET_PARAM));
			int count = Integer.parseInt(request.getParameter(COUNT_PARAM));
		
			//Parameters must be positive numbers
			if(offset < 0 || count <= 0) {
				return false;
			}
			
			request.setAttribute(OFFSET_ATTRIBUTE, offset);
			request.setAttribute(COUNT_ATTRIBUTE, count);
			
			return true;
			
		} catch(NumberFormatException e) {
			//Parameters were not valid
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("AuthenticationInfo must be always set");
		}
				
		if(authenticationInfo.isAuthenticated()) {
			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("AuthenticationInfo must be always set");
		}
		
		//The user must be an authenticated administrator
		if(authenticationInfo.isAuthenticated() && authenticationInfo.isAdmin()) {
			//Obtains and sets the id of the user performing the request
			AuthenticationInfo authUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			int adminId = authUserInfo.getUserId();
			
			request.setAttribute(ADMIN_ID_ATTRIBUTE, adminId);
			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}
}
