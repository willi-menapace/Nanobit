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

package it.webapp.view;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends an order confirmation message as response
 */
public class PlaceOrderSuccessfulView implements View {

        private static final String PAGE_URL = "/jsp/PlaceOrderSuccessful.jsp";
        
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
            try {
                    request.getRequestDispatcher(PAGE_URL).forward(request, response);
                } catch (IOException | ServletException exception) {
            throw new IllegalArgumentException("Invalid redirect path " +  request.getContextPath() + PAGE_URL, exception);
                }
            }   

}
