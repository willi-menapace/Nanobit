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

package it.webapp.view;

import it.webapp.applicationcontroller.AddIssueEvaluationFormController;
import it.webapp.applicationcontroller.OrderInfoController;
import it.webapp.db.entities.IssueAggregateInfo;
import it.webapp.db.entities.IssueEntity;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import it.webapp.requestprocessor.AddIssueEvaluationFormPreprocessor;
import it.webapp.requestprocessor.OrderInfoPreprocessor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IssueView implements View {

	public static final String ISSUES_ATTRIBUTE = "user_issue_view_issues";
	public static final String ADMIN_MODE_ATTRIBUTE = "user_issue_view_admin_mode";
	
	/**
	 * Convenience method for setting View parameters
	 * @param issues List of issues, can be null
	 * @param adminMode Set to visualize the issues in administrator mode
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(List<IssueAggregateInfo> issues, boolean adminMode, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		request.setAttribute(ISSUES_ATTRIBUTE, issues);
		request.setAttribute(ADMIN_MODE_ATTRIBUTE, adminMode);
	}
	
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html");
		
		PrintWriter writer;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			throw new IllegalStateException("Cannot write response to client", e);
		}
		
		boolean adminMode = (boolean) request.getAttribute(ADMIN_MODE_ATTRIBUTE);
		//Print each order
		List<IssueAggregateInfo> issueAggregateList = (List<IssueAggregateInfo>) request.getAttribute(ISSUES_ATTRIBUTE);
		for(IssueAggregateInfo currentIssueAggregate : issueAggregateList) {
			
			IssueEntity currentIssue = currentIssueAggregate.getIssue();
			ShopOrderItemEntity currentShopOrderItem = currentIssueAggregate.getShopOrderItem();
			ItemEntity currentItem = currentIssueAggregate.getItem();
			
			writer.write("<div class=\"content-box\">");
			
			writer.write("<span class=\"title\">Dettagli della segnalazione</span>");
			writer.write("<div class=\"spacer\"></div>");
			
			writer.write("<div class=\"info\">");
			
			//Order info
			writer.write("<div class=\"orderinfo-grid-container1\">");
			
			writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-label\">Articolo</p>");
			writer.write("<p class=\"info-value\">" + currentItem.getTitle() + "</p>");
			writer.write("</div>");
			
			writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-label\">Descrizione</p>");
			writer.write("<p class=\"info-value\">" + currentIssue.getDescription() + "</p>");
			writer.write("</div>");
			writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-label\">Data</p>");
			writer.write("<p class=\"info-value\">" + currentIssue.getOpenDate() + "</p>");
			writer.write("</div>");
			
			//Issue not closed
			if(currentIssue.getIssueResult() == null) {
				//Admins can close issues while users cannot
				if(adminMode == true) {
					writer.write("<div class=\"info-noborder\">");
					writer.write("<a class=\"border-link-button\" href=\"" + request.getContextPath() + AddIssueEvaluationFormController.URL + "?" + AddIssueEvaluationFormPreprocessor.ISSUE_ID_PARAM + "=" + currentIssue.getIssueId() + "\">Valuta segnalazione</a>");
					writer.write("</div>");
				} else {
					writer.write("<div class=\"info-noborder\">");
					writer.write("<p class=\"info-label\">I nostri amministratori stanno valutando la tua segnalazione</p>");
					writer.write("</div>");
				}
			//Issue already closed
			} else {
				writer.write("<div class=\"info-noborder\">");
				writer.write("<p class=\"info-label\">Esito valutazione</p>");
				writer.write("<p class=\"info-value\">" + currentIssue.getIssueResult().getDescription() + "</p>");
				writer.write("</div>");
				writer.write("<div class=\"info-noborder\">");
				writer.write("<p class=\"info-label\">Motivazione</p>");
				writer.write("<p class=\"info-value\">" + currentIssue.getMotivation() + "</p>");
				writer.write("</div>");
				writer.write("<div class=\"info-noborder\">");
				writer.write("<p class=\"info-label\">Data valutazione</p>");
				writer.write("<p class=\"info-value\">" + currentIssue.getCloseDate() + "</p>");
				writer.write("</div>");
			}

			writer.write("</div>");
			
			writer.write("<div class=\"info-noborder\">");
			writer.write("<a class=\"border-link-button\" href=\"" + request.getContextPath() + OrderInfoController.URL + "?" + OrderInfoPreprocessor.SHOP_ORDER_ID_PARAM + "=" + currentShopOrderItem.getShopOrderId() + "\">Dettagli ordine</a>");
			writer.write("</div>");

			writer.write("</div>");
			
			writer.write("</div>");
			
		}
	}
}
