<%--
    Document    : UpdateShopShipmentType.jsp
    Author      : Daniele Giuliani
    Desciption  : Pagina utilizzata da un venditrore per aggiornare le informazioni relative ai costi di spedizione
    Requiriments: Base.css, UpdateUser.css
		
					impostare messaggio facoltativo in caso di errore o meno
					controlli javascript con messaggio per evitare di inviare cavolate
--%>

<%@page import="it.webapp.requestprocessor.UpdateShopShipmentTypePreprocessor"%>
<%@page import="java.math.BigDecimal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.UpdateShopShipmentTypeController"%>
<%@page import="it.webapp.requestprocessor.UpdateShopShipmentTypeFormPreprocessor"%>
<%@page import="it.webapp.view.UpdateShopShipmentTypeFormView"%>
<%@page import="it.webapp.db.entities.ShopShipmentTypeEntity"%>
<%@page import="it.webapp.db.entities.ShipmentType"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="info_message" value="<%= (String) request.getAttribute(UpdateShopShipmentTypeFormView.MESSAGE) %>"></c:set>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="../css/Base.css">
		<link rel="stylesheet" type="text/css" href="../css/UpdateUser.css">
    <link rel="stylesheet" type="text/css" href="../css/TopBar.css">
    <link rel="stylesheet" type="text/css" href="../css/Footer.css">
		<link rel="stylesheet" type="text/css" href="../css/UpdateShopShipmentTypeForm.css">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Nanobit</title>
  </head>
  <body>
		<c:if test="${not empty info_message}">
			<div class="message-overlay">
				<div class="message message-info">
					<span class="message-title">Info</span>
					<span class="message-text">${info_message}</span>
					<button class="message-close">Chiudi</button>
				</div>
			</div>
		</c:if>
    <div class="container">
        <%@include file="../WEB-INF/jspf/TopBar.jspf" %>
				<div class="content">
					<div class="content-box content-box-middle main">
						<span class="title">Imposta costi spedizione</span>
						<p>Inserisci le informazioni relative ai costi di spedizione che devi sostenere, se non ne sei a conoscenza chiedi al tuo corriere di fiducia o all'ufficio postale più vicino.</p>
						<p>Se non puoi fornire un particolare tipo di spedizione clicca il pulsante accanto ad essa per disabilitarla.</p>
						<div class="spacer"></div>
                                                <%
                                                    //Fetch parameters
                                                    List<ShopShipmentTypeEntity> shopShipmentTypeEntities = (List<ShopShipmentTypeEntity>) request.getAttribute(UpdateShopShipmentTypeFormView.SHOP_SHIPMENT_TYPE_ENTITIES);

                                                    //Begin creating layout
                                                    for(int i = 1; i <= ShipmentType.getNumberOfShipments(); i++) {
                                                        ShipmentType currentST = ShipmentType.getById(i);
                                                        //Fetch date form each ShipmentType
                                                        int idCurrentST = currentST.getId();
                                                        String descriptionCurrentST = currentST.getShipmentDescription();
                                                        BigDecimal priceCurrentST = null;
                                                        Iterator <ShopShipmentTypeEntity> listIterator = shopShipmentTypeEntities.iterator();
                                                        while(listIterator.hasNext() && priceCurrentST == null) {
                                                            ShopShipmentTypeEntity temp = listIterator.next();
                                                            if(temp.getShipmentType().getId() == currentST.getId()) {
                                                                priceCurrentST = temp.getPrice();
                                                            }
                                                        }
                                                        out.println("<div class=info>");
                                                        out.println("<p class=info-label>" + descriptionCurrentST + "</p>");
                                                        if(priceCurrentST != null && priceCurrentST.intValue() > 0) {
                                                            //the current shipment type is in use and has a price
                                                            out.println("<input type=text id=" + idCurrentST + " value='" + priceCurrentST.toPlainString() + "' class='textbox price'/><button class=toggle type=button onclick=disableShipment(this)>Disabilita</button>");
                                                        } else {
                                                            //the current shipment type is not in use (insert negative price) and set field to disabled
                                                            out.println("<input type=text id=" + idCurrentST + " value='-1' class='textbox price-disabled' disabled/><button class=toggle type=button onclick=enableShipment(this)>Abilita</button>");
                                                        }

                                                        out.println("</div>");

                                                    }
                                                %>
                                                <form action="${UpdateShopShipmentTypeController.URL}" method="post">
                                                    <div class="submit-wrap">
                                                        <input name="${UpdateShopShipmentTypePreprocessor.SHIPMENT_TYPE_IDS_PARAM}" type="text" hidden></input>
                                                        <input name="${UpdateShopShipmentTypePreprocessor.PRICES_PARAM}" type="text" hidden></input>
                                                        <button type="submit" onclick="formatRequest()" class="link-button">Salva</button>
                                                    </div>
						</form>
					</div>
				</div>
      	<%@include file="../WEB-INF/jspf/Footer.jspf" %>
        <!-- Script -->
        <script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>
				<script src="../js/UpdateShopShipmentTypeForm.js"></script>
				<script src="../js/util.js"></script>
    </div>
  </body>
</html>
