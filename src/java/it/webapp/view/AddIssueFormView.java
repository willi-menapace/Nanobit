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

import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends a form to open an Issue as a response
 */
public class AddIssueFormView implements View {

	public static final String SHOP_ORDER_ATTRIBUTE = "add_issue_from_view_shop_order";
	public static final String SHOP_ORDER_ITEM_ATTRIBUTE = "add_issue_from_view_shop_order_item";
	public static final String ITEM_ATTRIBUTE = "add_issue_from_view_item";
	public static final String IMAGE_ATTRIBUTE = "add_issue_from_view_image";
    private static final String PAGE_URL = "/jsp/AddIssueForm.jsp";
	
	/**
	 * Convenience method for setting View parameters
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(ShopOrderEntity shopOrder, ShopOrderItemEntity shopOrderItem, ItemEntity item, ResourceEntity image, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(shopOrder == null || shopOrderItem == null || item == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
        
		request.setAttribute(SHOP_ORDER_ATTRIBUTE, shopOrder);
		request.setAttribute(SHOP_ORDER_ITEM_ATTRIBUTE, shopOrderItem);
		request.setAttribute(ITEM_ATTRIBUTE, item);
		request.setAttribute(IMAGE_ATTRIBUTE, image);
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
