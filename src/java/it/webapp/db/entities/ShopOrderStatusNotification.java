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

import java.util.Date;

public class ShopOrderStatusNotification extends NotificationEntity {
	
	private Integer shopOrderId;

	public ShopOrderStatusNotification(Integer notificationId, Date date, boolean isNew, Integer userId, Integer shopOrderId) {
		super(notificationId, date, isNew, userId);
		
		this.shopOrderId = shopOrderId;
	}

	@Override
	public Integer getShopReviewId() {
		return null;
	}

	@Override
	public Integer getShopOrderId() {
		return shopOrderId;
	}

	@Override
	public Integer getIssueId() {
		return null;
	}

	@Override
	public NotificationType getType() {
		return NotificationType.SHOP_ORDER_STATUS_NOTIFICATION;
	}

	public void setShopOrderId(Integer shopOrderId) {
		this.shopOrderId = shopOrderId;
	}
	
}