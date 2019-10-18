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
 * Preprocessor for a request for item reviews
 */
public class ShopReviewPreprocessor implements RequestPreprocessor {

	public static final String SHOP_ID_PARAM = "shop_review_preprocessor_shop_id";
	public static final String OFFSET_PARAM = "shop_review_preprocessor_offset";
	public static final String COUNT_PARAM = "shop_review_preprocessor_count";
	
	public static final String SHOP_ID_ATTRIBUTE = "shop_review_preprocessor_shop_id";
	public static final String OFFSET_ATTRIBUTE = "shop_review_preprocessor_offset";
	public static final String COUNT_ATTRIBUTE = "shop_review_preprocessor_count";

	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		try {
			int itemId = (int) Integer.parseInt(request.getParameter(SHOP_ID_PARAM));
			int offset = (int) Integer.parseInt(request.getParameter(OFFSET_PARAM));
			int count = (int) Integer.parseInt(request.getParameter(COUNT_PARAM));
			
			//Parameters must be positive numbers
			if(offset < 0 || count <= 0) {
				return false;
			}
			
			request.setAttribute(SHOP_ID_ATTRIBUTE, itemId);
			request.setAttribute(OFFSET_ATTRIBUTE, offset);
			request.setAttribute(COUNT_ATTRIBUTE, count);
			return true;
			
		} catch(NumberFormatException e) {
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
