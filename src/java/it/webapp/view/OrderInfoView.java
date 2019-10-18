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

import it.webapp.db.entities.ShopOrderAggregateInfo;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends order information as response
 */
public class OrderInfoView implements View {

	private static final String PAGE_URL = "/jsp/OrderInfo.jsp";
	
	public static final String ORDER_INFO_ATTRIBUTE = "order_info_view_order_info";
        public static final String USER_ID_ATTRIBUTE = "order_info_user_id";
	
	/**
	 * Convenience method for setting View parameters
	 * @param orderInfo Information about the order
         * @param shopMode Set to visualize the order information in shop mode
         * @param shopUserId Set to visualize the order information in shop mode. If shopMode is false then shopUserId will be -1
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(ShopOrderAggregateInfo orderInfo, int userId, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		if(orderInfo == null) {
			throw new IllegalArgumentException("Parameters must be non null");
		}
		
		request.setAttribute(ORDER_INFO_ATTRIBUTE, orderInfo);
                request.setAttribute(USER_ID_ATTRIBUTE, userId);
                
	}

	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
		try {
            request.getRequestDispatcher(PAGE_URL).forward(request, response);
        } catch (IOException | ServletException exception) {
            throw new IllegalArgumentException("Invalid redirect path " +  request.getContextPath() + PAGE_URL, exception);
        }
	}
	
}
