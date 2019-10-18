<%--
    Document    : OrderInfo
    Author      : Daniele Giuliani, Alessio Paternoster, Willi Menapace
    Desciption  : Pagina utilizzata dall'utente per visualizzare un ordine presente o passato
    Requirements: Base.css, Item.css, OrderInfo.css, util.js, LeaveFeedback.js

    IMPORTANTE:
        - non modificare l'ordine dei vari elementi ne le classi a loro assegnate, altrimenti il javascript che
          esegue le funzionalità si rompe! E' comunque possibile modificare tutto il CSS che si vuole
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="it.webapp.db.entities.ResourceEntity"%>
<%@page import="it.webapp.db.entities.ResourceEntity"%>
<%@page import="it.webapp.db.entities.ShopReviewAggregateInfo"%>
<%@page import="it.webapp.view.ShopView"%>
<%@page import="it.webapp.db.entities.ShopEntity"%>
<%@page import="it.webapp.db.entities.UserEntity"%>
<%@page import="it.webapp.applicationcontroller.StaticContentController"%>
<%@page import="it.webapp.requestprocessor.StaticContentPreprocessor"%>
<%@page import="it.webapp.db.entities.ShopOrderStatusEntity"%>
<%@page import="it.webapp.db.entities.ShopOrderItemAggregateInfo"%>
<%@page import="it.webapp.db.entities.ShopReviewEntity"%>
<%@page import="it.webapp.db.entities.ShopOrderEntity"%>
<%@page import="it.webapp.db.entities.AddressEntity"%>
<%@page import="it.webapp.db.entities.ShopOrderAggregateInfo"%>
<%@page import="it.webapp.view.OrderInfoView"%>
<%@page import="java.util.List"%>
<%@page import="it.webapp.applicationcontroller.ItemController"%>
<%@page import="it.webapp.applicationcontroller.ShopController"%>
<%@page import="it.webapp.applicationcontroller.AddIssueFormController"%>
<%@page import="it.webapp.requestprocessor.ItemPreprocessor"%>
<%@page import="it.webapp.requestprocessor.ShopPreprocessor"%>
<%@page import="it.webapp.requestprocessor.AddIssueFormPreprocessor"%>

<%@page import="it.webapp.applicationcontroller.AddItemReviewController"%>
<%@page import="it.webapp.requestprocessor.AddItemReviewPreprocessor"%>
<%@page import="it.webapp.applicationcontroller.AddShopReviewController"%>
<%@page import="it.webapp.requestprocessor.AddShopReviewPreprocessor"%>

<%
    ShopOrderAggregateInfo aggregateInfo = (ShopOrderAggregateInfo) request.getAttribute(OrderInfoView.ORDER_INFO_ATTRIBUTE);
    int userId = (int) request.getAttribute(OrderInfoView.USER_ID_ATTRIBUTE);

    UserEntity user = aggregateInfo.getUser();
    AddressEntity address = aggregateInfo.getShipmentAddress();
    ShopOrderEntity shopOrder = aggregateInfo.getShopOrder();
    ShopReviewEntity shopReview = aggregateInfo.getShopReview();

    List<ShopOrderStatusEntity> orderStatuses = aggregateInfo.getOrderStatuses();
    List<ShopOrderItemAggregateInfo> shopOrderItemsAggregate = aggregateInfo.getShopOrderItemsAggregate();

    /* Rendo disponibili le informazioni nella pagina */
    pageContext.setAttribute("address", address);
    pageContext.setAttribute("user", user);
    pageContext.setAttribute("shopOrder", shopOrder);
    pageContext.setAttribute("shopReview", shopReview);
    pageContext.setAttribute("orderStatuses", orderStatuses);
    pageContext.setAttribute("shopOrderItemsAggregate", shopOrderItemsAggregate);
    pageContext.setAttribute("userId", userId);
%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ordine ${shopOrder.shopOrderId} | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Item.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/OrderInfo.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/jquery-ui.min.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="content-box">
                    <span class="title">Dettagli dell'ordine</span>
                    <div class="spacer"></div>

                    <div class="info">
                        <div class="orderinfo-grid-container1">
                            <div class="info-noborder">
                                <p class="info-label">Ordine</p>
                                <p class="info-value">${shopOrder.shopOrderId}</p>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Data</p>
                                <p class="info-value">${shopOrder.date}</p>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Spedizione</p>
                                <p class="info-value">
                                    <span>${shopOrder.shipmentType.shipmentDescription}</span>
                                    <span>(${shopOrder.shipmentPrice}&euro;)<span>
                                </p>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Indirizzo consegna</p>
                                <div class="info-value">
                                    <span>${address.street}, ${address.streetNumber}</span><br>
                                    <span>${address.zip} ${address.city} ${address.district}, ${address.country}</span>
                                </div>
                            </div>
                            <c:if test="${userId ne shopOrder.shopId}"> <%-- visualizzo gli ordini effettuati --%>
                                <div class="info-noborder">
                                    <p class="info-label">Venditore</p>
                                    <div class="info-value">
                                        <a class="seller-link" href="${contextPath}${ShopController.URL}?${ShopPreprocessor.SHOP_ID_PARAM}=${user.userId}"><span>${user.username}</span></a><br>
                                    </div>
                                </div>
                            </c:if>
                        </div>

                        <%-- La classe "attiva" viene impostata solamente per l'ultimo item della lista --%>
                        <div class="info-noborder">
                            <p class="info-label">Stato ordine</p>
                            <ul class="order-status">
                                <c:forEach items="${orderStatuses}" var="currentStatus">
                                    <li class="status-entry">
                                        <span class="status-value">${currentStatus.shopOrderStatus.description}</span>
                                        <span class="status-date">${currentStatus.changeDate}</span>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>

                        <c:if test="${userId ne shopOrder.shopId}"> <%-- visualizzo gli ordini effettuati --%>
                            <div class="info-noborder">
                                <p class="info-label">Recensione Venditore</p>
                                <span class="shop-order-id" hidden>${shopOrder.shopOrderId}</span>
                                <div class="review-container content-box-inner">
                                    <div class="info-value">
                                        <c:choose>
                                            <c:when test="${empty shopReview}"> <%-- recensione mancante --%>
                                                <p>Non hai ancora lasciato alcuna recensione, se vuoi puoi farlo ora.</p>
                                                <div style="text-align: center;">
                                                    <button onclick="leaveShopFeedback()">Lascia recensione</button>
                                                </div>
                                            </c:when>
                                            <c:otherwise> <%-- recensione già lascita --%>
                                                <p>Hai già lasciato una recensione per questo venditore.</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <div class=info-noborder>
                            <p class="info-label">Articoli</p>
                            <c:forEach items="${shopOrderItemsAggregate}" var="currentItemAggregate">
                                <c:set var="shopOrderItemEntity" value="${currentItemAggregate.shopOrderItem}"></c:set>
                                <c:set var="itemEntity" value="${currentItemAggregate.item}"></c:set>
                                <c:set var="resourceEntity" value="${currentItemAggregate.image}"></c:set>
                                <c:set var="itemReviewEntity" value="${currentItemAggregate.itemReview}"></c:set>

                                <div class="content-box-inner orderinfo-grid-container2">
                                    <span class="shop-order-item-id">${shopOrderItemEntity.shopOrderItemId}</span>
                                    <a class="title2 area-title" href="${contextPath}${ItemController.URL}?${ItemPreprocessor.ITEM_ID_PARAM}=${itemEntity.itemId}"><p>${itemEntity.title}</p></a>
                                    <c:choose>
                                        <c:when test="${empty resourceEntity}">
                                            <img src="${contextPath}/resource/Item/default.jpg" class="area-image">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${contextPath}${StaticContentController.URL}?${StaticContentPreprocessor.RESOURCE_PARAM}=${resourceEntity.filename}" class="area-image">
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="area-info">
                                        <p>
                                            <span class="info-label">Qt. </span>
                                            <span>${shopOrderItemEntity.quantity}</span>
                                        </p>
                                        <p>
                                            <span class="info-label">Prezzo: </span>
                                            <span>${shopOrderItemEntity.price}&euro;</span>
                                        </p>
                                        <c:if test="${userId ne shopOrder.shopId}"> <%-- visualizzo gli ordini effettuati --%>
                                            <p>
                                                <span class="info-label">
                                                    <a class="blue-border-link-button" href="${contextPath}${AddIssueFormController.URL}?${AddIssueFormPreprocessor.SHOP_ORDER_ITEM_ID_PARAM}=${shopOrderItemEntity.shopOrderItemId}">Segnala anomalia</a>
                                                </span>
                                            </p>
                                            <c:choose>
                                                <c:when test="${empty itemReviewEntity}"> <%-- recensione mancante --%>
                                                    <p>
                                                        <span class="info-label">Feedback: </span>
                                                        <span>
                                                            <button onclick="leaveItemFeedback(this);">Lascia Feedback</button>
                                                        </span>
                                                    </p>
                                                </c:when>
                                                <c:otherwise> <%-- recensione giò lascita --%>
                                                    <p>
                                                        <span class="info-label">Feedback: </span>
                                                        <span>hai già lasciato un feedback per questo oggetto.</span>
                                                    </p>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
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
    <script src="${contextPath}/js/util.js"></script>
    <script src="${contextPath}/js/LeaveFeedback.js"></script>
    <script type="text/javascript">
    function sendItemFeedback() {
            //collecting data from form
            var shopOrderItemId = $("#shopOrderItemId").val();
            var reviewTitleAxax = $("#reviewTitleAxax").val();
            var reviewDescriptionAxax = $("#reviewDescriptionAxax").val();
            var reviewRatingAjax = $("#reviewRatingAjax").val();

            var data = {
                ${AddItemReviewPreprocessor.SHOP_ORDER_ITEM_ID_PARAM}: shopOrderItemId,
                ${AddItemReviewPreprocessor.TITLE_PARAM}: reviewTitleAxax,
                ${AddItemReviewPreprocessor.DESCRIPTION_PARAM}: reviewDescriptionAxax,
                ${AddItemReviewPreprocessor.RATING_PARAM}: reviewRatingAjax
            };

            //Performs ajax request
            $.post("${contextPath}${AddItemReviewController.URL}", data)
            .done(function(msg){
                // Se richiesa ajax positiva:
                removeFeedbackBox();
                var ids = $(".shop-order-item-id");
                ids.each(function(){
                        if($(this).html().localeCompare(shopOrderItemId) == 0){
                                //abbiamo trovato l'oggetto per cui abbiamo appena lasciato al recensione
                                var article = $(this).parent();
                                var pFeedback = article.children().last().children().last();
                                //modifico il paragrafo per inserire il messaggio
                                pFeedback.html("<span class=info-label>Feedback: </span><span>Grazie per aver fornito la tua recensione.</span>");
                        }
                });
            })
            .fail(function(xhr, status, error) {
                // Se richiesta ajax negativa:
                var target = $(".feedback");
                target.html("");
                target.append("<p class=feedback-title>Lascia feedback articolo</p>");
                target.append("<p>" + error + "</p>");
                target.append("<div class=feedback-buttons><button onclick=removeFeedbackBox()>Chiudi</button></div>");
            });

    }

    function sendShopFeedback() {
            //collecting data from form
            var shopOrderId = $("#shopOrderId").val();
            var reviewTitleAxax = $("#reviewTitleAxax").val();
            var reviewDescriptionAxax = $("#reviewDescriptionAxax").val();
            var reviewRatingAjax = $("#reviewRatingAjax").val();

            var data = {
                ${AddShopReviewPreprocessor.SHOP_ORDER_ID_PARAM}: shopOrderId,
                ${AddShopReviewPreprocessor.TITLE_PARAM}: reviewTitleAxax,
                ${AddShopReviewPreprocessor.DESCRIPTION_PARAM}: reviewDescriptionAxax,
                ${AddShopReviewPreprocessor.RATING_PARAM}: reviewRatingAjax
            };

            //Performs ajax request
            $.post("${contextPath}${AddShopReviewController.URL}", data)
            .done(function(msg){
                // Se richiesa ajax positiva:
                removeFeedbackBox();
                var id = $(".shop-order-id");
                var target = id.parent().children().last();
                target.html("<p>Grazie per aver fornito la tua recensione.</p>");
            })
            .fail(function(xhr, status, error) {
                // Se richiesta ajax negativa:
                var target = $(".feedback");
                target.html("");
                target.append("<p class=feedback-title>Lascia feedback venditore</p>");
                target.append("<p>" + error + "</p>");
                target.append("<div class=feedback-buttons><button onclick=removeFeedbackBox()>Chiudi</button></div>");
            });

    }
    </script>
</html>
