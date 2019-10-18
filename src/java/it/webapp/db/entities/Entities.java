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

package it.webapp.db.entities;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Entity utility methods
 */
public class Entities {
	
	private Entities() {
		//Not instantiable
	}
	
	/**
	 * Normalizes the prefix adding a point to its end if it is not empty, otherwise returns an empty prefix
	 * @param prefix The prefix to normalize
	 * @return The normalized prefix
	 */
	private static String getNormalizedPrefix(String prefix) {
		if(prefix == null) {
			prefix = "";
		}
		//If we are to use a prefix then add a point after it
		if(!prefix.equals("")) {
			prefix += ".";
		}
		
		return prefix;
	}
	
	/**
	 * Builds an AddressEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created AddressEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static AddressEntity getAddressFromResultSet(ResultSet results, String prefix) throws SQLException {
		
		prefix = getNormalizedPrefix(prefix);
		
		//Retrieve fields
		Integer addressId = results.getInt(prefix + "address_id");
		String street = results.getString(prefix + "street");
		String streetNumber = results.getString(prefix + "street_number");
	    String city = results.getString(prefix + "city");
		String district = results.getString(prefix + "district");
		String zip = results.getString(prefix + "zip");
		String country = results.getString(prefix + "country");
		BigDecimal latitude = results.getBigDecimal(prefix + "latitude");
		BigDecimal longitude = results.getBigDecimal(prefix + "longitude");
		
		AddressEntity address = new AddressEntity(addressId, street, streetNumber, city, district, zip, country, latitude, longitude);
		return address;
		
	}
	
	/**
	 * Builds a ShopEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ShopEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ShopEntity getShopFromResultsSet(ResultSet results, String prefix) throws SQLException {
		
		prefix = getNormalizedPrefix(prefix);
		
		Integer shopId = results.getInt(prefix + "shop_id");
		String description = results.getString(prefix + "description");
		String vatNumber = results.getString(prefix + "vat_number");
		String phoneNumber = results.getString(prefix + "phone_number");
		Date upgradeDate = results.getDate(prefix + "upgrade_date");
		
		ShopEntity shop = new ShopEntity(shopId, description, vatNumber, phoneNumber, upgradeDate);
		return shop;
	}
	
	/**
	 * Builds a ResourceEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ResourceEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ResourceEntity getResourceFromResultSet(ResultSet results, String prefix) throws SQLException {
		
		prefix = getNormalizedPrefix(prefix);
		
		Integer resourceId = results.getInt(prefix + "resource_id");
		String filename = results.getString(prefix + "filename");
		String friendlyName = results.getString(prefix + "friendly_name");
		
		ResourceEntity resource = new ResourceEntity(resourceId, filename, friendlyName);
		return resource;
	}
	
	/**
	 * Builds a ItemEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ItemEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ItemEntity getItemFromResultSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer itemId = results.getInt(prefix + "item_id");
		String title = results.getString(prefix + "title");
		String description = results.getString(prefix + "description");
		Date insertDate = results.getDate(prefix + "insert_date");
		Department department = Department.getById(results.getInt(prefix + "department_id"));
		
		ItemEntity item = new ItemEntity(itemId, title, description, insertDate, department);
		return item;
	}
	
	/**
	 * Builds a ShopShipmentTypeEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ShopShipmentTypeEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ShopShipmentTypeEntity getShopShipmentTypeFromResultSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer shopId = results.getInt(prefix + "shop_id");
		ShipmentType shipmentType = ShipmentType.getById(results.getInt(prefix + "shipment_type_id"));
		BigDecimal price = results.getBigDecimal(prefix + "price");
		
		ShopShipmentTypeEntity shopShipmentType = new ShopShipmentTypeEntity(shopId, shipmentType, price);
		return shopShipmentType;
	}
	
	/**
	 * Builds a ShopOrderStatusEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ShopOrderStatusEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ShopOrderStatusEntity getShopOrderStatusFromResultSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer shopOrderId = results.getInt(prefix + "shop_order_id");
		ShopOrderStatusType shopOrderStatus = ShopOrderStatusType.getById(results.getInt(prefix + "shop_order_status_type_id"));
		Date changeDate = results.getDate(prefix + "change_date");
		
		ShopOrderStatusEntity shopOrderStatusEntity = new ShopOrderStatusEntity(shopOrderId, shopOrderStatus, changeDate);
		return shopOrderStatusEntity;
	}
	
	/**
	 * Builds a IssueEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created IssueEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static IssueEntity getIssueFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer issueId = results.getInt(prefix + "issue_id");
		String description = results.getString(prefix + "description");
		Date openDate = results.getDate(prefix + "open_date");
		int issueResultId = results.getInt(prefix + "issue_result_id");
		IssueResult issueResult = null;
		//Handles the case where the issue is not already closed
		if(issueResultId != 0) {
			issueResult = IssueResult.getById(issueResultId);
		}
		String motivation = results.getString(prefix + "motivation");
		Date closeDate = results.getDate(prefix + "close_date");
		Integer shopOrderItemId = results.getInt(prefix + "shop_order_item_id");
		
		IssueEntity issueEntity = new IssueEntity(issueId, description, openDate, issueResult, motivation, closeDate, shopOrderItemId);
		return issueEntity;
	}
	
	/**
	 * Builds a UserEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created UserEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static UserEntity getUserFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer userId = results.getInt(prefix + "user_id");
		String username = results.getString(prefix + "username");
		HashDigest passwordHash = new HashDigest(results.getBytes(prefix + "password_hash"));
		String firstName = results.getString(prefix + "first_name");
		String lastName = results.getString(prefix + "last_name");
		Integer addressId = results.getInt(prefix + "address_id");
		String email = results.getString(prefix + "email");
		Date registrationDate = results.getDate(prefix + "registration_date");
		SecurityCode registrationCode = new SecurityCode(results.getBytes(prefix + "registration_code"));
		boolean acrivated = results.getBoolean(prefix + "activated");
		
		//Initializes the password reset code only if it exists
		byte[] resetCodeBytes = results.getBytes(prefix + "password_reset_code");
		SecurityCode passwordResetCode = null;
		if(resetCodeBytes != null) {
			passwordResetCode = new SecurityCode(resetCodeBytes);
		}

		UserEntity userEntity = new UserEntity(userId, username, passwordHash, firstName, lastName, addressId, email, registrationDate, registrationCode, acrivated, passwordResetCode);
		return userEntity;
	}
	
	/**
	 * Builds a ItemReviewEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ItemReviewEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ItemReviewEntity getItemReviewFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer itemReviewId = results.getInt(prefix + "item_review_id");
		String title = results.getString(prefix + "title");
		String description = results.getString(prefix + "description");
		Rating rating = new Rating(results.getInt(prefix + "rating"));
		Date date = results.getDate(prefix + "date");
		Integer shopOrderItemId = results.getInt(prefix + "shop_order_item_id");

		ItemReviewEntity itemReview = new ItemReviewEntity(itemReviewId, title, description, rating, date, shopOrderItemId);
		return itemReview;
	}
	
	/**
	 * Builds a ShopReviewEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ShopReviewEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ShopReviewEntity getShopReviewFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer shopReviewId = results.getInt(prefix + "shop_review_id");
		String title = results.getString(prefix + "title");
		String description = results.getString(prefix + "description");
		Rating rating = new Rating(results.getInt(prefix + "rating"));
		Date date = results.getDate(prefix + "date");
		Integer shopOrderId = results.getInt(prefix + "shop_order_id");

		ShopReviewEntity shopReview = new ShopReviewEntity(shopReviewId, title, description, rating, date, shopOrderId);
		return shopReview;
	}
	
	/**
	 * Builds a ShopOrderItemEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ShopOrderItemEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ShopOrderItemEntity getShopOrderItemFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer shopOrderItemId = results.getInt(prefix + "shop_order_item_id");
		Integer shopOrderId = results.getInt(prefix + "shop_order_id");
		Integer itemId = results.getInt(prefix + "item_id");
		BigDecimal price = results.getBigDecimal(prefix + "price");
		Integer quantity = results.getInt(prefix + "quantity");

		ShopOrderItemEntity shopOrderItem = new ShopOrderItemEntity(shopOrderItemId, shopOrderId, itemId, price, quantity);
		return shopOrderItem;
	}
	
	/**
	 * Builds a ShopOrderEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ShopOrderEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ShopOrderEntity getShopOrderFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer shopOrderId = results.getInt(prefix + "shop_order_id");
		Date date = results.getDate(prefix + "date");
		Integer userId = results.getInt(prefix + "user_id");
		Integer shopId = results.getInt(prefix + "shop_id");
		Integer addressId = results.getInt(prefix + "address_id");
		ShipmentType shipmentType = ShipmentType.getById(results.getInt(prefix + "shipment_type_id"));
		BigDecimal shipmentPrice = results.getBigDecimal(prefix + "shipment_price");

		ShopOrderEntity shopOrder = new ShopOrderEntity(shopOrderId, date, userId, shopId, addressId, shipmentType, shipmentPrice);
		return shopOrder;
	}
	
	/**
	 * Builds a ShopItemEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created ShopItemEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ShopItemEntity getShopItemFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer shopItemId = results.getInt(prefix + "shop_item_id");
		Integer shopId = results.getInt(prefix + "shop_id");
		Integer itemId = results.getInt(prefix + "item_id");
		BigDecimal price = results.getBigDecimal(prefix + "price");
		Integer availability = results.getInt(prefix + "availability");

		ShopItemEntity shopItem = new ShopItemEntity(shopItemId, shopId, itemId, price, availability);
		return shopItem;
	}
	
	/**
	 * Builds a CartEntryEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created CartEntryEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static CartEntryEntity getCartEntryFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
			
		Integer cartEntryId = results.getInt(prefix + "cart_entry_id");
		Integer userId = results.getInt(prefix + "user_id");
		Integer shopItemId = results.getInt(prefix + "shop_item_id");
		Integer quantity = results.getInt(prefix + "quantity");
		
		CartEntryEntity cartEntry = new CartEntryEntity(cartEntryId, userId, shopItemId, quantity);
		return cartEntry;
	}
	
	/**
	 * Builds a NotificationEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created NotificationEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static NotificationEntity getNotificationFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
		
		Integer notificationId = results.getInt(prefix + "notification_id");
		Date date = results.getDate(prefix + "date");
		boolean isNew = results.getBoolean(prefix + "is_new");
		NotificationType notificationType = NotificationType.getById(results.getInt(prefix + "notification_type_id"));
		Integer userId = results.getInt(prefix + "user_id");
		Integer shopReviewId = results.getInt(prefix + "shop_review_id");
		Integer shopOrderId = results.getInt(prefix + "shop_order_id");
		Integer issueId = results.getInt(prefix + "issue_id");
		
		NotificationEntity notification = null;
		
		//Builds the correct NotificationEntity according to the NotificationType
		switch(notificationType) {
			case USER_ISSUE_CLOSED_NOTIFICATION:
				notification = new UserIssueClosedNotificationEntity(notificationId, date, isNew, userId, issueId);
				break;
			case SHOP_ISSUE_OPENED_NOTIFICATION:
				notification = new ShopIssueOpenedNotificationEntity(notificationId, date, isNew, userId, issueId);
				break;
			case SHOP_ISSUE_CLOSED_NOTIFICATION:
				notification = new ShopIssueClosedNotificationEntity(notificationId, date, isNew, userId, issueId);
				break;
			case ADMIN_ISSUE_OPENED_NOTIFICATION:
				notification = new AdminIssueOpenedNotificationEntity(notificationId, date, isNew, issueId);
				break;
			case SHOP_ORDER_ADDED_NOTIFICATION:
				notification = new ShopOrderAddedNotification(notificationId, date, isNew, userId, shopOrderId);
				break;
			case SHOP_ORDER_STATUS_NOTIFICATION:
				notification = new ShopOrderStatusNotification(notificationId, date, isNew, userId, shopOrderId);
				break;
			case SHOP_REVIEW_NOTIFICATION:
				notification = new ShopReviewNotification(notificationId, date, isNew, userId, shopReviewId);
				break;
			default:
				//Ensures we do not forget to update with new NotificationTypes
				throw new UnsupportedOperationException("Unknown notification type: " + notification);
		}
				
		return notification;
	}
	
	/**
	 * Builds a AdministratorEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created AdministratorEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static AdministratorEntity getAdministratorFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
			
		Integer administratorId = results.getInt(prefix + "administrator_id");
		Date upgradeDate = results.getDate(prefix + "upgrade_date");
		
		AdministratorEntity administrator = new AdministratorEntity(administratorId, upgradeDate);
		return administrator;
	}
	
	/**
	 * Builds a AdministratorEntity from the current results Set
	 * 
	 * @param results A results set containing fields named prefix.canonicalDbName
	 * @param prefix A prefix to use for qualifying table in the resutls set. Can be emptpy or null
	 * @return The newly created AdministratorEntity
	 * 
	 * @throws SQLException In case results cannot be retrieved from the result set
	 */
	public static ShopReviewAggregateInfo getShopReviewAggregateFromResultsSet(ResultSet results, String prefix) throws SQLException {
		prefix = getNormalizedPrefix(prefix);
			
		Rating averageRating = new Rating(results.getInt(prefix + "average_rating"));
		Integer reviewsCount = results.getInt(prefix + "reviews_count");
		
		ShopReviewAggregateInfo shopReviewAggregateInfo = new ShopReviewAggregateInfo(averageRating, reviewsCount);
		return shopReviewAggregateInfo;
	}
	
}
