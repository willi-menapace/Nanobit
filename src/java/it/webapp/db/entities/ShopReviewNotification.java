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

public class ShopReviewNotification extends NotificationEntity {
	
	private Integer shopReviewId;

	public ShopReviewNotification(Integer notificationId, Date date, boolean isNew, Integer userId, Integer shopReviewId) {
		super(notificationId, date, isNew, userId);
		
		this.shopReviewId = shopReviewId;
	}

	@Override
	public Integer getShopReviewId() {
		return shopReviewId;
	}

	@Override
	public Integer getShopOrderId() {
		return null;
	}

	@Override
	public Integer getIssueId() {
		return null;
	}
	
	@Override
	public NotificationType getType() {
		return NotificationType.SHOP_REVIEW_NOTIFICATION;
	}

	public void setShopReviewId(Integer shopReviewId) {
		this.shopReviewId = shopReviewId;
	}
	
}