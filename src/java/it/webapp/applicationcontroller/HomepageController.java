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

import it.webapp.requestprocessor.EmptyRequestProcessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.HomepageView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Elaborates a request for the homepage
 */
public class HomepageController extends ApplicationControllerSkeleton {
	
	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "Homepage";
	
	public HomepageController() {
		//No pre processing required on the request
		super(new EmptyRequestProcessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		//Directly dispatches the request to the view
		HomepageView homepageView = ViewFactory.getView(HomepageView.class);
		homepageView.view(request, response);
	}

	@Override
	public void processParseOfValidationFailure(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("No validation can fail for this request");
	}

	@Override
	public void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("No authentication can fail for this request");
	}

	@Override
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("No authorization can fail for this request");
	}

}
