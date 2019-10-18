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
import it.webapp.db.dao.jdbc.JdbcShopReviewDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ShopReviewEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.ItemReviewPreprocessor;
import it.webapp.requestprocessor.ShopReviewPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ShopReviewView;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ShopReviewController extends ApplicationControllerSkeleton {
	
	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ShopReview";

	private JdbcShopReviewDao shopReviewDao;
	
	public ShopReviewController() throws ApplicationControllerException {
		super(new ShopReviewPreprocessor());
	
		try {
			shopReviewDao = JdbcDaoFactory.getShopReviewDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int shopId = (int) request.getAttribute(ShopReviewPreprocessor.SHOP_ID_ATTRIBUTE);
		int offset = (int) request.getAttribute(ShopReviewPreprocessor.OFFSET_ATTRIBUTE);
		int count = (int) request.getAttribute(ShopReviewPreprocessor.COUNT_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			List<ShopReviewEntity> shopReviews = null;
			
			try {
				shopReviews = shopReviewDao.getByShopId(shopId, offset, count, transaction);
				
			} catch(DaoException e) {
				Logger.log("Database could not satisfy shop review request", e);
				
				shopReviews = null; //An error occurred, throw results away
			}
			
			//Sends the notifications as a response
			ShopReviewView.setRequestParameters(shopReviews, request);
			ShopReviewView shopReviewView = ViewFactory.getView(ShopReviewView.class);
			shopReviewView.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}
}