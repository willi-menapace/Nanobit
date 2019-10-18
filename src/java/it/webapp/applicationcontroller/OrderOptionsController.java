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
import it.webapp.db.dao.jdbc.JdbcCartEntryDao;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcShopShipmentTypeDao;
import it.webapp.db.dao.jdbc.JdbcUserDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.CartEntryAggregateInfo;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ShopEntity;
import it.webapp.db.entities.ShopShipmentTypeEntity;
import it.webapp.db.entities.UserEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.OrderOptionsPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.OrderOptionsView;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for a form to insert the necessary information to complete an order
 */
public class OrderOptionsController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "OrderOptions";
	
	private JdbcCartEntryDao cartEntryDao;
	private JdbcAddressDao addressDao;
	private JdbcShopShipmentTypeDao shopShipmentTypeDao;
    private JdbcUserDao userDao;
	
	public OrderOptionsController() throws ApplicationControllerException {
		super(new OrderOptionsPreprocessor());
		
		try {
            userDao = JdbcDaoFactory.getUserDao();
			cartEntryDao = JdbcDaoFactory.getCartEntryEntityDao();
			addressDao = JdbcDaoFactory.getAddressDao();
			shopShipmentTypeDao = JdbcDaoFactory.getShopShipmentTypeDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
	
		int userId = (int) request.getAttribute(OrderOptionsPreprocessor.USER_ID_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				//Some items could have become not available so we obtain their info and remove them from the cart
				List<ItemEntity> notAvailableItems = cartEntryDao.getNoLongerAvailable(userId, transaction);
				cartEntryDao.deleteNoLongerAvailable(userId, transaction);
				
				//Obtains info about the items in the cart
				List<CartEntryAggregateInfo> cartInfo = cartEntryDao.getAggregateByUserId(userId, transaction);
				
				AddressEntity userAddress = addressDao.getByUserId(userId, transaction);
                
                // Containes for every shop the cart items associated with It
                Map<Integer, List<CartEntryAggregateInfo>> shopCartAggregateInfoMap = new HashMap<>();
				
                //For every shop populate the map with the cart entries associated with it
				for(CartEntryAggregateInfo currentAggregate : cartInfo) {
					ShopEntity currentShop = currentAggregate.getShop();
					List<CartEntryAggregateInfo> shopCartAggregateInfoEntries;
                    
					if(shopCartAggregateInfoMap.containsKey(currentShop.getShopId()) == false) {
						shopCartAggregateInfoEntries = new ArrayList<>();
					}else{
                        shopCartAggregateInfoEntries = shopCartAggregateInfoMap.get(currentShop.getShopId());
                    }
                    
                    shopCartAggregateInfoEntries.add(currentAggregate);
                    shopCartAggregateInfoMap.put(currentShop.getShopId(), shopCartAggregateInfoEntries);
				}
                
				Map<Integer, List<ShopShipmentTypeEntity>> shopShipmentTypesMap = new HashMap<>();
				//For every shop populate the map with the possible shipment types
				for(CartEntryAggregateInfo currentAggregate : cartInfo) {
					ShopEntity currentShop = currentAggregate.getShop();
					
					if(shopShipmentTypesMap.containsKey(currentShop.getShopId()) == false) {
						List<ShopShipmentTypeEntity> shipmentTypes = shopShipmentTypeDao.getByShopId(currentShop.getShopId(), transaction);
						shopShipmentTypesMap.put(currentShop.getShopId(), shipmentTypes);
					}
				}
                
                // Containes for every shop the user entiy associated with It
                Map<Integer, UserEntity> shopUsersMap = new HashMap<>();
                
                //For every shop populate the map with the user entity entries associated with it
                for(CartEntryAggregateInfo currentAggregate : cartInfo) {
                    ShopEntity currentShop = currentAggregate.getShop();
                    
                    if(shopUsersMap.containsKey(currentShop.getShopId()) == false) {
						UserEntity shopUser = userDao.getById(currentShop.getShopId(), transaction);
						shopUsersMap.put(currentShop.getShopId(), shopUser);
					}
                }
                
				OrderOptionsView.setRequestParameters(userAddress, shopCartAggregateInfoMap, shopShipmentTypesMap, notAvailableItems, shopUsersMap, request);
				OrderOptionsView orderOptionsView = ViewFactory.getView(OrderOptionsView.class);
				orderOptionsView.view(request, response);
				
			} catch(DaoException e) {
				//An error occurred, we must undo the changes
				transaction.rollback();
				
				Logger.log("Failed to show order options to user", e);
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

}
