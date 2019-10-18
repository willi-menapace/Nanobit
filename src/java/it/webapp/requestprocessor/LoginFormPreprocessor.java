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

package it.webapp.requestprocessor;

import it.webapp.applicationcontroller.AuthenticationInfo;
import javax.servlet.http.HttpServletRequest;

/**
 * Preprocessor for the login page request
 */
public class LoginFormPreprocessor implements RequestPreprocessor {
    
    public static final String REDIRECT_PARAMETER = "login_form_preprocessor_redirect";
    public static final String REDIRECT_ATTRIBUTE = "login_form_preprocessor_redirect";

    @Override
    public boolean parseAndValidate(HttpServletRequest request) {
        
        String redirectUrl = request.getParameter(REDIRECT_PARAMETER);
        
        //Validates input
        if(redirectUrl == null){
            return false;
        }
        
        //Sets request attributes
        request.setAttribute(REDIRECT_ATTRIBUTE, redirectUrl);
        
        return true;
    }

    @Override
    public boolean checkAuthentication(HttpServletRequest request) {
        AuthenticationInfo authenticationInfo = (AuthenticationInfo) request.getSession().getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		
		if(authenticationInfo == null) {
			throw new IllegalStateException("AuthenticationInfo must be always set");
		}
		
		//Only not authenticated users can access to login page
		return authenticationInfo.isAuthenticated() == false;
    }

    @Override
    public boolean checkAuthorization(HttpServletRequest request) {
        //No authorization required
		return true;
    }
    
}
