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

import it.webapp.db.entities.Department;
import it.webapp.requestprocessor.ItemSearchBasePreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ItemSearchBaseView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for a base page on which to view search results
 */
public class ItemSearchBaseController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ItemSearchBase";
	
	public ItemSearchBaseController() {
		super(new ItemSearchBasePreprocessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        String term = (String) request.getAttribute(ItemSearchBasePreprocessor.TERM_ATTRIBUTE);
		Department department = (Department) request.getAttribute(ItemSearchBasePreprocessor.DEPARTMENT_ATTRIBUTE);
		
        ItemSearchBaseView.setRequestParameters(term, department, request);
		ItemSearchBaseView itemSearchBaseView = ViewFactory.getView(ItemSearchBaseView.class);
		itemSearchBaseView.view(request, response);
		
	}

}
