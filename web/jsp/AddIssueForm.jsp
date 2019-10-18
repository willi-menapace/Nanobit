<%--
    Document    : IssueForm
    Author      : Daniele Giuliani, Alessio Paternoster
	Desciption  : Pagina utilizzata dall'utente per aprire una segnalazione di un prodotto
    Requirements: Base.css, IssueForm.css, Cart.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.view.AddIssueFormView" %>
<%@page import="it.webapp.applicationcontroller.StaticContentController" %>
<%@page import="it.webapp.applicationcontroller.AddIssueController" %>
<%@page import="it.webapp.requestprocessor.StaticContentPreprocessor" %>
<%@page import="it.webapp.requestprocessor.AddIssuePreprocessor" %>
<%@page import="it.webapp.db.entities.ShopOrderEntity" %>
<%@page import="it.webapp.db.entities.ItemEntity" %>
<%@page import="it.webapp.db.entities.ShopOrderItemEntity" %>
<%@page import="it.webapp.db.entities.ShipmentType" %>
<%@page import="it.webapp.db.entities.ResourceEntity" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="shopOrder" value="<%= (ShopOrderEntity) request.getAttribute(AddIssueFormView.SHOP_ORDER_ATTRIBUTE) %>"></c:set>
<c:set var="shopOrderItem" value="<%= (ShopOrderItemEntity) request.getAttribute(AddIssueFormView.SHOP_ORDER_ITEM_ATTRIBUTE) %>"></c:set>
<c:set var="item" value="<%= (ItemEntity) request.getAttribute(AddIssueFormView.ITEM_ATTRIBUTE) %>"></c:set>

<%-- Item can have or not an image, we set it properly --%>
<%
    String itemImageURI = "/resource/Item/default.jpg";
    ResourceEntity imageResource = (ResourceEntity) request.getAttribute(AddIssueFormView.IMAGE_ATTRIBUTE);
    
    /* If the item has an image, we replace the default one with that */
    if(imageResource != null){
        itemImageURI = StaticContentController.URL;
        itemImageURI += "?" + StaticContentPreprocessor.RESOURCE_PARAM + "=" + imageResource.getFilename();
    }
    
    pageContext.setAttribute("itemImageURI", itemImageURI);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Segnalazione prodotto | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/IssueForm.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Cart.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="content-box">
                    <span class="title">Apri segnalazione prodotto</span>
                    <div class="spacer"></div>
                    <div class="info">
                        <div class="info-noborder">
                            <p class="info-label">Numero ordine</p>
                            <p class="info-value">${shopOrder.shopOrderId}</p>
                        </div>
                        <div class="info-noborder">
                            <p class="info-label">Data ordine</p>
                            <p class="info-value">${shopOrder.date}</p>
                        </div>
                        <div class=info-noborder>
                            <p class="info-label">Articolo</p>
                            <div class="content-box-inner grid-container">
                                <p class="item-title">${item.title}</p>
                                <img src="${contextPath}${itemImageURI}" class="item-image">
                                <div class="grid-info">
                                    <p>
                                        <span class="info-label">Qt. </span>
                                        <span>${shopOrderItem.quantity}</span>
                                    </p>
                                    <p>
                                        <span class="info-label">Prezzo: </span>
                                        <span>${shopOrderItem.price} &euro;</span>
                                    </p>
                                    <p>
                                        <span class="info-label">Tipologia spedizione: </span>
                                        <span>${shopOrder.shipmentType.shipmentDescription} (${shopOrder.shipmentPrice} &euro;)</span>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <form method="POST" action="${contextPath}${AddIssueController.URL}" class="info-noborder">
                            <input type="text" name="${AddIssuePreprocessor.SHOP_ORDER_ITEM_ID_PARAM}" value="${shopOrderItem.shopOrderItemId}" hidden/>
                            <p class="info-label">Descrizione del problema</p>
                            <textarea name="${AddIssuePreprocessor.DESCRIPTION_PARAM}" placeholder="Inserisci una breve descrizione del tuo problema" class="textbox"></textarea>
                            <div class="submit-wrap">
                                <button type="submit" class="link-button">Invia Segnalazione</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <%@include file="/WEB-INF/jspf/Footer.jspf" %>
        </div>
    </body>
    <!-- Script -->
    <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/TopBar.js"></script>
</html>
