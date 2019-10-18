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

public abstract class NotificationEntity {
	private Integer notificationId;
	private Date date;
	private boolean isNew;
	private Integer userId;

	public NotificationEntity(Integer notificationId, Date date, boolean isNew, Integer userId) {
		this.notificationId = notificationId;
		this.date = date;
		this.isNew = isNew;
		this.userId = userId;
	}

	public abstract NotificationType getType();
	
	public abstract Integer getShopReviewId();
	public abstract Integer getShopOrderId();
	public abstract Integer getIssueId();
	
	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}