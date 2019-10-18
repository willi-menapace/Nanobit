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

import it.webapp.applicationcontroller.AuthenticatedUserInfo;
import it.webapp.applicationcontroller.AuthenticationInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Preprocessor for a request to change the quantity of a certain item in the cart
 */
public class UpdateCartItemQuantityPreprocessor implements RequestPreprocessor {

	public static final String SHOP_ITEM_ID_PARAM = "update_cart_item_quantity_preprocessor_shop_item_id";
	public static final String QUANTITY_PARAM = "update_cart_item_quantity_preprocessor_quantity";
	
	public static final String USER_ID_ATTRIBUTE = "update_cart_item_quantity_preprocessor_user_id";
	public static final String SHOP_ITEM_ID_ATTRIBUTE = "update_cart_item_quantity_preprocessor_shop_item_id";
	public static final String QUANTITY_ATTRIBUTE = "update_cart_item_quantity_preprocessor_quantity";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int shopItemId = Integer.parseInt(request.getParameter(SHOP_ITEM_ID_PARAM));
			int quantity = Integer.parseInt(request.getParameter(QUANTITY_PARAM));
			
			if(quantity < 0) {
				return false;
			}

			request.setAttribute(SHOP_ITEM_ID_ATTRIBUTE, shopItemId);
			request.setAttribute(QUANTITY_ATTRIBUTE, quantity);
			
			return true;
		//The parameters were not numbers
		} catch(NumberFormatException e) {
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		//Only an authenticated user can log out
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("Authentication info must always be present");
		}
		
		if(authenticationInfo.isAuthenticated() == false) {
			return false;
		} else {
			//User must be authenticated to update its cart
			AuthenticatedUserInfo authenticatedUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			request.setAttribute(USER_ID_ATTRIBUTE, authenticatedUserInfo.getUserId());
			return true;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization is necessary
		return true;
	}

}
