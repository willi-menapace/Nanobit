/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author:
 * 
 ******************************************************************************/

package it.webapp.view;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends a login form as a response
 */
public class LoginView implements View {

	public static final String REDIRECT_ATTRIBUTE = "login_view_redirect";
	public static final String ERROR_MESSAGE_ATTRIBUTE = "login_view_error_message";
    private static final String PAGE_URL = "/jsp/Login.jsp";
	
	/**
	 * Convenience method for setting View parameters
	 * @param redirect The page to which redirect the request
	 * @param errorMessage An optional error message to display
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(String redirect, String errorMessage, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(redirect == null) {
			throw new IllegalArgumentException("Redirect must be non null");
		}
        if(errorMessage == null) {
			throw new IllegalArgumentException("ErrorMessage must be non null");
		}
		
		request.setAttribute(LoginView.REDIRECT_ATTRIBUTE, redirect);
		request.setAttribute(LoginView.ERROR_MESSAGE_ATTRIBUTE, errorMessage);
	}
	
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher(PAGE_URL).forward(request, response);
        } catch (IOException | ServletException exception) {
            throw new IllegalArgumentException("Invalid redirect path " +  request.getContextPath() + PAGE_URL, exception);
        }
    }

}
