<%--
    Document     : OrderOptions.jsp
    Author       : Alessio Paternoster
    Desciption   : Pagina che mostra il riepilogo dell'ordine e chiede gli ultimi dettagli per l'ordine
    Requirements : Base.css, OrderOptions.css, util.js, OrderOptions.js
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.view.OrderOptionsView" %>
<%@page import="it.webapp.applicationcontroller.PlaceOrderController" %>
<%@page import="it.webapp.applicationcontroller.HomepageController" %>
<%@page import="it.webapp.applicationcontroller.ItemController" %>
<%@page import="it.webapp.applicationcontroller.StaticContentController" %>
<%@page import="it.webapp.applicationcontroller.ShopController" %>
<%@page import="it.webapp.requestprocessor.PlaceOrderPreprocessor" %>
<%@page import="it.webapp.requestprocessor.StaticContentPreprocessor" %>
<%@page import="it.webapp.requestprocessor.ItemPreprocessor" %>
<%@page import="it.webapp.requestprocessor.ShopPreprocessor" %>
<%@page import="it.webapp.db.entities.AddressEntity" %>
<%@page import="it.webapp.db.entities.UserEntity"%>
<%@page import="it.webapp.db.entities.CartEntryAggregateInfo" %>
<%@page import="it.webapp.db.entities.ItemEntity" %>
<%@page import="it.webapp.db.entities.ShopShipmentTypeEntity"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map" %>
<%@page import="java.util.List" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="removedItems" value="<%= (List<ItemEntity>) request.getAttribute(OrderOptionsView.REMOVED_ITEMS_ATTRIBUTE) %>"></c:set>
<c:set var="userAddress" value="<%= (AddressEntity) request.getAttribute(OrderOptionsView.USER_ADDRESS_ATTRIBUTE) %>"></c:set>
<c:set var="shopCartAggregateInfoMap" value="<%= (Map<Integer, List<CartEntryAggregateInfo>>) request.getAttribute(OrderOptionsView.SHOP_AGGREGATE_CART_ENTRIES_MAP_ATTRIBUTE) %>"></c:set>
<c:set var="shopShipmentTypesMap" value="<%= (Map<Integer, List<ShopShipmentTypeEntity>>) request.getAttribute(OrderOptionsView.SHOP_SHIPMENT_TYPES_MAP_ATTRIBUTE) %>"></c:set>
<c:set var="shopUsersMap" value="<%= (Map<Integer, UserEntity>) request.getAttribute(OrderOptionsView.SHOP_USERS_MAP_ATTRIBUTE) %>"></c:set>

<%
    Map<Integer, List<CartEntryAggregateInfo>> shopCartAggregateInfoMap = (Map<Integer, List<CartEntryAggregateInfo>>) request.getAttribute(OrderOptionsView.SHOP_AGGREGATE_CART_ENTRIES_MAP_ATTRIBUTE);
    Iterator shopCartAggregateInfoMapIterator = shopCartAggregateInfoMap.entrySet().iterator();
    BigDecimal orderTotalPriceWithoutShipment = BigDecimal.ZERO;
    
    /* Calcolo il costo totale di tutti gli item compresi nell'ordine, esclusi i costi di spedizione */
    while(shopCartAggregateInfoMapIterator.hasNext()){
        Map.Entry<Integer, List<CartEntryAggregateInfo>> shopCartAggregateInfoMapEntry = (Map.Entry<Integer, List<CartEntryAggregateInfo>>) shopCartAggregateInfoMapIterator.next();
        List<CartEntryAggregateInfo> shopCartAggregateInfoEntries = shopCartAggregateInfoMapEntry.getValue();
        for(CartEntryAggregateInfo cartEntryAggregateInfo : shopCartAggregateInfoEntries){
            int itemQuantity = cartEntryAggregateInfo.getCartEntry().getQuantity();
            BigDecimal itemPrice = cartEntryAggregateInfo.getShopItem().getPrice();
            BigDecimal itemCost = itemPrice.multiply(new BigDecimal(itemQuantity));
            orderTotalPriceWithoutShipment = orderTotalPriceWithoutShipment.add(itemCost);
        }
    }
    
    /* Rendo disponibile il costo totale nella pagina */
    pageContext.setAttribute("orderCostNoShipment", orderTotalPriceWithoutShipment);
%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Riepilogo Ordine | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/OrderOptions.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/OrderInfo.css">
    </head>
    <body>
        <c:if test="${not empty removedItems}">
            <div class="message-overlay">
                <div class="message message-warning">
                    <span class="message-title">Attenzione !</span>
                    <span class="message-text">Alcuni articoli presenti nel tuo ordine non sono più disponibili e sono stati rimossi.</span>
                    <button class="message-close">Chiudi</button>
                </div>
            </div>
        </c:if>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <c:if test="${not empty removedItems}">
                    <div class="message message-info">
                        <p class="message-title">Articoli rimossi dal tuo ordine:</p>
                        <ul>
                            <c:forEach items="${removedItems}" var="removedItem">
                                <li>${removedItem.title}</li>
                            </c:forEach>
                        </ul>
                        <button class="message-close">Chiudi</button>
                    </div>
                </c:if>
                <form method="POST" action="${contextPath}${PlaceOrderController.URL}" id="order-info-form">
                    <div class="content-box">
                        <span class="title">Riepilogo dell'ordine</span>
                        <c:forEach items="${shopUsersMap}" var="shopUsersMapElement">
                            <c:set var="shopUser" value="${shopUsersMapElement.value}"></c:set>
                            <c:set var="shopCartAggregateInfos" value="${shopCartAggregateInfoMap[shopUser.userId]}"></c:set>
                            <c:set var="shopShipmentTypes" value="${shopShipmentTypesMap[shopUser.userId]}"></c:set>
                            <div class="info">
                                <div class="info-noborder">
                                    <p class="info-label">Venditore:</p>
                                    <p class="info-value"><a class="seller-link" href="${contextPath}${ShopController.URL}?${ShopPreprocessor.SHOP_ID_PARAM}=${shopUser.userId}">${shopUser.username}</a></p><br>
                                
                                    <input type="hidden" name="${PlaceOrderPreprocessor.SHOP_IDS_PARAM}" value="${shopUser.userId}"/>
                                </div>
                                <div class="info-noborder">
                                    <p class="info-label">Articoli:</p> 
                                    <div class="info-value">
                                        <c:forEach items="${shopCartAggregateInfos}" var="cartAggregateInfo">
                                            <c:set var="cartEntryItem" value="${cartAggregateInfo.item}"></c:set>
                                            <c:set var="cartEntryResource" value="${cartAggregateInfo.image}"></c:set>
                                            <c:set var="cartEntryItemQuantity" value="${cartAggregateInfo.cartEntry.quantity}"></c:set>
                                            <c:set var="cartEntryPrice" value="${cartAggregateInfo.shopItem.price * cartEntryItemQuantity}"></c:set>
                                            <div class="content-box-inner order-info-item-grid-container">
                                                <div class=" area-title">
                                                    <a href="${contextPath}${ItemController.URL}?${ItemPreprocessor.ITEM_ID_PARAM}=${cartEntryItem.itemId}">
                                                        <p class="title2">${cartEntryItem.title}</p>
                                                    </a>
                                                </div>
                                                <div class="area-image">
                                                    <c:choose>
                                                        <c:when test="${not empty cartEntryResource && not empty cartEntryResource.filename}">
                                                            <img src="${contextPath}${StaticContentController.URL}?${StaticContentPreprocessor.RESOURCE_PARAM}=${cartEntryResource.filename}" >
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${contextPath}/resource/Item/default.jpg" >
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="area-info">
                                                    <div>
                                                        <p>
                                                            <span class="info-label">Qt. </span>
                                                            <span>${cartEntryItemQuantity}</span>
                                                        </p>
                                                        <p>
                                                            <span class="info-label">Prezzo: </span>
                                                            <span>${cartEntryPrice} &euro;</span>
                                                        </p>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div> 
                                </div>
                                <div class="info-noborder">
                                    <p class="info-label">Informazioni sulla spedizione:</p>
                                    <div class="info-value">
                                        <span>Seleziona il tipo di spedizione per questo venditore: </span>
                                        <div class="dropdown-menu" id="item-results-sort-dropdown">
                                            <span class="selected dropdown-arrow">
                                                <span class="value"></span>
                                                -- seleziona --
                                                <input type="hidden" value="0"/>
                                            </span>
                                            <ul class="dropdown-menu-list">
                                                <c:forEach items="${shopShipmentTypes}" var="shopShipmentType">
                                                    <c:set var="shipmentType" value="${shopShipmentType.shipmentType}"></c:set>
                                                    <li>
                                                        <span class="value">${shipmentType.id}</span>
                                                        <c:choose>
                                                            <c:when test="${shopShipmentType.price gt 0}">
                                                                ${shipmentType.shipmentDescription} (${shopShipmentType.price} &euro;)
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${shipmentType.shipmentDescription}
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <input type="hidden" value="${shopShipmentType.price}"/>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                        <input type="hidden" value="0" name="shipment-price" disabled />
                                        <input type="hidden" value="" name="${PlaceOrderPreprocessor.SHIPMENT_TYPE_IDS_PARAM}" />
                                        <label class="field-error">*Seleziona un metodo di spedizione</label>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="content-box">
                        <span class="title">Informazioni sul pagamento</span>
                        <div class="info area-payment-info">
                            <div class="info-noborder">
                                <p class="info-label">Intestatario carta:</p> 
                                <input type="text" class="input-big" name="${PlaceOrderPreprocessor.CC_OWNER_PARAM}" placeholder="Mario Rossi" required/>
                            </div>
                            <div class="info-noborder" id="credit-card-expiration-date">
                                <p class="info-label">Data di scadenza (YYYY / MM):</p> 
                                <input type="text" class="input-little" name="${PlaceOrderPreprocessor.CC_EXPIRATION_YEAR_PARAM}" placeholder="2015" required/>
                                <label> / </label>
                                <input type="text" class="input-little" name="${PlaceOrderPreprocessor.CC_EXPIRATION_MONTH_PARAM}" placeholder="06" required/>
                                <label class="field-error">*Campo errato</label>
                            </div>
                            <div class="info-noborder" id="credit-card-number">
                                <p class="info-label">Numero di carta:</p> 
                                <input type="number" class="input-big" name="${PlaceOrderPreprocessor.CC_NUMBER_PARAM}" placeholder="567432450985" required/>
                                <label class="field-error">*Campo errato</label>
                            </div>
                            <div class="info-noborder" id="credit-card-cvv">
                                <p class="info-label">Codice di sicurezza (CVV):</p> 
                                <input type="number" name="${PlaceOrderPreprocessor.CC_SECURITY_CODE_PARAM}" placeholder="432" required/>
                                <label class="field-error">*Campo errato</label>
                            </div>
                        </div>
                    </div>
                    <%-- Box nascosto per i dati sull'indirizzo dell'utente che sta facendo l'ordine --%>
                    <div name="user-address-info">
                        <input type="hidden" name="${PlaceOrderPreprocessor.STREET_NAME_PARAM}" value="${userAddress.street}"/>
                        <input type="hidden" name="${PlaceOrderPreprocessor.STREET_NUMBER_PARAM}" value="${userAddress.streetNumber}"/>
                        <input type="hidden" name="${PlaceOrderPreprocessor.CITY_PARAM}" value="${userAddress.city}"/>
                        <input type="hidden" name="${PlaceOrderPreprocessor.DISTRICT_PARAM}" value="${userAddress.district}"/>
                        <input type="hidden" name="${PlaceOrderPreprocessor.ZIP_PARAM}" value="${userAddress.zip}"/>
                        <input type="hidden" name="${PlaceOrderPreprocessor.COUNTRY_PARAM}" value="${userAddress.country}"/>
                    </div>
                    <div class="content-box">
                        <div class="area-end-order">
                            <div>
                                <p class="info-label">Totale da pagare:</p> 
                                <p class="price" id="total-order-cost">${orderCostNoShipment} &euro;</p>
                                <button type="submit">Acquista</button>
                                <button href="${contextPath}${HomepageController.URL}">Annulla</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <%@include file="/WEB-INF/jspf/Footer.jspf" %>
        </div>
    </body>
    <!-- Script -->
    <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/TopBar.js"></script>
    <script src="${contextPath}/js/util.js"></script>
    <script src="${contextPath}/js/OrderOptions.js"></script>
    <script>
        var creditCardExpirationMonthParam = "${PlaceOrderPreprocessor.CC_EXPIRATION_MONTH_PARAM}";
        var creditCardExpirationYearParam = "${PlaceOrderPreprocessor.CC_EXPIRATION_YEAR_PARAM}";
        var creditCardNumberParam = "${PlaceOrderPreprocessor.CC_NUMBER_PARAM}";
        var creditCardCVVParam = "${PlaceOrderPreprocessor.CC_SECURITY_CODE_PARAM}";
        var shipmentIDInputName = "${PlaceOrderPreprocessor.SHIPMENT_TYPE_IDS_PARAM}";
    </script>
</html>
