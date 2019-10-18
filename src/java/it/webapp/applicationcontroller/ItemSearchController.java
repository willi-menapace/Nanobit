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

import it.webapp.db.dao.ItemDao;
import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcItemDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.Department;
import it.webapp.db.entities.ItemAggregateInfo;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.ItemSearchPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.spatial.GeoFence;
import it.webapp.view.ItemSearchView;
import it.webapp.view.ViewFactory;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for the search of an item
 */
public class ItemSearchController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "ItemSearch";
	
	private JdbcItemDao itemDao;
	
	public ItemSearchController() throws ApplicationControllerException {
		super(new ItemSearchPreprocessor());
		
		try {
			 itemDao = JdbcDaoFactory.getItemDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String term = (String) request.getAttribute(ItemSearchPreprocessor.TERM_ATTRIBUTE);
		Department department = (Department) request.getAttribute(ItemSearchPreprocessor.DEPARTMENT_ATTRIBUTE);
		BigDecimal priceLow = (BigDecimal) request.getAttribute(ItemSearchPreprocessor.PRICE_LOW_ATTRIBUTE);
		BigDecimal priceHigh = (BigDecimal) request.getAttribute(ItemSearchPreprocessor.PRICE_HIGH_ATTRIBUTE);
		GeoFence geoFence = (GeoFence) request.getAttribute(ItemSearchPreprocessor.GEOFENCE_ATTRIBUTE);
		ItemDao.SortOrder sortOrder = (ItemDao.SortOrder) request.getAttribute(ItemSearchPreprocessor.ORDER_ATTRIBUTE);
		int offset = (int) request.getAttribute(ItemSearchPreprocessor.OFFSET_ATTRIBUTE);
		int count = (int) request.getAttribute(ItemSearchPreprocessor.COUNT_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			List<ItemAggregateInfo> itemInfo = null;
			try {
				itemInfo = itemDao.getFromAdvancedSearch(term, department, priceLow, priceHigh, geoFence, sortOrder, offset, count, transaction);
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not process search due to database error", e);
				
				itemInfo = null;
			}
			
			ItemSearchView.setRequestParameters(itemInfo, request);
			ItemSearchView itemSearchView = ViewFactory.getView(ItemSearchView.class);
			itemSearchView.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
	}
	
}
