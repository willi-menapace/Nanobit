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

package it.webapp.view;

import com.google.gson.Gson;
import it.webapp.db.entities.ShopReviewEntity;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ShopReviewView implements View {
	
	public static final String SHOP_REVIEWS_ATTRIBUTE = "shop_review_view_shop_reviews";
	
	/**
	 * Convenience method for setting View parameters
	 * @param shopReviews The shop reviews to send
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(List<ShopReviewEntity> shopReviews, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		request.setAttribute(SHOP_REVIEWS_ATTRIBUTE, shopReviews);
	}

	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {      
        
        List<ShopReviewEntity> shopReviews = (List<ShopReviewEntity>) request.getAttribute(SHOP_REVIEWS_ATTRIBUTE);
        
        // Generate JSON string from itemSearchHits list data 
        String shopReviewsJSONString = new Gson().toJson(shopReviews);
        if(shopReviewsJSONString == null){
            throw new IllegalArgumentException("Error during the managing of JSON data.");
        }
        
        // Writing JSON data to response stream
        try {
            response.setContentType("application/json");
            response.getWriter().write(shopReviewsJSONString);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Error during writing response.", exception);
        }
	}
}
