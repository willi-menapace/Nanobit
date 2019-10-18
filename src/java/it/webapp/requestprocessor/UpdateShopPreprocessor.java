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

public class UpdateShopPreprocessor implements RequestPreprocessor {

	public static final String DESCRIPTION_PARAMAM = "update_shop_preprocessor_description";
	public static final String PHONE_PARAMAM = "update_shop_preprocessor_phone";
	
	public static final String SHOP_ID_ATTRIBUTE = "update_shop_preprocessor_shop_id";
	public static final String DESCRIPTION_ATTRIBUTE= "update_shop_preprocessor_description";
	public static final String PHONE_ATTRIBUTE = "update_shop_preprocessor_phone";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		String description = request.getParameter(DESCRIPTION_PARAMAM);
		String phone = request.getParameter(PHONE_PARAMAM);
		
		if(description == null || phone == null) {
			return false;
		}
		if(description.isEmpty() || phone.isEmpty()) {
			return false;
		}
		
		request.setAttribute(DESCRIPTION_ATTRIBUTE, description);
		request.setAttribute(PHONE_ATTRIBUTE, phone);
		
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("Authentication info must always be present");
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
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		
		if(authenticationInfo.isAuthenticated() && authenticationInfo.isShop()) {
			//Retrieves the shop id on which to perform the action and sets its attribute
			AuthenticatedUserInfo authenticatedUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			request.setAttribute(SHOP_ID_ATTRIBUTE, authenticatedUserInfo.getUserId());
			return true;
		} else {
			//User is not an authenticated shop
			return false;
		}
	}

}
