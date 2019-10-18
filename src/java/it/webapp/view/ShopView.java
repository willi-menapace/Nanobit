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
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.ShopEntity;
import it.webapp.db.entities.ShopReviewAggregateInfo;
import it.webapp.db.entities.UserEntity;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShopView implements View {

	private static final String PAGE_URL = "/jsp/Shop.jsp";
	
	public static final String SHOP_ATTRIBUTE = "shop_view_shop";
	public static final String ADDRESS_ATTRIBUTE = "shop_view_address";
	public static final String IMAGES_ATTRIBUTE = "shop_view_images";
	public static final String RATING_AGGREGATE_ATTRIBUTE = "shop_view_rating_aggregate";
	public static final String USER_ATTRIBUTE = "shop_view_user";
    
	/**
	 * Convenience method for setting View parameters
     * @param shop the shop general information like description, vat number..
     * @param address the shop address
     * @param images all the images that the shop has
     * @param shopReviewAggregateInfo the reviews info for the shop
     * @param shopUser  user associated to the shop
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(ShopEntity shop, AddressEntity address, List<ResourceEntity> images, ShopReviewAggregateInfo shopReviewAggregateInfo, UserEntity shopUser, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(shop == null || address == null || images == null || shopReviewAggregateInfo == null || shopUser == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
		
		request.setAttribute(SHOP_ATTRIBUTE, shop);
		request.setAttribute(ADDRESS_ATTRIBUTE, address);
		request.setAttribute(IMAGES_ATTRIBUTE, images);
		request.setAttribute(RATING_AGGREGATE_ATTRIBUTE, shopReviewAggregateInfo);
        request.setAttribute(USER_ATTRIBUTE, shopUser);
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
