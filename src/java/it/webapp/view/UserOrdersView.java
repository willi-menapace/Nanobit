/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author:
 * 
 ******************************************************************************/

package it.webapp.view;

import it.webapp.applicationcontroller.OrderInfoController;
import it.webapp.applicationcontroller.ShopController;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.ShopOrderAggregateInfo;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderStatusEntity;
import it.webapp.db.entities.UserEntity;
import it.webapp.requestprocessor.OrderInfoPreprocessor;
import it.webapp.requestprocessor.ShopPreprocessor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends information about orders as a response
 */
public class UserOrdersView implements View {

	public static final String SHOP_ORDERS_ATTRIBUTE = "user_orders_view_shop_orders";
	
	/**
	 * Convenience method for setting View parameters
	 * @param shopOrders The shop orders to send
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(List<ShopOrderAggregateInfo> shopOrders, HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
		
		request.setAttribute(SHOP_ORDERS_ATTRIBUTE, shopOrders);
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
		List<ShopOrderAggregateInfo> shopOrdersAggregate = (List<ShopOrderAggregateInfo>) request.getAttribute(SHOP_ORDERS_ATTRIBUTE);
		for(ShopOrderAggregateInfo currentOrderAggregate : shopOrdersAggregate) {
			UserEntity user = currentOrderAggregate.getUser();
			AddressEntity address = currentOrderAggregate.getShipmentAddress();
			ShopOrderEntity shopOrder = currentOrderAggregate.getShopOrder();

			List<ShopOrderStatusEntity> orderStatuses = currentOrderAggregate.getOrderStatuses();
			
			writer.write("<div class=\"content-box\">");
			
			writer.write("<span class=\"title\">Dettagli dell'ordine</span>");
			writer.write("<div class=\"spacer\"></div>");
			
			writer.write("<div class=\"info\">");
			
			//Order info
			writer.write("<div class=\"orderinfo-grid-container1\">");
			
			writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-label\">Ordine</p>");
			writer.write("<p class=\"info-value\">" + shopOrder.getShopOrderId() + "</p>");
			writer.write("</div>");
			writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-label\">Data</p>");
			writer.write("<p class=\"info-value\">" + shopOrder.getDate() + "</p>");
			writer.write("</div>");
			
			writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-label\">Spedizione</p>");
			writer.write("<p class=\"info-value\">");
			writer.write("<span>" + shopOrder.getShipmentType().getShipmentDescription() + " " + "</span>");
			writer.write("<span>(" + shopOrder.getShipmentPrice() + "&euro;)<span>");
			writer.write("</p>");
			writer.write("</div>");
			writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-label\">Indirizzo consegna</p>");
			writer.write("<div class=\"info-value\">");
			writer.write("<span>" + address.getStreet() + ", " + address.getStreetNumber() + "</span><br>");
			writer.write("<span>" + address.getZip() + " " + address.getCity() + " " + address.getDistrict() + ", " + address.getCountry() + "</span>");
			writer.write("</div>");
			writer.write("</div>");
                        
                        writer.write("<div class=\"info-noborder\">");
			writer.write("<p class=\"info-label\">Venditore</p>");
			writer.write("<div class=\"info-value\">");
                        writer.write("<a class=\"seller-link\" href=\"" + request.getContextPath() + ShopController.URL + "?" + ShopPreprocessor.SHOP_ID_PARAM + "=" + user.getUserId() + "\"><span>" + user.getUsername() + "</span></a><br>");
			writer.write("</div>");
			writer.write("</div>");
			
			
			writer.write("</div>");
			//Order status info
			writer.write("<div class=\"info-noborder\">");
			
			writer.write("<p class=\"info-label\">Stato ordine</p>");
			//Info for every status
			writer.write("<ul class=\"order-status\">");
			
			for(ShopOrderStatusEntity currentOrderStatus : orderStatuses) {
				
				writer.write("<li class=\"status-entry\">");
				writer.write("<span class=\"status-value\">" + currentOrderStatus.getShopOrderStatus().getDescription() + "</span>");
				writer.write("<span class=\"status-date\">" + currentOrderStatus.getChangeDate() + "</span>");
				writer.write("</li>");
				
			}
			
			writer.write("</ul>");

			writer.write("</div>");
			
			writer.write("<div class=\"info-noborder\">");
			writer.write("<a class=\"border-link-button\" href=\"" + request.getContextPath() + OrderInfoController.URL + "?" + OrderInfoPreprocessor.SHOP_ORDER_ID_PARAM + "=" + shopOrder.getShopOrderId() + "\">Dettagli</a>");
			writer.write("</div>");

			writer.write("</div>");
			
			writer.write("</div>");
			
		}
		
	}

}
