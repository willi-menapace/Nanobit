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

import it.webapp.db.entities.SecurityCode;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResetUserPasswordView implements View {

	public static final String USER_ID_ATTRIBUTE = "reset_user_password_view_user_id";
	public static final String PASSWORD_RESET_CODE_ATTRIBUTE = "reset_user_password_view_password_reset_code";
    private static final String PAGE_URL = "/jsp/ResetUserPassword.jsp";
	
	/**
	 * Convenience method for setting View parameters
	 * @param userId id of the user for which to reset the password
	 * @param passwordResetCode code to use to authorize password reset
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(int userId, SecurityCode passwordResetCode, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(passwordResetCode == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
		
		request.setAttribute(USER_ID_ATTRIBUTE, userId);
		request.setAttribute(PASSWORD_RESET_CODE_ATTRIBUTE, passwordResetCode);
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
