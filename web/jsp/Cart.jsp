<%--
    Document   : Cart
    Author     : Daniele Giuliani
		Desciption : Pagina utilizzata dall'utente per visualizzare il proprio carrello
		Todo			 : vedi vari TODO
--%>


<%@page import="it.webapp.applicationcontroller.StaticContentController"%>
<%@page import="it.webapp.applicationcontroller.ItemController"%>
<%@page import="it.webapp.applicationcontroller.OrderOptionsController"%>
<%@page import="it.webapp.applicationcontroller.UpdateCartItemQuantityController"%>
<%@page import="it.webapp.requestprocessor.StaticContentPreprocessor"%>
<%@page import="it.webapp.requestprocessor.ItemPreprocessor"%>
<%@page import="it.webapp.requestprocessor.UpdateCartItemQuantityPreprocessor"%>
<%@page import="it.webapp.db.entities.ItemEntity"%>
<%@page import="it.webapp.db.entities.ResourceEntity"%>
<%@page import="it.webapp.db.entities.ShopEntity"%>
<%@page import="it.webapp.db.entities.ShopItemEntity"%>
<%@page import="it.webapp.db.entities.CartEntryEntity"%>
<%@page import="it.webapp.db.entities.CartEntryAggregateInfo"%>
<%@page import="it.webapp.view.CartView"%>
<%@page import="it.webapp.view.CartView"%>
<%@page import="it.webapp.db.entities.ItemEntity"%>
<%@page import="java.util.List"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

    
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<CartEntryAggregateInfo> cartEntries = (List<CartEntryAggregateInfo>) request.getAttribute(CartView.AGGREGATE_CART_ENTRIES_ATTRIBUTE);
    List<ItemEntity> removedItems = (List<ItemEntity>) request.getAttribute(CartView.REMOVED_ITEMS_ATTRIBUTE);
    
    /* Rendo disponibili le informazioni nella pagina */
    pageContext.setAttribute("cartEntries", cartEntries);
    pageContext.setAttribute("removedItems", removedItems);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
                    <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/jquery-ui.min.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Cart.css">  
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <title>Nanobit</title>
    </head>
    <body>
        <c:if test="${!empty removedItems}">
            <div class="message-overlay">
                <div class="message message-warning">
                    <span class="message-title">Attenzione!</span>
                    <span class="message-text">Alcuni articoli presenti nel tuo carrello non sono più disponibili e sono stati rimossi</span>
                    <button class="message-close">Chiudi</button>
                </div>
            </div>
        </c:if>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
                <div class="content">
                    <div class="content-box">
                        <span class="title">Carrello</span>
                        <c:if test="${!empty removedItems}">
                            <div class="message message-info">
                                <p class="message-title">Articoli rimossi</p>
                                <ul>
                                    <c:forEach items="${removedItems}" var="currentItem">
                                        <li>${currentItem.title}</li>
                                    </c:forEach>
                                </ul>
                                <button class="message-close">Chiudi</button>
                            </div>
                        </c:if>
                        
                        <c:set var="total" value="0"></c:set>
                        
                        <c:forEach items="${cartEntries}" var="currentAggregateEntry">
                            <c:set var="shopItem" value="${currentAggregateEntry.shopItem}"></c:set>
                            <c:set var="cartEntry" value="${currentAggregateEntry.cartEntry}"></c:set>
                            <c:set var="total" value="${total + shopItem.price * cartEntry.quantity}"></c:set>
                        </c:forEach>
                        
                        <%-- CART FOOTER --%>
                        <div class="cart-footer">
                            <span class="item-title" style="margin-right: 10px;">Totale:</span>
                            <span class="item-price">${total}€</span>
                        </div>

                        <div class="cart-footer">
                            <a class="border-link-button" href="${contextPath}${OrderOptionsController.URL}">Procedi all'acquisto</a>
                        </div>
                        
                        <div class="spacer"></div>
                        <div class="cart-entries">
                            <%-- CART HEADER --%>
                            <div class="cart-header">
                                <span class="header-quantity">Quantità</span>
                                <span class="header-price">Prezzo</span>
                            </div>
                                              
                            <c:forEach items="${cartEntries}" var="currentAggregateEntry">
                                <c:set var="itemEntity" value="${currentAggregateEntry.item}"></c:set>
                                <c:set var="itemImage" value="${currentAggregateEntry.image}"></c:set>
                                <c:set var="shop" value="${currentAggregateEntry.shop}"></c:set>
                                <c:set var="shopItem" value="${currentAggregateEntry.shopItem}"></c:set>
                                <c:set var="cartEntry" value="${currentAggregateEntry.cartEntry}"></c:set>
                                
                                <div class="cart-entry content-box-inner">
                                    <img src="${contextPath}${StaticContentController.URL}?${StaticContentPreprocessor.RESOURCE_PARAM}=${itemImage.filename}" class="item-image"></img>
                                    <div class="item-info">
                                        <a href="${contextPath}${ItemController.URL}?${ItemPreprocessor.ITEM_ID_PARAM}=${itemEntity.itemId}"><p class="item-title">${itemEntity.title}</p></a>
                                        <form class="quantity-form" action="${contextPath}${UpdateCartItemQuantityController.URL}?${UpdateCartItemQuantityPreprocessor.SHOP_ITEM_ID_PARAM}=${shopItem.shopItemId}" method="post">
                                            <span>Qt.</span><input name="${UpdateCartItemQuantityPreprocessor.QUANTITY_PARAM}" class="item-quantity" value="${cartEntry.quantity}" size="1"/><button type="submit" class="button-quantity">Modifica</button>
                                        </form>
                                        <form class="remove-form" action="${contextPath}${UpdateCartItemQuantityController.URL}?${UpdateCartItemQuantityPreprocessor.SHOP_ITEM_ID_PARAM}=${shopItem.shopItemId}" method="post">
                                            <%-- impostare la quantitò a 0 per rimuovere l'item dal carrello, input nascosto--%>
                                            <input name="${UpdateCartItemQuantityPreprocessor.QUANTITY_PARAM}" value="0" hidden/>
                                            <button type="submit" class="button-remove">Rimuovi articolo</button>
                                        </form>
                                    
                                        <p class="item-price">${shopItem.price * cartEntry.quantity}€</p> <%-- il prezzo è mostrato è: prezzo per unitò * quantità --%>
                                    </div>
                                </div>
                                
                            </c:forEach>
                           
                            <%-- CART FOOTER --%>
                            <div class="cart-footer">
                                <span class="item-title" style="margin-right: 10px;">Totale:</span>
                                <span class="item-price">${total}€</span>
                            </div>
                            
                            <div class="cart-footer">
                                <a class="border-link-button" href="${contextPath}${OrderOptionsController.URL}">Procedi all'acquisto</a>
                            </div>
                            
                        </div>
                    </div>
                </div>
            <%@include file="/WEB-INF/jspf/Footer.jspf" %>
            <!-- Script -->
            <script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>
            <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" crossorigin="anonymous"></script>
            <script src="${contextPath}/js/util.js"></script> 
            <script src="${contextPath}/js/TopBar.js"></script>
            <script src="${contextPath}/js/util.js"></script>
        </div>
    </body>
</html>
