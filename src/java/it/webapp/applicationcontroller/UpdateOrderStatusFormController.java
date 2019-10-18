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

import it.webapp.requestprocessor.RequestPreprocessorException;
import it.webapp.requestprocessor.UpdateOrderStatusFormPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.UpdateOrderStatusView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to update the status of a certain order
 */
public class UpdateOrderStatusFormController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UpdateOrderStatusForm";
	
	public UpdateOrderStatusFormController() throws ApplicationControllerException, RequestPreprocessorException {
		super(new UpdateOrderStatusFormPreprocessor());
		
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int shopOrderId = (int) request.getAttribute(UpdateOrderStatusFormPreprocessor.SHOP_ORDER_ID_ATTRIBUTE);
		
		UpdateOrderStatusView.setRequestParameters(shopOrderId, null, request);
		View view = ViewFactory.getView(UpdateOrderStatusView.class);
		view.view(request, response);
	}

}