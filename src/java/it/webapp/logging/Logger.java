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

package it.webapp.logging;

import javax.servlet.ServletContext;

public class Logger {

	//The context to use for logging
	private static ServletContext context;
	
	public static void init(ServletContext contextParam) {
		if(contextParam == null) {
			throw new IllegalArgumentException("Context must not be null");
		}
		
		context = contextParam;
	}
	
	/**
	 * Logs a message on the server log
	 * 
	 * @param message The message to log
	 */
	public static void log(String message) {
		if(context == null) {
			throw new IllegalStateException("Logger must be initialized before use");
		}
		
		context.log(message);
	}
	
	/**
	 * Logs a message on the server log
	 * 
	 * @param message The message to log
	 * @param throwable Throwable object for which to print descriptive information on the log
	 */
	public static void log(String message, Throwable throwable) {
		if(context == null) {
			throw new IllegalStateException("Logger must be initialized before use");
		}
		
		context.log(message, throwable);
	}
	
}
