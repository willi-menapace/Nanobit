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
import it.webapp.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Preprocesses a request for detailed order info
 */
public class OrderInfoPreprocessor implements RequestPreprocessor {

	public static final String SHOP_ORDER_ID_PARAM = "order_info_preprocessor_shop_order_id";
	
	public static final String SHOP_ORDER_ID_ATTRIBUTE = "order_info_preprocessor_shop_order_id";
	
	private JdbcShopOrderDao shopOrderDao;
	
	public OrderInfoPreprocessor() throws RequestPreprocessorException {
		try {
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
		} catch (DaoFactoryException e) {
			throw new RequestPreprocessorException("Could not initialize dao", e);
		}
	}
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int shopOrderId = Integer.parseInt(request.getParameter(SHOP_ORDER_ID_PARAM));
		
			request.setAttribute(SHOP_ORDER_ID_ATTRIBUTE, shopOrderId);
			return true;
		} catch(NumberFormatException e) {
			//Parameter was not valid
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
		//Checks if the order was placed by the current user or received by the current shop
		int shopOrderId = (int) request.getAttribute(SHOP_ORDER_ID_ATTRIBUTE);
		
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("AuthenticationInfo must be always set");
		}
		
		AuthenticatedUserInfo authUserInfo = (AuthenticatedUserInfo) authenticationInfo;
		
		try {
			ShopOrderEntity shopOrder = shopOrderDao.getById(shopOrderId);
			//Checks if the given shop order exists
			if(shopOrder == null) {
				return false;
			}
			
			int authenticatedUserId = authUserInfo.getUserId();
			//If the authenticated user is the shop or the user itself then it is granted access
			if(shopOrder.getUserId() == authenticatedUserId || shopOrder.getShopId() == authenticatedUserId || authUserInfo.isAdmin()) {
				return true;
			} else {
				return false;
			}
		} catch(DaoException e) {
			Logger.log("Cannot access database", e);
			
			return false;
		}
	}

}
