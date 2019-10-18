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
import it.webapp.db.entities.ShipmentType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UpdateShopShipmentTypePreprocessor implements RequestPreprocessor {

	public static final String SHIPMENT_TYPE_IDS_PARAM = "update_shop_shipment_type_preprocessor_shipment_type_ids";
	public static final String PRICES_PARAM = "update_shop_shipment_type_preprocessor_prices";
	
	public static final String SHOP_ID_ATTRIBUTE = "update_shop_shipment_type_preprocessor_shop_id";
	public static final String SHIPMENT_TYPE_IDS_ATTRIBUTE = "update_shop_shipment_type_preprocessor_shipment_type_ids";
	public static final String PRICES_ATTRIBUTE = "update_shop_shipment_type_preprocessor_prices";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		String shipmentTypesIdsParam = request.getParameter(SHIPMENT_TYPE_IDS_PARAM);
		String pricesParam = request.getParameter(PRICES_PARAM);
		
		if(shipmentTypesIdsParam == null || pricesParam == null) {
			return false;
		}
		
		List<ShipmentType> shipmentTypes = new ArrayList<>();
		List<BigDecimal> prices = new ArrayList<>();
		String[] shipmentStringArray = shipmentTypesIdsParam.split("[ ]");
		String[] pricesStringArray = pricesParam.split("[ ]");
		
		//There must be the same number of arguments in each list
		if(pricesStringArray.length != shipmentStringArray.length) {
			return false;
		}
		
		//Populates the lists
		for(int i = 0; i < pricesStringArray.length; ++i) {
			shipmentTypes.add(ShipmentType.getById(Integer.parseInt(shipmentStringArray[i])));
			prices.add(new BigDecimal(pricesStringArray[i]));
		}
		
		request.setAttribute(SHIPMENT_TYPE_IDS_ATTRIBUTE, shipmentTypes);
		request.setAttribute(PRICES_ATTRIBUTE, prices);
		
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		
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
