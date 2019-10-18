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
import it.webapp.db.dao.jdbc.JdbcIssueDao;
import it.webapp.db.dao.jdbc.JdbcNotificationDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderItemDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.IssueEntity;
import it.webapp.db.entities.IssueResult;
import it.webapp.db.entities.ShopIssueClosedNotificationEntity;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import it.webapp.db.entities.UserIssueClosedNotificationEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.AddIssueEvaluationPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.AddIssueEvaluationView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request to close an Issue
 */
public class AddIssueEvaluationController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddIssueEvaluation";
	
	private JdbcIssueDao issueDao;
	private JdbcNotificationDao notificationDao;
	private JdbcShopOrderItemDao shopOrderItemDao;
	private JdbcShopOrderDao shopOrderDao;
	
	public AddIssueEvaluationController() throws ApplicationControllerException {
		super(new AddIssueEvaluationPreprocessor());
		
		try {
			issueDao = JdbcDaoFactory.getIssueDao();
			notificationDao = JdbcDaoFactory.getNotificationDao();
			shopOrderItemDao = JdbcDaoFactory.getShopOrderItemDao();
			shopOrderDao = JdbcDaoFactory.getShopOrderDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int issueId = (int) request.getAttribute(AddIssueEvaluationPreprocessor.ISSUE_ID_ATTRIBUTE);
		int issueResultId = (int) request.getAttribute(AddIssueEvaluationPreprocessor.ISSUE_RESULT_ID_ATTRIBUTE);
		String motivation = (String) request.getAttribute(AddIssueEvaluationPreprocessor.MOTIVATION_ATTRIBUTE);
		
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			
			View view;
			try {
				
				//Retrieves referenced issue
				IssueEntity issue = issueDao.getById(issueId, transaction);
				
				if(issue == null) {
					AddIssueEvaluationView.setRequestParameters(issueId, "Specified issue does not exist", request);
					view = ViewFactory.getView(AddIssueEvaluationView.class);
				} else if(issue.getIssueResult() != null) {
					AddIssueEvaluationView.setRequestParameters(issueId, "Issue already closed", request);
					view = ViewFactory.getView(AddIssueEvaluationView.class);
				} else {
					issue.closeIssue(IssueResult.getById(issueResultId), motivation);
					
					issueDao.update(issue, transaction);
					
					//Gets associated order information
					ShopOrderItemEntity shopOrderItem = shopOrderItemDao.getById(issue.getShopOrderItemId(), transaction);
					ShopOrderEntity shopOrder = shopOrderDao.getById(shopOrderItem.getShopOrderId(), transaction);
					
					UserIssueClosedNotificationEntity userNotification = new UserIssueClosedNotificationEntity(null, new Date(), true, shopOrder.getUserId(), issueId);
					ShopIssueClosedNotificationEntity shopNotification = new ShopIssueClosedNotificationEntity(null, new Date(), true, shopOrder.getShopId(), issueId);
					
					notificationDao.insert(userNotification, transaction);
					notificationDao.insert(shopNotification, transaction);
					
					AddIssueEvaluationView.setRequestParameters(issueId, "Segnalazione chiusa con successo", request);
					view = ViewFactory.getView(AddIssueEvaluationView.class);
				}
				
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("Could not add a new issue due to database error", e);
				
				AddIssueEvaluationView.setRequestParameters(issueId, "Database error", request);
				view = ViewFactory.getView(AddIssueEvaluationView.class);
			}
			view.view(request, response);
			
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		} catch(DaoFactoryException e) {
			Logger.log("Could not begin transaction", e);
		}
		
	}

}
