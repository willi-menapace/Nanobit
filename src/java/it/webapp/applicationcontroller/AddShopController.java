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
import it.webapp.db.dao.jdbc.JdbcResourceDao;
import it.webapp.db.dao.jdbc.JdbcShopDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.ShopEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.AddShopPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.AddShopSuccessfulView;
import it.webapp.view.AddShopView;
import it.webapp.view.ErrorView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddShopController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddShop";
	
	JdbcShopDao shopDao;
	JdbcResourceDao resourceDao;
	
	public AddShopController() throws ApplicationControllerException {
		super(new AddShopPreprocessor());
		
		try {
			shopDao = JdbcDaoFactory.getShopDao();
			resourceDao = JdbcDaoFactory.getResourceDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Cannot initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int shopId = (int) request.getAttribute(AddShopPreprocessor.SHOP_ID_ATTRIBUTE);
		String description = (String) request.getAttribute(AddShopPreprocessor.DESCRIPTION_ATTRIBUTE);
		String vat = (String) request.getAttribute(AddShopPreprocessor.VAT_ATTRIBUTE);
		String phone = (String) request.getAttribute(AddShopPreprocessor.PHONE_ATTRIBUTE);
		List<ResourceEntity> images = (List<ResourceEntity>) request.getAttribute(AddShopPreprocessor.IMAGES_ATTRIBUTE);
		
		//Id must be set manually as Shop it is a subclass of User
		ShopEntity shop = new ShopEntity(shopId, description, vat, phone, new Date());
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			View view = ViewFactory.getView(AddShopSuccessfulView.class); //If no errors arise we will send a success response
			try {
				shopDao.insert(shop, transaction);

				for(ResourceEntity currentImage : images) {
					resourceDao.insertShopImage(currentImage, shopId, transaction);
				}
				
				
			} catch(DaoException e) {
				//In case of error aborts the operation
				transaction.rollback();
				
				//Changes the view to use to the original one displaying an error message
				AddShopView.setRequestParameters("Could not add shop due to internal server error", request);
				view = ViewFactory.getView(AddShopView.class);
			}
			
            // Se è stato aggiunto con successo lo shop, aggiorno le informazioni di autenticazione in maniera tale che l'utente sia uno 'shop'
            AuthenticationInfo oldAuthenticationInfo = (AuthenticationInfo) request.getSession().getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
            AuthenticationInfo newAuthenticationInfo = new AuthenticatedUserInfo(oldAuthenticationInfo.getUserId(), oldAuthenticationInfo.getUsername(), true, oldAuthenticationInfo.isAdmin());
            request.getSession().setAttribute(AuthenticationInfo.SESSION_ATTRIBUTE, newAuthenticationInfo);
            
			view.view(request, response);
			
		} catch(DaoFactoryException | SQLException e) {
			Logger.log("Failed to add Shop", e);
			
			ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
			//Sets the parameters and send the error
			ErrorView.setRequestParameters("Database error", "The database could not process the request", request);
			errorView.view(request, response);
		}
	}

	@Override
	public void processParseOfValidationFailure(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Bad Request", "The request parameters could not be processed correctly", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authentication Failure", "User must be authenticated", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authorization Failure", "The User is already a Shop", request);
		errorView.view(request, response);
	}

}
