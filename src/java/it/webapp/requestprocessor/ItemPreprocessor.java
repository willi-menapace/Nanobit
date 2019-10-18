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

/**
 * Preprocesses a request for information about an item
 */
public class ItemPreprocessor implements RequestPreprocessor {

	public static final String ITEM_ID_PARAM = "item_preprocessor_item_id";
	
	public static final String ITEM_ID_ATTRIBUTE = "item_preprocessor_item_id";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int itemId = Integer.parseInt(request.getParameter(ITEM_ID_PARAM));
			
			request.setAttribute(ITEM_ID_ATTRIBUTE, itemId);
			return true;
			
		} catch(NumberFormatException e) {
			//Parameter was not valid
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		//No authentication is needed
		return true;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization is needed
		return true;
	}

}
