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
 * Preprocesses a request to close an Issue
 */
public class AddIssueEvaluationPreprocessor implements RequestPreprocessor {

	public static final String ISSUE_ID_PARAM = "add_issue_evaluation_preprocessor_issue_id";
	public static final String ISSUE_RESULT_ID_PARAM = "add_issue_evaluation_preprocessor_issue_result_id";
	public static final String MOTIVATION_PARAM = "add_issue_evaluation_preprocessor_motivation";
	
	public static final String ISSUE_ID_ATTRIBUTE = "add_issue_evaluation_preprocessor_issue_id";
	public static final String ISSUE_RESULT_ID_ATTRIBUTE = "add_issue_evaluation_preprocessor_issue_result_id";
	public static final String MOTIVATION_ATTRIBUTE = "add_issue_evaluation_preprocessor_motivation";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {

			int issueId = Integer.parseInt(request.getParameter(ISSUE_ID_PARAM));
			int issueResultId = Integer.parseInt(request.getParameter(ISSUE_RESULT_ID_PARAM));
			String motivation = request.getParameter(MOTIVATION_PARAM);
			
			if(motivation == null || motivation.isEmpty()) {
				return false;
			}
			
			request.setAttribute(ISSUE_ID_ATTRIBUTE, issueId);
			request.setAttribute(ISSUE_RESULT_ID_ATTRIBUTE, issueResultId);
			request.setAttribute(MOTIVATION_ATTRIBUTE, motivation);
			
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

			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}

}
