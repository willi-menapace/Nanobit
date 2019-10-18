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
import it.webapp.db.dao.jdbc.JdbcCartEntryDao;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.CartEntryAggregateInfo;
import it.webapp.db.entities.ItemEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.CartPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.CartView;
import it.webapp.view.ErrorView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shows user cart
 */
public class CartController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "Cart";
	
	private JdbcCartEntryDao cartEntryDao;
	
	public CartController() throws ApplicationControllerException {
		super(new CartPreprocessor());
		
		try {
			cartEntryDao = JdbcDaoFactory.getCartEntryEntityDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Cannot initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(CartPreprocessor.USER_ID_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				//Some items could have become not available so we obtain their info and remove them from the cart
				List<ItemEntity> notAvailableItems = cartEntryDao.getNoLongerAvailable(userId, transaction);
				cartEntryDao.deleteNoLongerAvailable(userId, transaction);
				
				//Obtains info about the items in the cart
				List<CartEntryAggregateInfo> cartInfo = cartEntryDao.getAggregateByUserId(userId, transaction);
				
				CartView.setRequestParameters(cartInfo, notAvailableItems, "", request);
				View cartView = ViewFactory.getView(CartView.class);
				cartView.view(request, response);
				
			} catch(DaoException e) {
				//An error occurred, we must undo the changes
				transaction.rollback();
				
				Logger.log("Failed to show cart to user", e);
				ErrorView errorView = ViewFactory.getView(ErrorView.class);

				//Sets the parameters and send the error
				ErrorView.setRequestParameters("Database error", "An internal server error caused the request to fail", request);
				errorView.view(request, response);
				
			}
		} catch(DaoFactoryException e) {
			Logger.log("Failed to open transaction", e);
		} catch(SQLException e) {
			Logger.log("Failed to close transaction", e);
		}
		
	}

	@Override
	public void processParseOfValidationFailure(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("Parameter parsing must never fail");
	}

	@Override
	public void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authentication failure", "User must be authenticated", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("Authorization check must never fail");
	}

}
