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
 * Sends an error message as response
 */
public class ErrorView implements View {

	public static final String TITLE_ATTRIBUTE = "error_view_title";
	public static final String DESCRIPTION_ATTRIBUTE = "error_view_description";
    private static final String PAGE_URL = "/jsp/Error.jsp";
	
	/**
	 * Convenience method for setting View parameters
	 * @param title The title of the page
	 * @param description The description of the page
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(String title, String description, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(title == null || description == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
		if(title.isEmpty()) {
			throw new IllegalArgumentException("title must not be empty");
		}
		
		request.setAttribute(TITLE_ATTRIBUTE, title);
		request.setAttribute(DESCRIPTION_ATTRIBUTE, description);
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
