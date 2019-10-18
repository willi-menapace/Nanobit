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

import com.google.maps.model.LatLng;
import it.webapp.applicationcontroller.AuthenticatedUserInfo;
import it.webapp.applicationcontroller.AuthenticationInfo;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.ShipmentType;
import it.webapp.payment.CreditCard;
import it.webapp.spatial.GeocodingManager;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Preprocesses a request to place an order
 */
public class PlaceOrderPreprocessor implements RequestPreprocessor {

	public static final String STREET_NAME_PARAM = "place_order_preprocessor_street";
	public static final String STREET_NUMBER_PARAM = "place_order_preprocessor_street_number";
	public static final String CITY_PARAM = "place_order_preprocessor_city";
	public static final String DISTRICT_PARAM = "place_order_preprocessor_district";
	public static final String ZIP_PARAM = "place_order_preprocessor_zip";
	public static final String COUNTRY_PARAM = "place_order_preprocessor_country";
	
	public static final String SHOP_IDS_PARAM = "place_order_preprocessor_shop_ids";
	public static final String SHIPMENT_TYPE_IDS_PARAM = "place_order_shop_shipment_type_ids";
	
	public static final String CC_OWNER_PARAM = "place_order_preprocessor_ccowner";
	public static final String CC_NUMBER_PARAM = "place_order_preprocessor_ccnumber";
	public static final String CC_EXPIRATION_YEAR_PARAM = "place_order_preprocessor_ccexpiration_year";
	public static final String CC_EXPIRATION_MONTH_PARAM = "place_order_preprocessor_ccexpiration_month";
	public static final String CC_SECURITY_CODE_PARAM = "place_order_preprocessor_ccsecurity_code";
	
	public static final String USER_ID_ATTRIBUTE = "place_order_user_id";
	public static final String SHIPMENT_ADDRESS_ATTRIBUTE = "place_order_shipment_address";
	public static final String SHOP_SHIPMENT_TYPE_MAP_ATTRIBUTE = "place_order_shop_shipment_type_map";
	public static final String CREDIT_CARD_ATTRIBUTE = "place_order_preprocessor_credit_card";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			String streetName = request.getParameter(STREET_NAME_PARAM);
			String streetNumber = request.getParameter(STREET_NUMBER_PARAM);
			String city = request.getParameter(CITY_PARAM);
			String district = request.getParameter(DISTRICT_PARAM);
			String zip = request.getParameter(ZIP_PARAM);
			String country = request.getParameter(COUNTRY_PARAM);
			
			String ccOwner = request.getParameter(CC_OWNER_PARAM);
			String ccNumber = request.getParameter(CC_NUMBER_PARAM);
			int ccExpirationYear = Integer.parseInt(request.getParameter(CC_EXPIRATION_YEAR_PARAM));
			int ccExpirationMonth = Integer.parseInt(request.getParameter(CC_EXPIRATION_MONTH_PARAM));
			String ccSecurityCode = request.getParameter(CC_SECURITY_CODE_PARAM);
			
			//Parameters must be not null
			if(streetName == null || streetNumber == null || city == null ||
			    district == null || zip == null || country == null) {
				return false;
			}

			//Parameters must be not empty
			if(streetNumber.isEmpty() || city.isEmpty() || district.isEmpty() || zip.isEmpty() || 
			   country.isEmpty()) {
				return false;
			}
			
			//Checks validity of the address
			LatLng addressCoordinates = GeocodingManager.getLatLngFromAddress(streetName, streetNumber, zip, city, district, country);
			if(addressCoordinates == null) {
				//Address was not valid
				return false;
			}
			
			AddressEntity address = new AddressEntity(null, streetName, streetNumber, city, district, zip, country, new BigDecimal(addressCoordinates.lat), new BigDecimal(addressCoordinates.lng));
			
			if(ccOwner == null || ccNumber == null || ccSecurityCode == null) {
				return false;
			}
			
			if(ccOwner.isEmpty() || ccNumber.isEmpty() || ccSecurityCode.isEmpty()) {
				return false;
			}
			
			//Builds credit card information
			CreditCard creditCart = new CreditCard(ccOwner, ccNumber, ccExpirationYear, ccExpirationMonth, ccSecurityCode);
			
			//Populates the shopId -> shopShipmentTypeId map
			String shopIdsParam[] = request.getParameterValues(SHOP_IDS_PARAM);
			String shopShipmentTypeIdsParam[] = request.getParameterValues(SHIPMENT_TYPE_IDS_PARAM);

			Map<Integer, ShipmentType> shopShipmentTypeMap = new HashMap<>();
			if(shopIdsParam == null || shopShipmentTypeIdsParam == null) {
				return false;
			}
            
			//There must be the same number of arguments in each list
			if(shopIdsParam.length != shopShipmentTypeIdsParam.length) {
				return false;
			}

			//Populates the map
			for(int i = 0; i < shopIdsParam.length; ++i) {
				int shopId = Integer.parseInt(shopIdsParam[i]);
				ShipmentType shipmentType = ShipmentType.getById(Integer.parseInt(shopShipmentTypeIdsParam[i]));
				shopShipmentTypeMap.put(shopId, shipmentType);
			}
			
			request.setAttribute(SHIPMENT_ADDRESS_ATTRIBUTE, address);
			request.setAttribute(CREDIT_CARD_ATTRIBUTE, creditCart);
			request.setAttribute(SHOP_SHIPMENT_TYPE_MAP_ATTRIBUTE, shopShipmentTypeMap);
			
			return true;

		} catch(NumberFormatException e) {
			//Parameters were not valid
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
			//User must be authenticated to complete its orders
			AuthenticatedUserInfo authenticatedUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			request.setAttribute(USER_ID_ATTRIBUTE, authenticatedUserInfo.getUserId());
			return true;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		//No authorization required
		return true;
	}

}
