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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Component capable of processing and responding to a user request
 */
public interface ApplicationController {
	
	/**
	 * Carries out the actions necessary to process and respond to a given request
	 * 
	 * @param request The request to process
	 * @param response The response to send
	 */
	public void dispatch(HttpServletRequest request, HttpServletResponse response);
}
