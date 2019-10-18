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

public abstract class IssueNotificationEntity extends NotificationEntity {
	
	private Integer issueId;

	public IssueNotificationEntity(Integer notificationId, Date date, boolean isNew, Integer userId, Integer issueId) {
		super(notificationId, date, isNew, userId);
		
		this.issueId = issueId;
	}

	@Override
	public Integer getShopReviewId() {
		return null;
	}

	@Override
	public Integer getShopOrderId() {
		return null;
	}

	@Override
	public Integer getIssueId() {
		return issueId;
	}
	
	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	
}
