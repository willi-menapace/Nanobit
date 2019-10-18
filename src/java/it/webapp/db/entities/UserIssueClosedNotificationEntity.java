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

public class UserIssueClosedNotificationEntity extends IssueNotificationEntity {

	public UserIssueClosedNotificationEntity(Integer notificationId, Date date, boolean isNew, Integer userId, Integer issueId) {
		super(notificationId, date, isNew, userId, issueId);
	}
	
	@Override
	public NotificationType getType() {
		return NotificationType.USER_ISSUE_CLOSED_NOTIFICATION;
	}
	
}
