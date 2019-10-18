/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author:
 * 
 ******************************************************************************/

package it.webapp.view;

import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.CartEntryAggregateInfo;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ShopShipmentTypeEntity;
import it.webapp.db.entities.UserEntity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends a form to complete the order with the needed information
 */
public class OrderOptionsView implements View {

	public static final String USER_ADDRESS_ATTRIBUTE = "order_options_view_user_address";
	public static final String SHOP_AGGREGATE_CART_ENTRIES_MAP_ATTRIBUTE = "order_options_view_shop_aggregate_cart_entries_map";
	public static final String SHOP_SHIPMENT_TYPES_MAP_ATTRIBUTE = "order_options_view_shop_shipment_types_map";
    public static final String SHOP_USERS_MAP_ATTRIBUTE = "order_options_view_shop_users_map";
	public static final String REMOVED_ITEMS_ATTRIBUTE = "order_options_view_removed_items";
    private static final String PAGE_URL = "/jsp/OrderOptions.jsp";
	
	/**
	 * Convenience method for setting View parameters
	 * @param userAddress The default address for the current user, not null
	 * @param shopCartAggregateInfoMap Map that associates shop ids to cart entries, not null
	 * @param shopShipmentTypesMap Map that associates shop ids to available shipment types, not null
	 * @param removedItems The items that have been removed from the user cart, not null
     * @param shopUsersMap Map that associates shop ids to user entity, not null
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(AddressEntity userAddress, Map<Integer, List<CartEntryAggregateInfo>> shopCartAggregateInfoMap,
												  Map<Integer, List<ShopShipmentTypeEntity>> shopShipmentTypesMap, List<ItemEntity> removedItems,
												  Map<Integer, UserEntity> shopUsersMap, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		if(userAddress == null || shopCartAggregateInfoMap == null || shopShipmentTypesMap == null || removedItems == null || shopUsersMap == null) {
			throw new IllegalArgumentException("Parameters must be non null");
        }
        
		request.setAttribute(USER_ADDRESS_ATTRIBUTE, userAddress);
		request.setAttribute(SHOP_AGGREGATE_CART_ENTRIES_MAP_ATTRIBUTE, shopCartAggregateInfoMap);
		request.setAttribute(SHOP_SHIPMENT_TYPES_MAP_ATTRIBUTE, shopShipmentTypesMap);
        request.setAttribute(SHOP_USERS_MAP_ATTRIBUTE, shopUsersMap);
		request.setAttribute(REMOVED_ITEMS_ATTRIBUTE, removedItems);
	}
	
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
		try {
            request.getRequestDispatcher(PAGE_URL).forward(request, response);
        } catch (IOException | ServletException exception) {
            throw new IllegalArgumentException("Invalid redirect path " +  request.getContextPath() + PAGE_URL, exception);
        }
	}

}
