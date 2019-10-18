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

public class RequestPreprocessorException extends Exception {
	
	/**
	 * Creates a new Exception
	 * 
	 * @param message Failure description
	 */
	public RequestPreprocessorException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception
	 * 
	 * @param message Failure description
	 * @param cause Exception that caused the failure
	 */
	public RequestPreprocessorException(String message, Exception cause) {
		super(message, cause);
	}
}
