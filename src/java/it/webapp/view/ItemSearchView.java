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
import it.webapp.db.entities.ItemAggregateInfo;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends item search results as response
 */
public class ItemSearchView implements View {

	public static final String ITEM_AGGREGATE_INFO_LIST = "item_search_view_item_aggregate_info_list";
	
	/**
	 * Convenience method for setting View parameters
	 * @param itemAggregateInfo Item search results list, null in case of error
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(List<ItemAggregateInfo> itemAggregateInfo, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}

		
		request.setAttribute(ITEM_AGGREGATE_INFO_LIST, itemAggregateInfo);
	}
	
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
		
        List<ItemAggregateInfo> itemAggregateInfo = (List<ItemAggregateInfo>) request.getAttribute(ITEM_AGGREGATE_INFO_LIST);
        
        // Generate JSON string from itemSearchHits list data 
        String itemAggregateInfoJSONString = new Gson().toJson(itemAggregateInfo);
        if(itemAggregateInfoJSONString == null){
            throw new IllegalArgumentException("Error during the managing of JSON data.");
        }
        
        // Writing JSON data to response stream
        try {
            response.setContentType("application/json");
            response.getWriter().write(itemAggregateInfoJSONString);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Error during writing response.", exception);
        }
        
	}

}
