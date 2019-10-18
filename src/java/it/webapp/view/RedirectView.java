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

import it.webapp.logging.Logger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectView implements View {

	public static final String REDIRECT_ATTRIBUTE = "redirect_view_redirect";
	
	/**
	 * Convenience method for setting View parameters
	 * @param redirect The url to which redirect
	 * @param key Optional key parameter to pass as argument in the encoded request url
	 * @param value Value to associate with the key parameter
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(String redirect, String key, String value, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(redirect == null) {
			throw new IllegalArgumentException("Redirect must be non null");
		}
		if(redirect.isEmpty()) {
			throw new IllegalArgumentException("Redirect must not be empty");
		}
		
		//We must pass an additional parameter
		if(key != null) {
			//Check if the url already had parameters
			if(!redirect.contains("?")) {
				redirect += "?";
			} else {
				redirect += "&";
			}
			redirect += key + "=";
			try {
				redirect += URLEncoder.encode(value, "UTF-8");
			} catch(UnsupportedEncodingException e) {
				throw new IllegalArgumentException("Code tried to use a not supported character set", e);
			}
		}
		
		request.setAttribute(RedirectView.REDIRECT_ATTRIBUTE, redirect);
	}
	
	/**
	 * Convenience method for setting View parameters
	 * @param redirect The url to which redirect
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(String redirect, HttpServletRequest request) {
		setRequestParameters(redirect, null, null, request);
	}
	
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect((String) request.getAttribute(REDIRECT_ATTRIBUTE));
		} catch(IOException e) {
			Logger.log("Could not send redirect", e);
			throw new IllegalStateException("Could not send redirect", e);
		}
	}

}
