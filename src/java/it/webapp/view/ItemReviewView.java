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

import com.google.gson.Gson;
import it.webapp.db.entities.ItemReviewEntity;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ItemReviewView implements View {
	
	public static final String ITEM_REVIEWS_ATTRIBUTE = "item_review_view_item_reviews";
	
	/**
	 * Convenience method for setting View parameters
	 * @param itemReviews The item reviews to send
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(List<ItemReviewEntity> itemReviews, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		request.setAttribute(ITEM_REVIEWS_ATTRIBUTE, itemReviews);
	}

	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
        
        List<ItemReviewEntity> itemReviews = (List<ItemReviewEntity>) request.getAttribute(ITEM_REVIEWS_ATTRIBUTE);
        
        // Generate JSON string from itemSearchHits list data 
        String itemReviewsJSONString = new Gson().toJson(itemReviews);
        if(itemReviewsJSONString == null){
            throw new IllegalArgumentException("Error during the managing of JSON data.");
        }
        
        // Writing JSON data to response stream
        try {
            response.setContentType("application/json");
            response.getWriter().write(itemReviewsJSONString);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Error during writing response.", exception);
        }
        
    }
}
