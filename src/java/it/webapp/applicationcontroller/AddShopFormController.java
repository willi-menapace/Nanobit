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

package it.webapp.applicationcontroller;

import it.webapp.requestprocessor.EmptyRequestProcessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.AddShopView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages the access to the add shop page
 */
public class AddShopFormController extends ApplicationControllerSkeleton  {

    public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddShopForm";
    
    public AddShopFormController() {
        super(new EmptyRequestProcessor());
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        // Prepare the view and then we call it
        AddShopView addShopView = ViewFactory.getView(AddShopView.class);
        addShopView.view(request, response);
    }
    
}
