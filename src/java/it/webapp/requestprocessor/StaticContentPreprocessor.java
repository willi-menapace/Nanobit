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

package it.webapp.requestprocessor;

import javax.servlet.http.HttpServletRequest;

public class StaticContentPreprocessor implements RequestPreprocessor {

	public static final String RESOURCE_PARAM = "static_content_preprocessor_resource";
	
	public static final String RESOURCE_ATTRIBUTE = "static_content_preprocessor_resource";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		String resource = request.getParameter(RESOURCE_PARAM);
		
		if(resource == null || resource.isEmpty()) {
			return false;
		}
		
		request.setAttribute(RESOURCE_ATTRIBUTE, resource);
		
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		//No authentication required
		return true;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authentication required
		return true;
	}

}
