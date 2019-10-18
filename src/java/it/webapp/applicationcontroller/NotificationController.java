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
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcNotificationDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.NotificationEntity;
import it.webapp.db.entities.NotificationType;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.NotificationPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.NotificationView;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Manages a request for the notifications of a given user
 */
public class NotificationController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "Notification";
	
	private JdbcNotificationDao notificationDao;
	
	public NotificationController() throws ApplicationControllerException {
		super(new NotificationPreprocessor());
	
		try {
			notificationDao = JdbcDaoFactory.getNotificationDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int userId = (int) request.getAttribute(NotificationPreprocessor.USER_ID_ATTRIBUTE);
		int offset = (int) request.getAttribute(NotificationPreprocessor.OFFSET_ATTRIBUTE);
		int count = (int) request.getAttribute(NotificationPreprocessor.COUNT_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			List<NotificationEntity> notifications = null;
			
			try {
				//Determines whether the user must receive admin notifications
				HttpSession session = request.getSession();
				AuthenticatedUserInfo authenticationInfo = (AuthenticatedUserInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
			
				notifications = notificationDao.getByUserId(userId, offset, count, authenticationInfo.isAdmin(), transaction);

			} catch(DaoException e) {
				Logger.log("Database could not satisfy notification request", e);
				
				notifications = null; //An error occurred, throw results away
			}
			
			//Sends the notifications as a response
			NotificationView.setRequestParameters(notifications, request);
			NotificationView notificationView = ViewFactory.getView(NotificationView.class);
			notificationView.view(request, response);
			
			try {
				//Sets the retrieved notifications as read
				for(NotificationEntity currentNotification : notifications) {
					if(currentNotification.getIsNew()) {
						currentNotification.setIsNew(false);
						notificationDao.update(currentNotification, transaction);
					}
				}
				
			} catch(DaoException e) {
				Logger.log("Database could not set notifications as read", e);
				
				notifications = null; //An error occurred, throw results away
			}
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}
}
