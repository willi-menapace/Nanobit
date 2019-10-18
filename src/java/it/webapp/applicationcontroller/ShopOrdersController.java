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
import it.webapp.requestprocessor.ShopOrdersPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ShopOrdersView;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to retrieve the orders placed by clients of a shop
 */
public class ShopOrdersController extends ApplicationControllerSkeleton {
	
	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ShopOrders";
	
	private JdbcShopOrderDao shopOrderDao;
	
	public ShopOrdersController() throws ApplicationControllerException {
		super(new ShopOrdersPreprocessor());
		
		try {
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not intialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int shopId = (int) request.getAttribute(ShopOrdersPreprocessor.SHOP_ID_ATTRIBUTE);
		int offset = (int) request.getAttribute(ShopOrdersPreprocessor.OFFSET_ATTRIBUTE);
		int count = (int) request.getAttribute(ShopOrdersPreprocessor.COUNT_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			List<ShopOrderAggregateInfo> shopOrderAggregateInfo = new ArrayList<>();
			
			try {
				
				//Retrieves the specified range of orders
				List<ShopOrderEntity> shopOrders = shopOrderDao.getByShopId(shopId, offset, count, transaction);
				
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
			ShopOrdersView.setRequestParameters(shopOrderAggregateInfo, request);
			ShopOrdersView userOrdersView = ViewFactory.getView(ShopOrdersView.class);
			userOrdersView.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}
	
}
