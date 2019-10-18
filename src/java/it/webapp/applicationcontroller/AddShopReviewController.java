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
import it.webapp.db.dao.jdbc.JdbcShopReviewDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.Rating;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopReviewEntity;
import it.webapp.db.entities.ShopReviewNotification;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.AddShopReviewPreprocessor;
import it.webapp.requestprocessor.RequestPreprocessorException;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.AddShopReviewView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to add a shop review
 */
public class AddShopReviewController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddShopReview";
	
	private JdbcShopReviewDao shopReviewDao;
	private JdbcShopOrderDao shopOrderDao;
	private JdbcNotificationDao notificationDao;
	
	public AddShopReviewController() throws ApplicationControllerException, RequestPreprocessorException {
		super(new AddShopReviewPreprocessor());
		
		try {
			shopReviewDao = JdbcDaoFactory.getShopReviewDao();
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
			notificationDao = JdbcDaoFactory.getNotificationDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize Dao", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(AddShopReviewPreprocessor.USER_ID_ATTRIBUTE);
		int shopOrderId = (int) request.getAttribute(AddShopReviewPreprocessor.SHOP_ORDER_ID_ATTRIBUTE);
		String title = (String) request.getAttribute(AddShopReviewPreprocessor.TITLE_ATTRIBUTE);
		String description = (String) request.getAttribute(AddShopReviewPreprocessor.DESCRIPTION_ATTRIBUTE);
		Rating rating = (Rating) request.getAttribute(AddShopReviewPreprocessor.RATING_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			View view;
			try {
				
				ShopReviewEntity shopReview = shopReviewDao.getByShopOrderId(shopOrderId, transaction);
				ShopOrderEntity shopOrder = shopOrderDao.getById(shopOrderId, transaction);
				
				//A review can be inserted only if no other one has already been inserted for that shop order
				if(shopReview == null) {
					shopReview = new ShopReviewEntity(null, title, description, rating, new Date(), shopOrderId);
					int reviewId = shopReviewDao.insert(shopReview, transaction);
					
					//Notifies the shop for the new review
					ShopReviewNotification shopReviewNotification = new ShopReviewNotification(null, new Date(), true, shopOrder.getShopId(), reviewId);
					notificationDao.insert(shopReviewNotification, transaction);
					
					AddShopReviewView.setRequestParameters(null, request);
					view = ViewFactory.getView(AddShopReviewView.class);
					
				} else {
					AddShopReviewView.setRequestParameters("Review already inserted", request);
					view = ViewFactory.getView(AddShopReviewView.class);
				}
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not add shop review due to database error", e);
				
				AddShopReviewView.setRequestParameters("Database error", request);
				view = ViewFactory.getView(AddShopReviewView.class);
			}
			view.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
	}

}
