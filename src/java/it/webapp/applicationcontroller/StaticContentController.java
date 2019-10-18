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

import it.webapp.requestprocessor.StaticContentPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.StaticContentView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticContentController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "StaticContent";
	
	public StaticContentController(){
		super(new StaticContentPreprocessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String resource = (String) request.getAttribute(StaticContentPreprocessor.RESOURCE_ATTRIBUTE);
		
		StaticContentView.setRequestParameters(resource, request);
		StaticContentView staticContentView = ViewFactory.getView(StaticContentView.class);
		staticContentView.view(request, response);
	}
	
}
