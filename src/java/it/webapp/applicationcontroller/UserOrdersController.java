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

import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcShopOrderDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ShopOrderAggregateInfo;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.UserOrdersPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.UserOrdersView;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UserOrdersController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UserOrders";
	
	private JdbcShopOrderDao shopOrderDao;
	
	public UserOrdersController() throws ApplicationControllerException {
		super(new UserOrdersPreprocessor());
		
		try {
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not intialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(UserOrdersPreprocessor.USER_ID_ATTRIBUTE);
		int offset = (int) request.getAttribute(UserOrdersPreprocessor.OFFSET_ATTRIBUTE);
		int count = (int) request.getAttribute(UserOrdersPreprocessor.COUNT_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			List<ShopOrderAggregateInfo> shopOrderAggregateInfo = new ArrayList<>();
			
			try {
				
				//Retrieves the specified range of orders
				List<ShopOrderEntity> shopOrders = shopOrderDao.getByUserId(userId, offset, count, transaction);
				
				//Retrieves extended info for every shop order
				for(ShopOrderEntity currentShopOrder : shopOrders) {
					ShopOrderAggregateInfo currentAggregate = shopOrderDao.getAggregateInfoById(currentShopOrder.getShopOrderId(), transaction);
					shopOrderAggregateInfo.add(currentAggregate);
				}

				
			} catch(DaoException e) {
				Logger.log("Database could not satisfy notification request", e);
				
				shopOrderAggregateInfo = null; //An error occurred, throw results away
			}
			
			//Sends the notifications as a response
			UserOrdersView.setRequestParameters(shopOrderAggregateInfo, request);
			UserOrdersView userOrdersView = ViewFactory.getView(UserOrdersView.class);
			userOrdersView.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}

}
