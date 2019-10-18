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
import it.webapp.db.dao.jdbc.JdbcShopOrderItemDao;
import it.webapp.db.entities.Rating;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import it.webapp.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Preprocesses a request for the insertion of an item review
 */
public class AddItemReviewPreprocessor implements RequestPreprocessor {

	public static final String SHOP_ORDER_ITEM_ID_PARAM = "add_item_review_preprocessor_shop_order_item_id_param";
	public static final String TITLE_PARAM = "add_item_review_preprocessor_title";
	public static final String DESCRIPTION_PARAM = "add_item_review_preprocessor_description";
	public static final String RATING_PARAM = "add_item_review_preprocessor_rating";
	
	public static final String USER_ID_ATTRIBUTE = "add_item_review_preprocessor_user_id_param";
	public static final String SHOP_ORDER_ITEM_ID_ATTRIBUTE = "add_item_review_preprocessor_shop_order_item_id_param";
	public static final String TITLE_ATTRIBUTE = "add_item_review_preprocessor_title";
	public static final String DESCRIPTION_ATTRIBUTE = "add_item_review_preprocessor_description";
	public static final String RATING_ATTRIBUTE = "add_item_review_preprocessor_rating";
	
	private JdbcShopOrderDao shopOrderDao;
	private JdbcShopOrderItemDao shopOrderItemDao;
	
	public AddItemReviewPreprocessor() throws RequestPreprocessorException {
		try {
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
			shopOrderItemDao = JdbcDaoFactory.getShopOrderItemDao();
		} catch(DaoFactoryException e) {
			throw new RequestPreprocessorException("Dao initialization failed", e);
		}
	}
	
	@Override
	public boolean parseAndValidate(HttpServletRequest request) {
		
		try {
			int shopOrderItemId = (int) Integer.parseInt(request.getParameter(SHOP_ORDER_ITEM_ID_PARAM));
			String title = request.getParameter(TITLE_PARAM);
			String description = request.getParameter(DESCRIPTION_PARAM);
			Rating rating = new Rating((int) Integer.parseInt(request.getParameter(RATING_PARAM)));
			
			if(title == null || description == null) {
				return false;
			}
			if(title.isEmpty() || description.isEmpty()) {
				return false;
			}
			
			request.setAttribute(SHOP_ORDER_ITEM_ID_ATTRIBUTE, shopOrderItemId);
			request.setAttribute(TITLE_ATTRIBUTE, title);
			request.setAttribute(DESCRIPTION_ATTRIBUTE, description);
			request.setAttribute(RATING_ATTRIBUTE, rating);
			
			return true;
			
		} catch(NumberFormatException e) {
			//Some parameter was not valid
			return false;
		}
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AuthenticationInfo authenticationInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
		if(authenticationInfo == null) {
			throw new IllegalStateException("Authentication info must always be present");
		}
		
		if(authenticationInfo.isAuthenticated()) {
			AuthenticatedUserInfo authUserInfo = (AuthenticatedUserInfo) authenticationInfo;
			request.setAttribute(USER_ID_ATTRIBUTE, authUserInfo.getUserId());
			
			return true;
		} else {
			//User is not authenticated
			return false;
		}
	}

	@Override
	public boolean checkAuthorization(HttpServletRequest request) {
		
		//Checks if the order was placed by the current user
		int userId = (int) request.getAttribute(USER_ID_ATTRIBUTE);
		int shopOrderItemId = (int) request.getAttribute(SHOP_ORDER_ITEM_ID_ATTRIBUTE);
		
		try {
			ShopOrderItemEntity shopOrderItem = shopOrderItemDao.getById(shopOrderItemId);
			//Checks if the given shop order item exists
			if(shopOrderItem == null) {
				return false;
			}
			ShopOrderEntity shopOrder = shopOrderDao.getById(shopOrderItem.getShopOrderId());

			if(shopOrder.getUserId() == userId) {
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
