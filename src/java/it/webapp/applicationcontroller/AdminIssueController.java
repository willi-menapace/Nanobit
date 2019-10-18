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
import it.webapp.db.entities.IssueAggregateInfo;
import it.webapp.db.entities.IssueEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.AdminIssuePreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.IssueView;
import it.webapp.view.ViewFactory;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for issues opened by all users
 */
public class AdminIssueController extends ApplicationControllerSkeleton {
	
	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AdminIssue";
	
	private JdbcIssueDao issueDao;
	
	public AdminIssueController() throws ApplicationControllerException {
		super(new AdminIssuePreprocessor());
			
		try {
			issueDao = JdbcDaoFactory.getIssueDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Could not initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		//admin id not necessary
		//int adminId = (int) request.getAttribute(AdminIssuePreprocessor.ADMIN_ID_ATTRIBUTE);
		int offset = (int) request.getAttribute(AdminIssuePreprocessor.OFFSET_ATTRIBUTE);
		int count = (int) request.getAttribute(AdminIssuePreprocessor.COUNT_ATTRIBUTE);
		
		//Tries to obtain the issues
		List<IssueEntity> issues = null;
		List<IssueAggregateInfo> issueAggregate = new ArrayList<>();
		try {
			issues = issueDao.getCloseOrderedIssues(offset, count);
		
			for(IssueEntity currentIssue : issues) {
				issueAggregate.add(issueDao.getAggregateInfoById(currentIssue.getIssueId()));
			}
		} catch(DaoException e) {
			Logger.log("Database error while requesting issues", e);
		}
		
		//Returns the issues or an error message in case issues are null
		IssueView.setRequestParameters(issueAggregate, true, request);
		IssueView issueView = ViewFactory.getView(IssueView.class);
		issueView.view(request, response);
		
	}

}
