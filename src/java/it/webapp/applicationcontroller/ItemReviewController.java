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
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.ItemReviewPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ItemReviewView;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ItemReviewController extends ApplicationControllerSkeleton {
	
	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ItemReview";

	private JdbcItemReviewDao itemReviewDao;
	
	public ItemReviewController() throws ApplicationControllerException {
		super(new ItemReviewPreprocessor());
	
		try {
			itemReviewDao = JdbcDaoFactory.getItemReviewDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int itemId = (int) request.getAttribute(ItemReviewPreprocessor.ITEM_ID_ATTRIBUTE);
		int offset = (int) request.getAttribute(ItemReviewPreprocessor.OFFSET_ATTRIBUTE);
		int count = (int) request.getAttribute(ItemReviewPreprocessor.COUNT_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			List<ItemReviewEntity> itemReviews = null;
			
			try {
				itemReviews = itemReviewDao.getByItemId(itemId, offset, count, transaction);
				
			} catch(DaoException e) {
				Logger.log("Database could not satisfy item review request", e);
				
				itemReviews = null; //An error occurred, throw results away
			}
			
			//Sends the notifications as a response
			ItemReviewView.setRequestParameters(itemReviews, request);
			ItemReviewView itemReviewView = ViewFactory.getView(ItemReviewView.class);
			itemReviewView.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}
}
