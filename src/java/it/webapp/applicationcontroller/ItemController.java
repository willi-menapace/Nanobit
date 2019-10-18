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
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ItemAggregateInfo;
import it.webapp.db.entities.ItemShopAvailabilityInfo;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.ItemPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.ItemView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for information about an item
 */
public class ItemController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "Item";
	
	private JdbcItemDao itemDao;
	private JdbcResourceDao resourceDao;
	
	public ItemController() throws ApplicationControllerException {
		super(new ItemPreprocessor());
		
		try {
			itemDao = JdbcDaoFactory.getItemDao();
			resourceDao = JdbcDaoFactory.getResourceDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int itemId = (int) request.getAttribute(ItemPreprocessor.ITEM_ID_ATTRIBUTE);
		
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			View view;
			try {
				//Retrieves the needed information
				ItemAggregateInfo itemInfo = itemDao.getAggregateInfoById(itemId, transaction);
				List<ResourceEntity> images = resourceDao.getByItemId(itemId, transaction);
				List<ItemShopAvailabilityInfo> availabilityInfo = itemDao.getShopAvailabilityInfoByItemId(itemId, transaction);
				
				//Checks if the item exists
				if(itemInfo == null) {
					ErrorView.setRequestParameters("Could not obtain item", "Item does not exist", request);
					view = ViewFactory.getView(ErrorView.class);
				} else {
					//Sorts shop list according to price
					Collections.sort(availabilityInfo, new Comparator<ItemShopAvailabilityInfo>() {
						@Override
						public int compare(ItemShopAvailabilityInfo first, ItemShopAvailabilityInfo second) {
							double firstPrice = first.getShopItemEntity().getPrice().doubleValue();
							double secondPrice = second.getShopItemEntity().getPrice().doubleValue();
							
							if(firstPrice < secondPrice) {
								return -1;
							} else if(firstPrice > secondPrice) {
								return +1;
							} else {
								return 0;
							}
						}
					});
					
					ItemView.setRequestParameters(itemInfo, images, availabilityInfo, request);
					view = ViewFactory.getView(ItemView.class);
				}
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not get item information due to database error", e);
				
				ErrorView.setRequestParameters("Internal server error", "Database generated an error", request);
				view = ViewFactory.getView(ErrorView.class);
			}
			
			view.view(request, response);
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}
	
}
