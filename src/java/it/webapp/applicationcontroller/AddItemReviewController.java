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
import it.webapp.db.dao.jdbc.JdbcItemReviewDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ItemReviewEntity;
import it.webapp.db.entities.Rating;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.AddItemReviewPreprocessor;
import it.webapp.requestprocessor.RequestPreprocessorException;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.AddItemReviewView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for the insertion of a new review
 */
public class AddItemReviewController extends ApplicationControllerSkeleton {
	
	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddItemReview";
	
	private JdbcItemReviewDao itemReviewDao;
	
	public AddItemReviewController() throws ApplicationControllerException, RequestPreprocessorException {
		super(new AddItemReviewPreprocessor());
		
		try {
			itemReviewDao = JdbcDaoFactory.getItemReviewDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize Dao", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(AddItemReviewPreprocessor.USER_ID_ATTRIBUTE);
		int shopOrderItemId = (int) request.getAttribute(AddItemReviewPreprocessor.SHOP_ORDER_ITEM_ID_ATTRIBUTE);
		String title = (String) request.getAttribute(AddItemReviewPreprocessor.TITLE_ATTRIBUTE);
		String description = (String) request.getAttribute(AddItemReviewPreprocessor.DESCRIPTION_ATTRIBUTE);
		Rating rating = (Rating) request.getAttribute(AddItemReviewPreprocessor.RATING_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			View view;
			try {
				
				ItemReviewEntity itemReview = itemReviewDao.getByShopOrderItemId(shopOrderItemId, transaction);
				//A review can be inserted only if no other one has already been inserted for that item
				if(itemReview == null) {
					itemReview = new ItemReviewEntity(null, title, description, rating, new Date(), shopOrderItemId);
					itemReviewDao.insert(itemReview, transaction);
					
					AddItemReviewView.setRequestParameters(null, request);
					view = ViewFactory.getView(AddItemReviewView.class);
				} else {
					AddItemReviewView.setRequestParameters("Review already inserted", request);
					view = ViewFactory.getView(AddItemReviewView.class);
				}
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not add item review due to database error", e);
				
				AddItemReviewView.setRequestParameters("Database error", request);
				view = ViewFactory.getView(AddItemReviewView.class);
			}
			view.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}

}
