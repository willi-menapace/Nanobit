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

import it.webapp.applicationcontroller.AuthenticationInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Preprocesses a requset to update an order status
 */
public class UpdateOrderStatusFormPreprocessor implements RequestPreprocessor {

	public static final String SHOP_ORDER_ID_PARAM = "update_order_status_form_preprocessor_shop_order_id";
	
	public static final String SHOP_ORDER_ID_ATTRIBUTE = "update_order_status_form_preprocessor_shop_order_id";
	
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		try {

			int orderId = Integer.parseInt(request.getParameter(SHOP_ORDER_ID_PARAM));
			
			request.setAttribute(SHOP_ORDER_ID_ATTRIBUTE, orderId);
			
			return true;
			
		} catch(NumberFormatException e) {
			//Parameters were not valid
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("AuthenticationInfo must be always set");
		}
				
		if(authenticationInfo.isAuthenticated()) {
			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required for form viewing
		return true;
	}

}