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

import it.webapp.requestprocessor.LoginFormPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.LoginView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages the access to the login page
 */
public class LoginFormController extends ApplicationControllerSkeleton {

    public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "LoginForm";
    
    public LoginFormController() {
        super(new LoginFormPreprocessor());
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        String redirect = (String) request.getAttribute(LoginFormPreprocessor.REDIRECT_ATTRIBUTE);
        
        // Prepare the view and then we call it
        LoginView.setRequestParameters(redirect, "", request);
        LoginView loginView = ViewFactory.getView(LoginView.class);
        loginView.view(request, response);
    }
    
}
