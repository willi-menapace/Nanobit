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
import it.webapp.applicationcontroller.PersistenceHelper;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.logging.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class AddShopPreprocessor implements RequestPreprocessor {

	public static final String DESCRIPTION_PARAM = "add_shop_preprocessor_description";
	public static final String VAT_PARAM = "add_shop_preprocessor_vat";
	public static final String PHONE_PARAM = "add_shop_preprocessor_phone";
	public static final String IMAGES_PARAM = "add_shop_preprocessor_images";
	
	public static final String SHOP_ID_ATTRIBUTE = "add_shop_preprocessor_shop_id";
	public static final String DESCRIPTION_ATTRIBUTE = "add_shop_preprocessor_description";
	public static final String VAT_ATTRIBUTE = "add_shop_preprocessor_vat";
	public static final String PHONE_ATTRIBUTE = "add_shop_preprocessor_phone";
	public static final String IMAGES_ATTRIBUTE = "add_shop_preprocessor_images";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		String description = request.getParameter(DESCRIPTION_PARAM);
		String vat = request.getParameter(VAT_PARAM);
		String phone = request.getParameter(PHONE_PARAM);
		
		//Validates the input
		if(description == null || vat == null || phone == null) {
			return false;
		}
		if(description.isEmpty() || vat.isEmpty() || phone.isEmpty()) {
			return false;
		}
		
		List<ResourceEntity> images = new ArrayList<>();
		
		try {
			//Gets optional images
			Collection<Part> parts = request.getParts();
		
			for(Part currentPart : parts) {
				if(currentPart.getSubmittedFileName() != null) {
					try {
						ResourceEntity newResource = PersistenceHelper.save(currentPart);
						images.add(newResource);

					//In case of IO problem frees the already saved images
					} catch(IOException e1) {
						try {
							PersistenceHelper.delete(images);
						} catch(IOException e2) {
							Logger.log("Cannot free all the uploaded resources", e2);
						}
						throw e1;
					}
				}
			}

			request.setAttribute(DESCRIPTION_ATTRIBUTE, description);
			request.setAttribute(VAT_ATTRIBUTE, vat);
			request.setAttribute(PHONE_ATTRIBUTE, phone);
			request.setAttribute(IMAGES_ATTRIBUTE, images);
			
			return true;
		
		} catch(ServletException | IOException e) {
			Logger.log("Cannot write uploaded resource", e);
			return false;
		}
		
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		
		if(authenticationInfo.isAuthenticated()) {
			//Retrieves the shop id on which to perform the action and sets its attribute
			AuthenticatedUserInfo authenticatedUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			request.setAttribute(SHOP_ID_ATTRIBUTE, authenticatedUserInfo.getUserId());
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
		
		//User must not be a shop to perform the account upgrade to shop
		if(authenticationInfo.isAuthenticated() && !authenticationInfo.isShop()) {
			return true;
		} else {
			//User is not an authenticated shop
			return false;
		}
	}

}
