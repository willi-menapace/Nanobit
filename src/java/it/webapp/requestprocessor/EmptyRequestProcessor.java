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
 * A request processor that does nothing
 * Every action does nothing and always succeed
 */
public class EmptyRequestProcessor implements RequestPreprocessor {

	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		return true;
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		return true;
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		return true;
	}

}
