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

import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.UserEntity;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends the user profile as a response
 */
public class UserProfileView implements View {

	private static final String PAGE_URL = "/jsp/UserProfile.jsp";
	
	public static final String USER_ATTRIBUTE = "user_profile_view_user";
	public static final String ADDRESS_ATTRIBUTE = "user_profile_view_address";
	public static final String MESSAGE_ATTRIBUTE = "user_profile_view_message";
	
	/**
	 * Convenience method for setting View parameters
	 * @param user The user entity for the current user
	 * @param address The address entity for the current user
	 * @param message Optional message to view
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(UserEntity user, AddressEntity address, String message, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(user == null || address == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
		
		request.setAttribute(MESSAGE_ATTRIBUTE, message);
		request.setAttribute(USER_ATTRIBUTE, user);
		request.setAttribute(ADDRESS_ATTRIBUTE, address);
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
