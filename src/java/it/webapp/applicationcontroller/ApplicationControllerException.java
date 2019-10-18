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

/**
 * Exception caused by the operation of an ApplicationController
 */
public class ApplicationControllerException extends Exception {

	/**
	 * Creates a new Exception
	 * 
	 * @param message Failure description
	 */
	public ApplicationControllerException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception
	 * 
	 * @param message Failure description
	 * @param cause Exception that caused the failure
	 */
	public ApplicationControllerException(String message, Exception cause) {
		super(message, cause);
	}
}
