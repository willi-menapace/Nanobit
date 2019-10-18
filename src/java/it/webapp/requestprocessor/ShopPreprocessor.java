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

public class ShopPreprocessor implements RequestPreprocessor {

	public static final String SHOP_ID_PARAM = "shop_preprocessor_shop_id";
	
	public static final String SHOP_ID_ATTRIBUTE = "shop_preprocessor_shop_id";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int shopId = Integer.parseInt(request.getParameter(SHOP_ID_PARAM));
			
			request.setAttribute(SHOP_ID_ATTRIBUTE, shopId);
		
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
