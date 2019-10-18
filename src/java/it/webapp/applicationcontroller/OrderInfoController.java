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
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.OrderInfoPreprocessor;
import it.webapp.requestprocessor.RequestPreprocessorException;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.OrderInfoView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to view detailed order info
 */
public class OrderInfoController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "OrderInfo";
	
	private JdbcShopOrderDao shopOrderDao;
	
	public OrderInfoController() throws ApplicationControllerException, RequestPreprocessorException {
		super(new OrderInfoPreprocessor());
		
		try {
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int orderId = (int) request.getAttribute(OrderInfoPreprocessor.SHOP_ORDER_ID_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			View view;
			try {
				ShopOrderAggregateInfo shopOrderAggregate = shopOrderDao.getAggregateInfoById(orderId, transaction);
                                
                                int userId = ((AuthenticationInfo) request.getSession().getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE)).getUserId();
                                
				OrderInfoView.setRequestParameters(shopOrderAggregate, userId, request);
				view = ViewFactory.getView(OrderInfoView.class);
			
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not add a new issue due to database error", e);
				
				ErrorView.setRequestParameters("Internal server error", "Could not process the request due to a database error", request);
				view = ViewFactory.getView(ErrorView.class);
				
			}
			view.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}

}
