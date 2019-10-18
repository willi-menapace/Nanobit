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

import static it.webapp.view.AddShopReviewView.MESSAGE_ATTRIBUTE;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends a response that indicates success or failure
 */
public class AddItemReviewView implements View {
	
	public static final String MESSAGE_ATTRIBUTE = "add_item_review_view_message";
	
	/**
	 * Convenience method for setting View parameters
	 * 
	 * @param errorMessage If not null and not empty indicates that an error happened
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(String errorMessage, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		request.setAttribute(MESSAGE_ATTRIBUTE, errorMessage);
	}
	
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
	
		String message = (String) request.getAttribute(MESSAGE_ATTRIBUTE);
		
		response.setContentType("text/html");
		
		PrintWriter writer;
		try {
			writer = response.getWriter();
		
			//Send success
			if(message == null || message.isEmpty()) {
				writer.write("OK");
			//Send error message
			} else {
				response.sendError(500, message);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Cannot write response to client", e);
		}
	}
}