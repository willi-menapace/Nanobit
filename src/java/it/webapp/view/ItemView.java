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

import it.webapp.db.entities.ItemAggregateInfo;
import it.webapp.db.entities.ItemShopAvailabilityInfo;
import it.webapp.db.entities.ResourceEntity;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends detailed item information as response
 */
public class ItemView implements View {

	public static final String ITEM_ATTRIBUTE = "item_view_item";
	public static final String IMAGES_ATTRIBUTE = "item_view_images";
	public static final String AVAILABILTY_INFO_ATTRIBUTE = "item_view_availability_info";
    private static final String PAGE_URL = "/jsp/Item.jsp";
	
	/**
	 * Convenience method for setting View parameters
	 * @param item The item
	 * @param images Images of the item
	 * @param availabilityInfo Info about the item availability with one entry per shop
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(ItemAggregateInfo item, List<ResourceEntity> images, List<ItemShopAvailabilityInfo> availabilityInfo, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(item == null || images == null || availabilityInfo == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
		
		request.setAttribute(ITEM_ATTRIBUTE, item);
		request.setAttribute(IMAGES_ATTRIBUTE, images);
		request.setAttribute(AVAILABILTY_INFO_ATTRIBUTE, availabilityInfo);
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
