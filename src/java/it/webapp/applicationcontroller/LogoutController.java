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

import it.webapp.requestprocessor.LogoutPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.HomepageView;
import it.webapp.view.RedirectView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "Logout";
	
	public LogoutController() {
		super(new LogoutPreprocessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
	
		String redirect = (String) request.getAttribute(LogoutPreprocessor.REDIRECT_ATTRIBUTE);
		
		//Destroy the session, will be recreated upon next request
		HttpSession session = request.getSession();
		session.invalidate();
		
		//Performs redirect if the user requested it, otherwise show default page
		View view;
		if(redirect.isEmpty()) {
			view = ViewFactory.getView(HomepageView.class);
		} else {
			RedirectView.setRequestParameters(redirect, request);
			view = ViewFactory.getView(RedirectView.class);
		}
		
		view.view(request, response);
	}

	@Override
	public void processParseOfValidationFailure(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Bad Parameters", "Request parameters were not set correctly", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Bad Parameters", "Request parameters were not set correctly", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("Authorization check must never fail on Logout");
	}

}
