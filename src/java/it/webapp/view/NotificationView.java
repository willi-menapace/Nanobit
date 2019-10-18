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

import it.webapp.applicationcontroller.AdminIssueBaseController;
import it.webapp.applicationcontroller.AuthenticatedUserInfo;
import it.webapp.applicationcontroller.AuthenticationInfo;
import it.webapp.applicationcontroller.OrderInfoController;
import it.webapp.applicationcontroller.ShopController;
import it.webapp.applicationcontroller.ShopIssueBaseController;
import it.webapp.applicationcontroller.UserIssueBaseController;
import it.webapp.db.entities.NotificationEntity;
import it.webapp.db.entities.NotificationType;
import it.webapp.requestprocessor.OrderInfoPreprocessor;
import it.webapp.requestprocessor.ShopPreprocessor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotificationView implements View {

	public static final String NOTIFICATIONS_ATTRIBUTE = "notification_view";
	
	/**
	 * Convenience method for setting View parameters
	 * @param notifications The notifications to send
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(List<NotificationEntity> notifications, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		request.setAttribute(NOTIFICATIONS_ATTRIBUTE, notifications);
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
		
		//Print each order
		List<NotificationEntity> notificationsList = (List<NotificationEntity>) request.getAttribute(NOTIFICATIONS_ATTRIBUTE);
		for(NotificationEntity currentNotification : notificationsList) {
			
			NotificationType notificationType = currentNotification.getType();
			
			String title = currentNotification.getType().getDescription();
			String text;
			String linkText;
			String link;
			
			if(notificationType == NotificationType.ADMIN_ISSUE_OPENED_NOTIFICATION) {
				text = "E' arrivata una nuova segnalazione da gestire";
				linkText = "Gestisci segnalazioni";
				link = request.getContextPath() + AdminIssueBaseController.URL;
			} else if(notificationType == NotificationType.SHOP_ISSUE_CLOSED_NOTIFICATION) {
				text = "Una segnalazione relativa al tuo negozio e' stata gestita";
				linkText = "Visualizza segnalazioni";
				link = request.getContextPath() + ShopIssueBaseController.URL;
			} else if(notificationType == NotificationType.SHOP_ISSUE_OPENED_NOTIFICATION) {
				text = "Una segnalazione relativa al tuo negozio e' stata aperta";
				linkText = "Visualizza segnalazioni";
				link = request.getContextPath() + ShopIssueBaseController.URL;
			} else if(notificationType == NotificationType.SHOP_ORDER_ADDED_NOTIFICATION) {
				text = "E' stato effettuato un nuovo ordina presso il tuo negozio";
				linkText = "Visualizza ordine";
				link = request.getContextPath() + OrderInfoController.URL + "?" + OrderInfoPreprocessor.SHOP_ORDER_ID_PARAM + "=" + currentNotification.getShopOrderId();
			}  else if(notificationType == NotificationType.SHOP_ORDER_STATUS_NOTIFICATION) {
				text = "Un tuo ordine ha cambiato stato";
				linkText = "Visualizza ordine";
				link = request.getContextPath() + OrderInfoController.URL + "?" + OrderInfoPreprocessor.SHOP_ORDER_ID_PARAM + "=" + currentNotification.getShopOrderId();
			}  else if(notificationType == NotificationType.SHOP_REVIEW_NOTIFICATION) {
				text = "E' stata lasciata una nuova recensione per il tuo negozio";
				linkText = "Visualizza recensioni";
				AuthenticatedUserInfo authInfo = (AuthenticatedUserInfo) request.getSession().getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
				
				link = request.getContextPath() + ShopController.URL + "?" + ShopPreprocessor.SHOP_ID_PARAM + "=" + authInfo.getUserId();
			}   else if(notificationType == NotificationType.USER_ISSUE_CLOSED_NOTIFICATION) {
				text = "Una tua segnalazione e' stata gestita";
				linkText = "Visualizza segnalazioni";
				link = request.getContextPath() + UserIssueBaseController.URL;
			} else {
				throw new UnsupportedOperationException("You must provide an implementation for every notification types");
			}
			
			writer.write("<div class=\"content-box\">");
			
			writer.write("<span class=\"title\">" + title + "</span>");
			writer.write("<div class=\"spacer\"></div>");
			
			writer.write("<div class=\"info\">");
			
			//Order info
			writer.write("<div class=\"orderinfo-grid-container1\">");
			
			writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-value\">" + text + "</p>");
			writer.write("</div>");
		
			writer.write("<div class=\"info-noborder\">");
			writer.write("<a class=\"border-link-button\" href=\"" + link + "\">" + linkText + "</a>");
			writer.write("</div>");
                        
                        if(currentNotification.getIsNew()) {
                            writer.write("<div class=\"info-noborder\">");
                            writer.write("<i class=\"fa fa-envelope-o\"></i>");
                            writer.write("</div>");
                        } else {
                            writer.write("<div class=\"info-noborder\">");
                            writer.write("<i class=\"fa fa-envelope-open-o\"></i>");
                            writer.write("</div>");
                        }
                        
			writer.write("</div>");
			writer.write("</div>");
			writer.write("</div>");
			writer.write("</div>");
			
		}
	}

}
