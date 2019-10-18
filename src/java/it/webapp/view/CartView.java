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

import it.webapp.db.entities.CartEntryAggregateInfo;
import it.webapp.db.entities.ItemEntity;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CartView implements View {
	
	private static final String PAGE_URL = "/jsp/Cart.jsp";
	
	public static final String AGGREGATE_CART_ENTRIES_ATTRIBUTE = "cart_view_aggregate_cart_entries";
	public static final String REMOVED_ITEMS_ATTRIBUTE = "cart_view_aggregate_removed_items";
	
	/**
	 * Convenience method for setting View parameters
	 * @param cartEntries The cart entries for the current user
	 * @param removedItems The items that have been removed from the user cart
	 * @param message An optional message
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(List<CartEntryAggregateInfo> cartEntries, List<ItemEntity> removedItems, String message, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		if(cartEntries == null || removedItems == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
		
		request.setAttribute(CartView.AGGREGATE_CART_ENTRIES_ATTRIBUTE, cartEntries);
		request.setAttribute(CartView.REMOVED_ITEMS_ATTRIBUTE, removedItems);
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
