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

import it.webapp.requestprocessor.OrderBasePreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ShopOrdersBaseView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Manages a request for a user orders base page
 */
public class ShopOrdersBaseController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ShopOrdersBase";
	
	public ShopOrdersBaseController() {
		super(new OrderBasePreprocessor());
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		ShopOrdersBaseView view = ViewFactory.getView(ShopOrdersBaseView.class);
		view.view(request, response);	
	}

}