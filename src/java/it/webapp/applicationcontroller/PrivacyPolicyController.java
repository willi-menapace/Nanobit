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
import it.webapp.view.PrivacyPolicyView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for the privacy policy
 */
public class PrivacyPolicyController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "PrivacyPolicy";
	
	public PrivacyPolicyController() {
		//Preprocessor need to do nothing
		super(new EmptyRequestProcessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		PrivacyPolicyView view = ViewFactory.getView(PrivacyPolicyView.class);
		view.view(request, response);
		
	}

}
