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
import it.webapp.db.dao.jdbc.JdbcIssueDao;
import it.webapp.db.dao.jdbc.JdbcNotificationDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderItemDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.AdminIssueOpenedNotificationEntity;
import it.webapp.db.entities.IssueEntity;
import it.webapp.db.entities.ShopIssueOpenedNotificationEntity;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.AddIssuePreprocessor;
import it.webapp.requestprocessor.RequestPreprocessorException;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.UserIssueBaseView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to open a new issue
 */
public class AddIssueController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddIssue";
	
	private JdbcIssueDao issueDao;
	private JdbcNotificationDao notificationDao;
	private JdbcShopOrderItemDao shopOrderItemDao;
	private JdbcShopOrderDao shopOrderDao;
	
	public AddIssueController() throws ApplicationControllerException, RequestPreprocessorException {
		super(new AddIssuePreprocessor());
		
		try {
			issueDao = JdbcDaoFactory.getIssueDao();
			notificationDao = JdbcDaoFactory.getNotificationDao();
			shopOrderItemDao = JdbcDaoFactory.getShopOrderItemDao();
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(AddIssuePreprocessor.USER_ID_ATTRIBUTE);
		int shopOrderItemId = (int) request.getAttribute(AddIssuePreprocessor.SHOP_ORDER_ITEM_ID_ATTRIBUTE);
		String description = (String) request.getAttribute(AddIssuePreprocessor.DESCRIPTION_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			View view;
			try {
				
				//Creates an open issue
				IssueEntity issue = new IssueEntity(description, shopOrderItemId);

				int issueId = issueDao.insert(issue, transaction);
				
				ShopOrderItemEntity shopOrderItem = shopOrderItemDao.getById(shopOrderItemId, transaction);
				ShopOrderEntity shopOrder = shopOrderDao.getById(shopOrderItem.getShopOrderId(), transaction);
				
				AdminIssueOpenedNotificationEntity adminNotification = new AdminIssueOpenedNotificationEntity(null, new Date(), true, issueId);
				ShopIssueOpenedNotificationEntity shopNotification = new ShopIssueOpenedNotificationEntity(null, new Date(), true, shopOrder.getShopId(), issueId);
				notificationDao.insert(adminNotification, transaction);
				notificationDao.insert(shopNotification, transaction);
				
				UserIssueBaseView.setRequestParameters("Issue opened successfully", request);
				view = ViewFactory.getView(UserIssueBaseView.class);
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not add a new issue due to database error", e);
				
				UserIssueBaseView.setRequestParameters("Cannot open issue due to database error", request);
				view = ViewFactory.getView(UserIssueBaseView.class);
			}
			
			view.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
	}

}
