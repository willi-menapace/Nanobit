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

public enum NotificationType {
	USER_ISSUE_CLOSED_NOTIFICATION(1, "Segnalazione chiusa"),
	SHOP_ISSUE_OPENED_NOTIFICATION(2, "Segnalazione negozio aperta"),
	SHOP_ISSUE_CLOSED_NOTIFICATION(3, "Segnalazione negozio chiusa"), 
	ADMIN_ISSUE_OPENED_NOTIFICATION(4, "Segnalazione da gestire"),
	SHOP_ORDER_ADDED_NOTIFICATION(5, "Nuovo ordine"), 
	SHOP_ORDER_STATUS_NOTIFICATION(6, "Stato ordine cambiato"),
	SHOP_REVIEW_NOTIFICATION(7, "Nuova recensione");
	
	public static NotificationType getById(int notificationTypeId) {
		NotificationType notificationType = null;
		
		switch(notificationTypeId) {
			case 1:
				notificationType = USER_ISSUE_CLOSED_NOTIFICATION;
				break;
			case 2:
				notificationType = SHOP_ISSUE_OPENED_NOTIFICATION;
				break;
			case 3:
				notificationType = SHOP_ISSUE_CLOSED_NOTIFICATION;
				break;
			case 4:
				notificationType = ADMIN_ISSUE_OPENED_NOTIFICATION;
				break;
			case 5:
				notificationType = SHOP_ORDER_ADDED_NOTIFICATION;
				break;
			case 6:
				notificationType = SHOP_ORDER_STATUS_NOTIFICATION;
				break;
			case 7:
				notificationType = SHOP_REVIEW_NOTIFICATION;
				break;
			default:
				//Ensure we do not miss to update the swtich statement with new NotificationTypes
				throw new UnsupportedOperationException("No NotificationType corresponding to id: " + notificationTypeId);
		}
		
		return notificationType;
	}
	
	private final int notificationTypeId;
	private final String description;

	NotificationType(int notificationTypeId, String descirption) {
		this.notificationTypeId = notificationTypeId;
		this.description = descirption;
	}
	public int getId() {
		return notificationTypeId;
	}
	
	public String getDescription() {
		return description;
	}
	
}
