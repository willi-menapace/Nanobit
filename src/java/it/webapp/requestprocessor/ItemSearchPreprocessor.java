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

import it.webapp.db.dao.ItemDao;
import it.webapp.db.dao.ItemDao.SortOrder;
import it.webapp.db.entities.Department;
import it.webapp.spatial.CircularGeoFence;
import it.webapp.spatial.GeoFence;
import java.math.BigDecimal;
import java.math.MathContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Preprocesses a request for an item search
 */
public class ItemSearchPreprocessor implements RequestPreprocessor {

	public static final String TERM_PARAM = "item_search_preprocessor_term";
	public static final String DEPARTMENT_PARAM = "item_search_preprocessor_department";
	public static final String PRICE_LOW_PARAM = "item_search_preprocessor_price_low";
	public static final String PRICE_HIGH_PARAM = "item_search_preprocessor_price_high";
	public static final String GEOFENCE_CENTER_LAT_PARAM = "item_search_preprocessor_geofence_center_lat";
	public static final String GEOFENCE_CENTER_LNG_PARAM = "item_search_preprocessor_geofence_center_lng";
	public static final String GEOFENCE_RADIUS_PARAM = "item_search_preprocessor_geofence_radius";
	public static final String ORDER_PARAM = "item_search_preprocessor_order";
	public static final String OFFSET_PARAM = "item_search_preprocessor_offset";
	public static final String COUNT_PARAM = "item_search_preprocessor_count";
	
	public static final String TERM_ATTRIBUTE = "item_search_preprocessor_term";
	public static final String DEPARTMENT_ATTRIBUTE = "item_search_preprocessor_department";
	public static final String PRICE_LOW_ATTRIBUTE = "item_search_preprocessor_price_low";
	public static final String PRICE_HIGH_ATTRIBUTE = "item_search_preprocessor_price_high";
	public static final String GEOFENCE_ATTRIBUTE = "item_search_preprocessor_geofence";
	public static final String ORDER_ATTRIBUTE = "item_search_preprocessor_order";
	public static final String OFFSET_ATTRIBUTE = "item_search_preprocessor_offset";
	public static final String COUNT_ATTRIBUTE = "item_search_preprocessor_count";
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			String term = request.getParameter(TERM_PARAM);
			String departmentString = request.getParameter(DEPARTMENT_PARAM);
			String priceLowString = request.getParameter(PRICE_LOW_PARAM);
			String priceHighString = request.getParameter(PRICE_HIGH_PARAM);
			String geofenceCenterLatString = request.getParameter(GEOFENCE_CENTER_LAT_PARAM);
			String geofenceCenterLngString = request.getParameter(GEOFENCE_CENTER_LNG_PARAM);
			String geofenceRadiusString = request.getParameter(GEOFENCE_RADIUS_PARAM);
		
			int sortOrderId = Integer.parseInt(request.getParameter(ORDER_PARAM));
			int offset = Integer.parseInt(request.getParameter(OFFSET_PARAM));
			int count = Integer.parseInt(request.getParameter(COUNT_PARAM));
            
			SortOrder sortOrder = ItemDao.SortOrder.getById(sortOrderId);
			if(sortOrder == null) {
                //SortOrder was not valid
				return false;
			}
            
			Department department = null;
			if(departmentString != null && !departmentString.isEmpty()) {
				department = Department.getById(Integer.parseInt(departmentString));
			}
			
			BigDecimal priceLow = null;
			if(priceLowString != null && !priceLowString.isEmpty()) {
				priceLow = new BigDecimal(priceLowString, MathContext.UNLIMITED);
			}
			
			BigDecimal priceHigh = null;
			if(priceHighString != null && !priceHighString.isEmpty()) {
				priceHigh = new BigDecimal(priceHighString, MathContext.UNLIMITED);
			}
			
            /* Se le stringhe sono vuote, imposto a null */
            if(geofenceCenterLatString != null && geofenceCenterLatString.isEmpty()){
                geofenceCenterLatString = null;
            }
            if(geofenceCenterLngString != null && geofenceCenterLngString.isEmpty()){
                geofenceCenterLngString = null;
            }
            if(geofenceRadiusString != null && geofenceRadiusString.isEmpty()){
                geofenceRadiusString = null;
            }
            
			GeoFence geoFence = null;
			if(geofenceCenterLatString != null || geofenceCenterLngString != null || geofenceRadiusString != null) {
				//All or none must be null
				if(geofenceCenterLatString == null || geofenceCenterLngString == null || geofenceRadiusString == null) {
					return false;
				}
				BigDecimal latCenter = new BigDecimal(geofenceCenterLatString, MathContext.UNLIMITED);
				BigDecimal lngCenter = new BigDecimal(geofenceCenterLngString, MathContext.UNLIMITED);
				double radius = Double.parseDouble(geofenceRadiusString);
				geoFence = new CircularGeoFence(latCenter, lngCenter, radius);
			}
            
            if(term.isEmpty()){
                term = null;
            }
			
			request.setAttribute(TERM_ATTRIBUTE, term);
			request.setAttribute(DEPARTMENT_ATTRIBUTE, department);
			request.setAttribute(PRICE_LOW_ATTRIBUTE, priceLow);
			request.setAttribute(PRICE_HIGH_ATTRIBUTE, priceHigh);
			request.setAttribute(GEOFENCE_ATTRIBUTE, geoFence);
			request.setAttribute(ORDER_ATTRIBUTE, sortOrder);
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
