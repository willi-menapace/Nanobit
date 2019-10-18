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
import it.webapp.db.entities.CartEntryEntity;
import it.webapp.db.entities.ItemEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.CartPreprocessor;
import it.webapp.requestprocessor.UpdateCartItemQuantityPreprocessor;
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
 * Updates the quantity of a certain item in the cart of the user
 */
public class UpdateCartItemQuantityController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UpdateCartItemQuantity";
	
	private JdbcCartEntryDao cartEntryDao;
	
	public UpdateCartItemQuantityController() throws ApplicationControllerException {
		super(new UpdateCartItemQuantityPreprocessor());
		
		try {
			cartEntryDao = JdbcDaoFactory.getCartEntryEntityDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Cannot initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(UpdateCartItemQuantityPreprocessor.USER_ID_ATTRIBUTE);
		int shopItemId = (int) request.getAttribute(UpdateCartItemQuantityPreprocessor.SHOP_ITEM_ID_ATTRIBUTE);
		int quantity = (int) request.getAttribute(UpdateCartItemQuantityPreprocessor.QUANTITY_ATTRIBUTE);
		
		//We must take multiple actions so a transaction is necessary
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			try {
				CartEntryEntity cartEntry = cartEntryDao.getByUserShopItemId(userId, shopItemId);
				//If the cart entry is not present create a new one
				if(cartEntry == null) {
					cartEntry = new CartEntryEntity(null, userId, shopItemId, quantity);
					cartEntryDao.insert(cartEntry, transaction);
				//Otherwise if the quantity is 0 we must delete the existing one
				} else if(quantity == 0) {
					cartEntryDao.delete(cartEntry.getCartEntryId(), transaction);
				//Otherwise update the existing one
				} else {
					cartEntry.setQuantity(quantity);
					cartEntryDao.update(cartEntry, transaction);
				}
				
				//Some items could have become not available so we obtain their info and remove them from the cart
				List<ItemEntity> notAvailableItems = cartEntryDao.getNoLongerAvailable(userId, transaction);
				cartEntryDao.deleteNoLongerAvailable(userId, transaction);
				
				//Obtains info about the items in the cart
				List<CartEntryAggregateInfo> cartInfo = cartEntryDao.getAggregateByUserId(userId, transaction);
				
				CartView.setRequestParameters(cartInfo, notAvailableItems, "", request);
				View cartView = ViewFactory.getView(CartView.class);
				cartView.view(request, response);
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Db could not propcess cart entry update request", e);
				
				ErrorView.setRequestParameters("Update failed", "Internal database error", request);
				ErrorView errorView = ViewFactory.getView(ErrorView.class);		
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
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Bad Parameters", "Request parameters were not set correctly", request);
		errorView.view(request, response);
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
