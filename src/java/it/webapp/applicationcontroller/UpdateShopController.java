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
import it.webapp.db.dao.jdbc.JdbcShopDao;
import it.webapp.db.entities.ShopEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.UpdateShopPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.UpdateShopView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Updates information about a shop
 */
public class UpdateShopController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UpdateShop";
	
	private JdbcShopDao shopDao;
	
	public UpdateShopController() throws ApplicationControllerException {
		super(new UpdateShopPreprocessor());
		
		try {
			shopDao = JdbcDaoFactory.getShopDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Cannot initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int shopId = (int) request.getAttribute(UpdateShopPreprocessor.SHOP_ID_ATTRIBUTE);
		String description = (String) request.getAttribute(UpdateShopPreprocessor.DESCRIPTION_ATTRIBUTE);
		String phone = (String) request.getAttribute(UpdateShopPreprocessor.PHONE_ATTRIBUTE);
		
		try {
			ShopEntity shop = shopDao.getById(shopId);

			//Shop to update must exist
			if(shop == null) {
				ErrorView errorView = ViewFactory.getView(ErrorView.class);

				//Sets the parameters and send the error
				ErrorView.setRequestParameters("Bad Request", "Your Shop account does not exist anymore", request);
				errorView.view(request, response);
				return;
			}

			//Makes requested changes to the db
			shop.setDescription(description);
			shop.setPhoneNumber(phone);

			shopDao.update(shop);

			UpdateShopView.setRequestParameters("Dati aggiornati correttamente", request);
			UpdateShopView view = ViewFactory.getView(UpdateShopView.class);
			view.view(request, response);
		} catch(DaoException e) {
			Logger.log("Failed to update Shop", e);
			
			UpdateShopView.setRequestParameters("The DB encountered a problem, please retry later", request);
			View updateShopView = ViewFactory.getView(UpdateShopView.class);
			updateShopView.view(request, response);
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
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authorization failure", "User must be a Shop", request);
		errorView.view(request, response);
	}

}
