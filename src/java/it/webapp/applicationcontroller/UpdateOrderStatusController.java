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
import it.webapp.db.dao.jdbc.JdbcNotificationDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderStatusDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderStatusEntity;
import it.webapp.db.entities.ShopOrderStatusNotification;
import it.webapp.db.entities.ShopOrderStatusType;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.RequestPreprocessorException;
import it.webapp.requestprocessor.UpdateOrderStatusPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.UpdateOrderStatusView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to update the status of a certain order
 */
public class UpdateOrderStatusController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UpdateOrderStatus";
	
	private JdbcShopOrderStatusDao shopOrderStatusDao;
	private JdbcNotificationDao notificationDao;
	private JdbcShopOrderDao shopOrderDao;
	
	public UpdateOrderStatusController() throws ApplicationControllerException, RequestPreprocessorException {
		super(new UpdateOrderStatusPreprocessor());
		
		try {
			shopOrderStatusDao = JdbcDaoFactory.getShopOrderStatusDao();
			notificationDao = JdbcDaoFactory.getNotificationDao();
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int shopOrderId = (int) request.getAttribute(UpdateOrderStatusPreprocessor.SHOP_ORDER_ID_ATTRIBUTE);
		ShopOrderStatusType shopOrderStatusType = (ShopOrderStatusType) request.getAttribute(UpdateOrderStatusPreprocessor.ORDER_STATUS_TYPE_ID_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			View view;
			try {
				
				ShopOrderStatusEntity shopOrderStatus = new ShopOrderStatusEntity(shopOrderId, shopOrderStatusType, new Date());
		
				shopOrderStatusDao.insert(shopOrderStatus, transaction);
		
				//Retrieves the shop order to obtain the user
				ShopOrderEntity shopOrder = shopOrderDao.getById(shopOrderId, transaction);
				
				ShopOrderStatusNotification statusNotification = new ShopOrderStatusNotification(null, new Date(), true, shopOrder.getUserId(), shopOrderId);
				notificationDao.insert(statusNotification, transaction);
				
				UpdateOrderStatusView.setRequestParameters(shopOrderId, "Stato aggiornato correttamente", request);
				view = ViewFactory.getView(UpdateOrderStatusView.class);
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not update order status due to database error", e);
				
				UpdateOrderStatusView.setRequestParameters(shopOrderId, "Database error", request);
				view = ViewFactory.getView(UpdateOrderStatusView.class);
			}
			view.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
	}

}
