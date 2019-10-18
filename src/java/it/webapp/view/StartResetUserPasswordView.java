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
 * Sends a user password reset form as a response
 */
public class StartResetUserPasswordView implements View{

	public static final String MESSAGE_ATTRIBUTE = "start_reset_user_password_message";
    private static final String PAGE_URL = "/jsp/StartResetUserPassword.jsp";
	
	/**
	 * Convenience method for setting View parameters
	 * @param message The message to display
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(String message, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(message == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
		
		request.setAttribute(MESSAGE_ATTRIBUTE, message);
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
