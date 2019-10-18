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
 * Preprocesses a request for the shop orders placed by a user
 */
public class ShopOrdersPreprocessor implements RequestPreprocessor {

	public static final String OFFSET_PARAM = "shop_orders_preprocessor_offset";
	public static final String COUNT_PARAM = "shop_orders_preprocessor_count";
	
	public static final String SHOP_ID_ATTRIBUTE = "shop_orders_preprocessor_user_id";
	public static final String OFFSET_ATTRIBUTE = "shop_orders_preprocessor_offset";
	public static final String COUNT_ATTRIBUTE = "shop_orders_preprocessor_count";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int offset = (int) Integer.parseInt(request.getParameter(OFFSET_PARAM));
			int count = (int) Integer.parseInt(request.getParameter(COUNT_PARAM));
			
			//Parameters must be positive numbers
			if(offset < 0 || count <= 0) {
				return false;
			}
			
			request.setAttribute(OFFSET_ATTRIBUTE, offset);
			request.setAttribute(COUNT_ATTRIBUTE, count);
			return true;
			
		} catch(NumberFormatException e) {
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		
		if(authenticationInfo.isAuthenticated()) {
			//Obtains and sets the id of the user performing the request
			AuthenticationInfo authUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			int userId = authUserInfo.getUserId();
			
			request.setAttribute(SHOP_ID_ATTRIBUTE, userId);
			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		
		//User must be a shop to view this page
		if(authenticationInfo.isAuthenticated() && authenticationInfo.isShop()) {
			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}

}