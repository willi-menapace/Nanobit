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
import java.io.IOException;
import java.util.List;
import javafx.util.Pair;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends information about matched items as response
 */
public class ItemSearchHintView implements View {

	public static final String ITEM_DESCRIPTORS = "item_search_hint_view";

	/**
	 * Convenience method for setting View parameters
	 * @param itemDescriptors List of item descriptors
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(List<Pair<Integer, String>> itemDescriptors, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		request.setAttribute(ITEM_DESCRIPTORS, itemDescriptors);
	}
	
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
		
        List itemSearchHints = (List) request.getAttribute(ITEM_DESCRIPTORS);        
        
        // Generate JSON string from itemSearchHits list data 
        String itemSearchHintsJSONString = new Gson().toJson(itemSearchHints);
        if(itemSearchHintsJSONString == null){
            throw new IllegalArgumentException("Error during the managing of JSON data.");
        }
        
        // Writing JSON data to response stream
        try {
            response.setContentType("application/json");
            response.getWriter().write(itemSearchHintsJSONString);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Error during writing response.", exception);
        }
        
	}
	
}
