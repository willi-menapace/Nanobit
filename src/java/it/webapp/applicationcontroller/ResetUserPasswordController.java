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

import it.webapp.db.entities.SecurityCode;
import it.webapp.requestprocessor.ResetUserPasswordPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ResetUserPasswordView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to change the user password
 */
public class ResetUserPasswordController extends ApplicationControllerSkeleton {
	
	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ResetUserPassword";
	
	public ResetUserPasswordController() {
		super(new ResetUserPasswordPreprocessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(ResetUserPasswordPreprocessor.USER_ID_ATTRIBUTE);
		SecurityCode passwordResetCode = (SecurityCode) request.getAttribute(ResetUserPasswordPreprocessor.PASSWORD_RESET_CODE_ATTRIBUTE);
		
		//Sends a response that will allow to modify the password
		ResetUserPasswordView.setRequestParameters(userId, passwordResetCode, request);
		ResetUserPasswordView view = ViewFactory.getView(ResetUserPasswordView.class);
		view.view(request, response);
	}
}
