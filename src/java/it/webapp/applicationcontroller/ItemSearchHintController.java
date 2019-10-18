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

package it.webapp.applicationcontroller;

import it.webapp.db.indexing.LuceneIndexHelper;
import it.webapp.requestprocessor.ItemSearchHintPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ItemSearchHintView;
import it.webapp.view.ViewFactory;
import java.io.IOException;
import java.util.List;
import javafx.util.Pair;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for item search hints
 */
public class ItemSearchHintController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ItemSearchHint";
	
	public ItemSearchHintController() {
		super(new ItemSearchHintPreprocessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String query = (String) request.getAttribute(ItemSearchHintPreprocessor.QUERY_ATTRIBUTE);
		int count = (int) request.getAttribute(ItemSearchHintPreprocessor.COUNT_ATTRIBUTE);
		
		List<Pair<Integer, String>> matchedItems;
		
		try {
			//Retrieves the matched items
			matchedItems = LuceneIndexHelper.getFromPartialQuery(query, count);
		} catch(IOException e) {
			//Null signals an error condition.
			matchedItems = null;
		}
		
		ItemSearchHintView.setRequestParameters(matchedItems, request);
		ItemSearchHintView itemSearchHintView = ViewFactory.getView(ItemSearchHintView.class);
		itemSearchHintView.view(request, response);
	}

}
