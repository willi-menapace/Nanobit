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

import it.webapp.requestprocessor.AddIssueEvaluationFormPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.AddIssueEvaluationView;
import it.webapp.view.View;
import it.webapp.view.ViewFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages a request for a form to close an Issue
 */
public class AddIssueEvaluationFormController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "AddIssueEvaluationForm";
	
	public AddIssueEvaluationFormController() {
		super(new AddIssueEvaluationFormPreprocessor());
		
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int issueId = (int) request.getAttribute(AddIssueEvaluationFormPreprocessor.ISSUE_ID_ATTRIBUTE);

		AddIssueEvaluationView.setRequestParameters(issueId, null, request);
		View view = ViewFactory.getView(AddIssueEvaluationView.class);

		view.view(request, response);
	}

}
