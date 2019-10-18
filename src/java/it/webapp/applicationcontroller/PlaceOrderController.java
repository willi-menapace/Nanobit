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
import it.webapp.db.dao.jdbc.JdbcNotificationDao;
import it.webapp.db.dao.jdbc.JdbcShopItemDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderItemDao;
import it.webapp.db.dao.jdbc.JdbcShopShipmentTypeDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.CartEntryAggregateInfo;
import it.webapp.db.entities.CartEntryEntity;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ShipmentType;
import it.webapp.db.entities.ShopItemEntity;
import it.webapp.db.entities.ShopOrderAddedNotification;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import it.webapp.db.entities.ShopShipmentTypeEntity;
import it.webapp.logging.Logger;
import it.webapp.payment.CreditCard;
import it.webapp.payment.PaymentGateway;
import it.webapp.requestprocessor.PlaceOrderPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.CartView;
import it.webapp.view.ErrorView;
import it.webapp.view.PlaceOrderSuccessfulView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to place an order
 */
public class PlaceOrderController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "PlaceOrder";
	
	private JdbcShopShipmentTypeDao shopShipmentTypeDao;
	private JdbcCartEntryDao cartEntryDao;
	private JdbcShopItemDao shopItemDao;
	private JdbcShopOrderDao shopOrderDao;
	private JdbcShopOrderItemDao shopOrderItemDao;
	private JdbcNotificationDao notificationDao;
    private JdbcAddressDao addressDao;
	
	public PlaceOrderController() throws ApplicationControllerException {
		super(new PlaceOrderPreprocessor());
		
		try {
			shopShipmentTypeDao = JdbcDaoFactory.getShopShipmentTypeDao();
			cartEntryDao = JdbcDaoFactory.getCartEntryEntityDao();
			shopItemDao = JdbcDaoFactory.getShopItemDao();
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
			shopOrderItemDao = JdbcDaoFactory.getShopOrderItemDao();
			notificationDao = JdbcDaoFactory.getNotificationDao();
            addressDao = JdbcDaoFactory.getAddressDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	/**
	 * Calculates the total price to pay
	 * 
	 * @param cartInfo Aggregate information about the ordered items
	 * @param shopShipmentTypeMap Information about the shipment types chosen for each shop
	 * @return Total price to pay
	 */
	private BigDecimal computeTotalPrice( List<CartEntryAggregateInfo> cartInfo, Map<Integer, ShipmentType> shopShipmentTypeMap) throws DaoException {
		BigDecimal result = new BigDecimal(0);
		
		for(CartEntryAggregateInfo currentAggregateEntry : cartInfo) {
			CartEntryEntity currentEntry = currentAggregateEntry.getCartEntry();
			ShopItemEntity currentShopItemEntry = currentAggregateEntry.getShopItem();

			//Multiplies item price by ordered quantity and adds to total
			BigDecimal entryTotal =  currentShopItemEntry.getPrice().multiply(new BigDecimal(currentEntry.getQuantity()));
			result.add(entryTotal);
		}
		
		//Adds the cost of the shipment
		for(Entry<Integer, ShipmentType> currentEntry : shopShipmentTypeMap.entrySet()) {
			int shopId = currentEntry.getKey();
			ShipmentType shipmentType = currentEntry.getValue();
			ShopShipmentTypeEntity shopShipmentTypeEntity = shopShipmentTypeDao.getByShopAndShipmentType(shopId, shipmentType);
			result.add(shopShipmentTypeEntity.getPrice());
		}
		
		return result;
	}
	
	/**
	 * Places an order
	 * 
	 * @param cartInfo Aggregate information about the ordered items
	 * @param shopShipmentTypeMap Information about the shipment types chosen for each shop
	 * @param transaction The transaction to use
	 * @return Total price to pay
	 */
	private void placeOrder(List<CartEntryAggregateInfo> cartInfo, Map<Integer, ShipmentType> shopShipmentTypeMap, AddressEntity shipmentAddress, int userId, Transaction transaction) throws DaoException {
		
		Map<Integer, ShopOrderEntity> shopShopOrderMap = new HashMap<>(); //Maps from shopIds to shop orders
		
        // Insert the AddressEntity to the DB
        int shipmentAddressId = addressDao.insert(shipmentAddress, transaction);
        
		for(CartEntryAggregateInfo currentAggregateEntry : cartInfo) {
			CartEntryEntity currentEntry = currentAggregateEntry.getCartEntry();
			ShopItemEntity currentShopItemEntry = currentAggregateEntry.getShopItem();
			ItemEntity currentItemEntry = currentAggregateEntry.getItem();

			int shopId = currentShopItemEntry.getShopId();
			
			ShopOrderEntity shopOrderEntity;
			//If the ShopOrder is not in the db then create a new one and insert in the db
			if(shopShopOrderMap.containsKey(shopId) == false) {
				ShopShipmentTypeEntity shopShipmentTypeEntity = shopShipmentTypeDao.getByShopAndShipmentType(shopId, shopShipmentTypeMap.get(shopId)); //Gets the selected shipment type
				ShopOrderEntity newShopOrder = new ShopOrderEntity(null, new Date(), userId, shopId, shipmentAddressId, shopShipmentTypeEntity.getShipmentType(), shopShipmentTypeEntity.getPrice());
				int newShopOrderId = shopOrderDao.insert(newShopOrder, transaction);
				newShopOrder.setShopOrderId(newShopOrderId);
				shopShopOrderMap.put(shopId, newShopOrder);
				
				//Notifies the shop of the new order
				ShopOrderAddedNotification orderNotification = new ShopOrderAddedNotification(null, new Date(), true, shopId, newShopOrderId);
				notificationDao.insert(orderNotification, transaction);
			}
			shopOrderEntity = shopShopOrderMap.get(shopId);
			
			//Create a new shop order item, insert it in the db and delete corresponding cart entry
			ShopOrderItemEntity newShopOrderItem = new ShopOrderItemEntity(null, shopOrderEntity.getShopOrderId(), currentItemEntry.getItemId(), currentShopItemEntry.getPrice(), currentEntry.getQuantity());
			shopOrderItemDao.insert(newShopOrderItem, transaction);
			cartEntryDao.delete(currentEntry.getCartEntryId(), transaction);
			
			//Removes the ordered items from the stock of a shop
			currentShopItemEntry.setAvailability(currentShopItemEntry.getAvailability() - currentEntry.getQuantity());
			shopItemDao.update(currentShopItemEntry, transaction);
		}

		
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(PlaceOrderPreprocessor.USER_ID_ATTRIBUTE);
		AddressEntity shipmentAddress = (AddressEntity) request.getAttribute(PlaceOrderPreprocessor.SHIPMENT_ADDRESS_ATTRIBUTE);
		Map<Integer, ShipmentType> shopShipmentTypeMap = (Map<Integer, ShipmentType>) request.getAttribute(PlaceOrderPreprocessor.SHOP_SHIPMENT_TYPE_MAP_ATTRIBUTE);
		CreditCard creditCard = (CreditCard) request.getAttribute(PlaceOrderPreprocessor.CREDIT_CARD_ATTRIBUTE);
		
		//Transaction ensures these operations are executed atomically
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			View view;
			try {
				//Some items could have become not available so we obtain their info and remove them from the cart
				List<ItemEntity> notAvailableItems = cartEntryDao.getNoLongerAvailable(userId, transaction);
				cartEntryDao.deleteNoLongerAvailable(userId, transaction);
				
				//Obtains info about the items in the cart
				List<CartEntryAggregateInfo> cartInfo = cartEntryDao.getAggregateByUserId(userId, transaction);
				
				//All items are available
				if(notAvailableItems.isEmpty()) {
					
					BigDecimal totalPrice = computeTotalPrice(cartInfo, shopShipmentTypeMap);
					boolean success = PaymentGateway.pay(creditCard, totalPrice);
					
					if(success) {
						placeOrder(cartInfo, shopShipmentTypeMap, shipmentAddress, userId, transaction);
						
						view = ViewFactory.getView(PlaceOrderSuccessfulView.class);
						
					} else {
						transaction.rollback();
						
						CartView.setRequestParameters(cartInfo, notAvailableItems, "Could not complete order, specified credit card was rejected", request);
						view = ViewFactory.getView(CartView.class);
					}
					
				//Some items became unavailable
				} else {
					transaction.rollback();
					
					CartView.setRequestParameters(cartInfo, notAvailableItems, "Could not complete order, some items became unavailable", request);
					view = ViewFactory.getView(CartView.class);
				}

				
			} catch(DaoException e) {
				//An error occurred, we must undo the changes
				transaction.rollback();
				
				Logger.log("Failed to show order options to user", e);
				view = ViewFactory.getView(ErrorView.class);

				//Sets the parameters and send the error
				ErrorView.setRequestParameters("Database error", "An internal server error caused the request to fail", request);
			}
			
			view.view(request, response);
		} catch(DaoFactoryException e) {
			Logger.log("Failed to open transaction", e);
		} catch(SQLException e) {
			Logger.log("Failed to close transaction", e);
		}
		
	}

}
