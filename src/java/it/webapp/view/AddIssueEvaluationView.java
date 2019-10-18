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
 * Sends a response that indicates success or failure
 */
public class AddIssueEvaluationView implements View {
	
	private static final String PAGE_URL = "/jsp/AddIssueEvaluationForm.jsp";
	
	public static final String ISSUE_ID_ATTRIBUTE = "add_issue_evaluation_view_issue_id";
	public static final String MESSAGE_ATTRIBUTE = "add_issue_evaluation_view_message";
	
	/**
	 * Convenience method for setting View parameters
	 * 
	 * @param issueId Id for the issue to close
	 * @param errorMessage If not null and not empty indicates that an error happened
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(int issueId, String errorMessage, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		request.setAttribute(ISSUE_ID_ATTRIBUTE, issueId);
		request.setAttribute(MESSAGE_ATTRIBUTE, errorMessage);
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