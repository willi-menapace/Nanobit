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

public class AdminIssueOpenedNotificationEntity extends IssueNotificationEntity {

	public AdminIssueOpenedNotificationEntity(Integer notificationId, Date date, boolean isNew, Integer issueId) {
		//Admin issues are not specific for any user
		super(notificationId, date, isNew, null, issueId);
	}
	
	@Override
	public NotificationType getType() {
		return NotificationType.ADMIN_ISSUE_OPENED_NOTIFICATION;
	}
	
}