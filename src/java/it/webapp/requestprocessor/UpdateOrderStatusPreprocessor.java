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

import it.webapp.applicationcontroller.AuthenticatedUserInfo;
import it.webapp.applicationcontroller.AuthenticationInfo;
import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcShopOrderDao;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderStatusType;
import it.webapp.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Preprocesses a requset to update an order status
 */
public class UpdateOrderStatusPreprocessor implements RequestPreprocessor {

	public static final String SHOP_ORDER_ID_PARAM = "update_order_status_preprocessor_shop_order_id";
	public static final String ORDER_STATUS_TYPE_ID_PARAM = "update_order_status_preprocessor_order_status_type_id";
	
	public static final String SHOP_ORDER_ID_ATTRIBUTE = "update_order_status_preprocessor_shop_order_id";
	public static final String ORDER_STATUS_TYPE_ID_ATTRIBUTE = "update_order_status_preprocessor_order_status_type_id";
	
	private JdbcShopOrderDao shopOrderDao;
	
	public UpdateOrderStatusPreprocessor() throws RequestPreprocessorException {
		try {
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
		} catch (DaoFactoryException e) {
			throw new RequestPreprocessorException("Could not initialize preprocessor", e);
		}
	}
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		try {

			int orderId = Integer.parseInt(request.getParameter(SHOP_ORDER_ID_PARAM));
			int orderStatusTypeId = Integer.parseInt(request.getParameter(ORDER_STATUS_TYPE_ID_PARAM));
			
			ShopOrderStatusType shopOrderStatus = ShopOrderStatusType.getById(orderStatusTypeId);
			
			request.setAttribute(SHOP_ORDER_ID_ATTRIBUTE, orderId);
			request.setAttribute(ORDER_STATUS_TYPE_ID_ATTRIBUTE, shopOrderStatus);
			
			return true;
			
		} catch(NumberFormatException e) {
			//Parameters were not valid
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("AuthenticationInfo must be always set");
		}
				
		if(authenticationInfo.isAuthenticated()) {
			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("AuthenticationInfo must be always set");
		}
		
		try {
			int shopOrderId = (int) request.getAttribute(SHOP_ORDER_ID_ATTRIBUTE);
			ShopOrderEntity shopOrder = shopOrderDao.getById(shopOrderId);
			
			//The user must be the shop on which the order was placed
			if(authenticationInfo.isAuthenticated() && authenticationInfo.isShop()) {

				AuthenticatedUserInfo authUserInfo = (AuthenticatedUserInfo) authenticationInfo;
				int authShopId = authUserInfo.getUserId();

				//Shop must be the same on which the order was placed
				return authShopId == shopOrder.getShopId();
			} else {
				//User is not authenticated
				return false;
			}
		} catch(DaoException e) {
			Logger.log("Could not preprocess request due to database error", e);
			return false;
		}
	}

}
