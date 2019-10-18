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

package it.webapp.applicationcontroller;

import it.webapp.requestprocessor.IssueBasePreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.UserIssueBaseView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for a base issue page
 */
public class UserIssueBaseController extends ApplicationControllerSkeleton {
	
	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UserIssueBase";
	
	public UserIssueBaseController(){
		super(new IssueBasePreprocessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String message = (String) request.getAttribute(IssueBasePreprocessor.MESSAGE_ATTRIBUTE);
		
		UserIssueBaseView.setRequestParameters(message, request);
		UserIssueBaseView userIssueBaseView = ViewFactory.getView(UserIssueBaseView.class);
		userIssueBaseView.view(request, response);
	}
}
