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
import it.webapp.db.dao.jdbc.JdbcItemDao;
import it.webapp.db.dao.jdbc.JdbcResourceDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderItemDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.AddIssueFormPreprocessor;
import it.webapp.requestprocessor.RequestPreprocessorException;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.AddIssueFormView;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Elaborats the request for an add issue form
 */
public class AddIssueFormController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddIssueForm";
	
	private JdbcShopOrderDao shopOrderDao;
	private JdbcShopOrderItemDao shopOrderItemDao;
	private JdbcItemDao itemDao;
	private JdbcResourceDao resourceDao;
	
	public AddIssueFormController() throws ApplicationControllerException, RequestPreprocessorException {
		super(new AddIssueFormPreprocessor());
		
		try {
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
			shopOrderItemDao = JdbcDaoFactory.getShopOrderItemDao();
			itemDao = JdbcDaoFactory.getItemDao();
			resourceDao = JdbcDaoFactory.getResourceDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Cannot initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(AddIssueFormPreprocessor.USER_ID_ATTRIBUTE);
		int shopOrderItemId = (int) request.getAttribute(AddIssueFormPreprocessor.SHOP_ORDER_ITEM_ID_PARAM);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			try {
				ShopOrderItemEntity shopOrderItem = shopOrderItemDao.getById(shopOrderItemId, transaction);
				ShopOrderEntity shopOrder = shopOrderDao.getById(shopOrderItem.getShopOrderId(), transaction);
				ItemEntity item = itemDao.getById(shopOrderItem.getItemId(), transaction);
				List<ResourceEntity> images = resourceDao.getByItemId(item.getItemId(), transaction);
				
				//Gets an image if it exists
				ResourceEntity image = null;
				if(images.isEmpty() == false) {
					image = images.get(0);
				}
				
				//Calls the view witout passing the optional message
				AddIssueFormView.setRequestParameters(shopOrder, shopOrderItem, item, image, request);
				AddIssueFormView issueFormView = ViewFactory.getView(AddIssueFormView.class);
				issueFormView.view(request, response);
				
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("An issue form request could not be processed", e);
				
				ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
				//Sets the parameters and send the error
				ErrorView.setRequestParameters("Internal server error", "Database could not process request", request);
				errorView.view(request, response);
			}
			
			
		} catch(DaoFactoryException e) {
			Logger.log("Could not open transaction", e);
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		}
		
		
	}

}
