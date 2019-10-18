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

import it.webapp.requestprocessor.UserHubControllerPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.UserHubView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Processes a request for the user hub
 */
public class UserHubController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UserHub";
	
	public UserHubController() {
		super(new UserHubControllerPreprocessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		View userHubView = ViewFactory.getView(UserHubView.class);
		userHubView.view(request, response);
		
	}

	@Override
	public void processParseOfValidationFailure(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("Parsing must never fail");
	}

	@Override
	public void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authentication failure", "User must be authenticated", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("Authorization must never fail");
	}

}
