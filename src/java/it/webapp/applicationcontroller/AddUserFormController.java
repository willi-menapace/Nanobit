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
import it.webapp.view.AddUserFormView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for a page with a registration form
 */
public class AddUserFormController extends ApplicationControllerSkeleton {

    public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddUserForm";
    
    public AddUserFormController() {
        super(new EmptyRequestProcessor());
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        AddUserFormView addUserFormView = ViewFactory.getView(AddUserFormView.class);
        addUserFormView.view(request, response);
    }
    
}
