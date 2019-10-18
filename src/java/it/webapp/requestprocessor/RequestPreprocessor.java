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

package it.webapp.requestprocessor;

import javax.servlet.http.HttpServletRequest;

/**
 * Component that performs request preprocessing such as parsing,
 * validation, authentication and authorization
 */
public interface RequestPreprocessor {
	
	/**
	 * Performs request parsing and input validation.
	 * Builds a representation of the request that is easier to handle
	 * 
	 * @param request The request to parse
	 * @return false in case it is not possible to get the necessary input or
	 *		   the input could not be validated, true otherwise
	 */
	public boolean parseAndValidate(HttpServletRequest request);
	
	/**
	 * Performs user authentication
	 * 
	 * @param request The request on which to perform the authentication check
	 * @return true if the authentication performed by the user is sufficient to
	 *         carry out the request, false otherwise
	 */
	public boolean checkAuthentication(HttpServletRequest request);
	
	/**
	 * Performs a check of user authentication
	 * 
	 * @param request The request on which to perform the authorization check
	 * @return true if the user has sufficient permissions to carry out the request,
	 *         false otherwise
	 */
	public boolean checkAuthorization(HttpServletRequest request);
	
}
