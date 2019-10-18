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
import it.webapp.db.dao.jdbc.JdbcAddressDao;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcResourceDao;
import it.webapp.db.dao.jdbc.JdbcShopDao;
import it.webapp.db.dao.jdbc.JdbcShopReviewDao;
import it.webapp.db.dao.jdbc.JdbcUserDao;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.ShopEntity;
import it.webapp.db.entities.ShopReviewAggregateInfo;
import it.webapp.db.entities.UserEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.ShopPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.ShopView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Elaborates a request for information about a shop
 */
public class ShopController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "Shop";
	
	private JdbcShopDao shopDao;
	private JdbcAddressDao addressDao;
	private JdbcResourceDao resourceDao;
	private JdbcShopReviewDao shopReviewDao;
    private JdbcUserDao shopUserDao;
	
	public ShopController() throws ApplicationControllerException {
		super(new ShopPreprocessor());
		
		try {
			shopDao = JdbcDaoFactory.getShopDao();
			addressDao = JdbcDaoFactory.getAddressDao();
			resourceDao = JdbcDaoFactory.getResourceDao();
			shopReviewDao = JdbcDaoFactory.getShopReviewDao();
            shopUserDao = JdbcDaoFactory.getUserDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Cannot initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int shopId = (int) request.getAttribute(ShopPreprocessor.SHOP_ID_ATTRIBUTE);
		
		try {
			ShopEntity shop = shopDao.getById(shopId);
			AddressEntity address = addressDao.getByUserId(shopId);
			List<ResourceEntity> images = resourceDao.getByShopId(shopId);
			ShopReviewAggregateInfo reviewInfo = shopReviewDao.getAverageRating(shopId);
            UserEntity shopUser = shopUserDao.getById(shopId);

			ShopView.setRequestParameters(shop, address, images, reviewInfo, shopUser, request);
			View view = ViewFactory.getView(ShopView.class);
			view.view(request, response);
			
		} catch(DaoException e) {
			Logger.log("Failed to get Shop data", e);
			
			ErrorView.setRequestParameters("Internal server error", "The database encountered a problem", request);
			View errorView = ViewFactory.getView(ErrorView.class);
			errorView.view(request, response);
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
		throw new UnsupportedOperationException("Authentication check must never fail");
	}

	@Override
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("Authorization check must never fail");
	}
	
}
