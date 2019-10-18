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

import it.webapp.requestprocessor.RequestPreprocessor;
import it.webapp.view.ErrorView;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Skeleton class for the implementation of an ApplicationController
 */
public abstract class ApplicationControllerSkeleton implements ApplicationController {

	private RequestPreprocessor requestPreprocessor;
	
	public ApplicationControllerSkeleton(RequestPreprocessor requestPreprocessor) {
		if(requestPreprocessor == null) {
			throw new IllegalArgumentException("RequestProcessor must be not null");
		}
		
		this.requestPreprocessor = requestPreprocessor;
	}
	
	@Override
	public void dispatch(HttpServletRequest request, HttpServletResponse response) {
		if(request == null) {
			throw new IllegalArgumentException("Request cannot be null");
		}
		if(response == null) {
			throw new IllegalArgumentException("Response cannot be null");
		}
		
		//Performs intial request elaboration and checking and calls handling procedures
		//in case of failure
		boolean result = requestPreprocessor.parseAndValidate(request);
		if(result == false) {
			processParseOfValidationFailure(request, response);
			return;
		}
		
		result = requestPreprocessor.checkAuthentication(request);
		if(result == false) {
			processAuthentication(request, response);
			return;
		}
		
		result = requestPreprocessor.checkAuthorization(request);
		if(result == false) {
			processAuthorization(request, response);
			return;
		}
		
		//Initial preprocessing completed successfully, perform request processing
		processRequest(request, response);
	}
	
	/**
	 * Performs request processing and produces a response
	 * Called only if preprocessing completed successfully
	 * 
	 * @param request The request to process
	 * @param response The response to produce
	 */
	public abstract void processRequest(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * Processes a failed parsing or validation of the request
	 * 
	 * @param request The request on which parsing failed
	 * @param response The response to send
	 */
	public void processParseOfValidationFailure(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Bad Parameters", "Request parameters were not set correctly", request);
		errorView.view(request, response);
	}
	
	/**
	 * Processes a failed authentication check
	 * 
	 * @param request The request on which authentication failed
	 * @param response The response to send
	 */
	public void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authentication failure", "User must be authenticated", request);
		errorView.view(request, response);
	}
	
	/**
	 * Processed a failed authorization step
	 * 
	 * @param request The request on which authorization failed
	 * @param response The response to send
	 */
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authorization failure", "The request did not satisfy authorization requirements", request);
		errorView.view(request, response);
	}
	
}
