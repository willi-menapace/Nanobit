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
 * Preprocesses a request to retrieve item search hints
 */
public class ItemSearchHintPreprocessor implements RequestPreprocessor {

	public static final String QUERY_PARAM = "item_search_hint_preprocessor_query";
	public static final String COUNT_PARAM = "item_search_hint_preprocessor_count";
	
	public static final String QUERY_ATTRIBUTE = "item_search_hint_preprocessor_query";
	public static final String COUNT_ATTRIBUTE = "item_search_hint_preprocessor_count";

	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			
			String query = request.getParameter(QUERY_PARAM);
			int count = Integer.parseInt(request.getParameter(COUNT_PARAM));
		
			if(query == null || query.isEmpty()) {
				return false;
			}

			request.setAttribute(QUERY_ATTRIBUTE, query);
			request.setAttribute(COUNT_ATTRIBUTE, count);

			return true;
		} catch(NumberFormatException e) {
			//Parameter was not valid
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		//No authentication required
		return true;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required
		return true;
	}
	
	
	
}
