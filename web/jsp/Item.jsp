<%--
    Document     : Item.jsp
    Author       : Alessio Paternoster
    Desciption   : Pagina per la visualizzazione della scheda informativa di un certo prodotto
    Requirements : Base.css, Item.css, slideshow.js, Item.js, util.js, twbsPagination JS

    TODOS:
        - Sistemare bottone 'Aggiungi a carrello': sistemare link a controller + stile
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="it.webapp.applicationcontroller.StaticContentController"%>
<%@page import="it.webapp.applicationcontroller.ShopController"%>
<%@page import="it.webapp.applicationcontroller.AuthenticationInfo"%>
<%@page import="it.webapp.applicationcontroller.LoginFormController"%>
<%@page import="it.webapp.applicationcontroller.UpdateCartItemQuantityController"%>
<%@page import="it.webapp.applicationcontroller.ItemReviewController"%>
<%@page import="it.webapp.requestprocessor.StaticContentPreprocessor"%>
<%@page import="it.webapp.requestprocessor.LoginFormPreprocessor"%>
<%@page import="it.webapp.requestprocessor.ItemReviewPreprocessor"%>
<%@page import="it.webapp.requestprocessor.ShopPreprocessor"%>
<%@page import="it.webapp.requestprocessor.UpdateCartItemQuantityPreprocessor"%>
<%@page import="it.webapp.view.ItemView" %>
<%@page import="it.webapp.db.entities.UserEntity" %>
<%@page import="it.webapp.db.entities.ItemEntity" %>
<%@page import="it.webapp.db.entities.ItemAggregateInfo" %>
<%@page import="it.webapp.db.entities.ResourceEntity" %>
<%@page import="it.webapp.db.entities.ItemShopAvailabilityInfo" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="itemAggregateInfo" value="<%= (ItemAggregateInfo) request.getAttribute(ItemView.ITEM_ATTRIBUTE) %>"></c:set>
<c:set var="currentPageControllerURI" value='<%= (String) (request.getAttribute("javax.servlet.forward.request_uri") + "?" + request.getQueryString()) %>' ></c:set>
<c:set var="item" value="${itemAggregateInfo.item}"></c:set>
    
<%
    List<ResourceEntity> images = (List<ResourceEntity>) request.getAttribute(ItemView.IMAGES_ATTRIBUTE);
    List<String> imageURIs = new ArrayList<>();
    
    /* Preparazione della lista con i filename per tutte le immagini */
    if(images != null & !images.isEmpty()){
        for(ResourceEntity resource: images){
            String imageURI = StaticContentController.URL;
            imageURI += "?" + StaticContentPreprocessor.RESOURCE_PARAM + "=" + resource.getFilename();
            imageURIs.add(imageURI);
        }
    }else{
        imageURIs.add("/resource/Item/default.jpg");
    }
    
    List<ItemShopAvailabilityInfo> shopsAvailabilityInfo = (List<ItemShopAvailabilityInfo>) request.getAttribute(ItemView.AVAILABILTY_INFO_ATTRIBUTE);
    List<UserEntity> shopUsers = new ArrayList();
    
    /* Estraggo tutti gli shopUsers */
    for(ItemShopAvailabilityInfo shopAvailabilityInfo: shopsAvailabilityInfo){
        shopUsers.add(shopAvailabilityInfo.getShopUser());
    }
    
    /* Controllo se l'utente è autenticato oppure no */ 
    boolean isUserLogged = false;
    AuthenticationInfo userAuthenticationInfo = (AuthenticationInfo) request.getSession().getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
    if(userAuthenticationInfo != null && userAuthenticationInfo.isAuthenticated()){
        isUserLogged = true;
    }
     
    /* Rendo disponibili le informazioni nella pagina */
    pageContext.setAttribute("imageURIs", imageURIs);
    pageContext.setAttribute("shopsAvailabilityInfo", shopsAvailabilityInfo);
    pageContext.setAttribute("shopUsers", shopUsers);
    pageContext.setAttribute("isUserLogged", isUserLogged);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Info prodotto | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Item.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="content-box">
                    <span class="title">${item.title}</span>
                    <div class="item-grid-container">
                        <div class="item-images-container">
                            <div>
                                <div class="item-image-slideshow">
                                    <c:forEach items="${imageURIs}" var="imageURI">
                                        <img class="slideshow" src="${contextPath}${imageURI}">
                                    </c:forEach>
                                </div>
                                <c:if test="${fn:length(imageURIs) gt 1}">
                                    <div class="item-image-slideshow-nav">
                                        <button class="slideshow-nav-previous">Precedente</button>
                                        <div class="slideshow-nav-button-separator"></div>
                                        <button class="slideshow-nav-next">Successiva</button>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="item-info-container">
                            <div class="item-vendor-info info-noborder">
                                <p class="info-label">Venditore</p>
                                <div>
                                    <c:choose>
                                        <c:when test="${fn:length(shopUsers) gt 0}">
                                            <c:choose>
                                                <c:when test="${fn:length(shopUsers) gt 1}">
                                                    <div class="dropdown-menu">
                                                        <span class="selected dropdown-arrow">
                                                            <span class="value">${shopUsers[0].userId}</span>${shopUsers[0].username}
                                                        </span>
                                                        <ul class="dropdown-menu-list">
                                                            <c:forEach items="${shopUsers}" var="shopUser">
                                                                <li>
                                                                    <span class="value">${shopUser.userId}</span>${shopUser.username}
                                                                </li>
                                                            </c:forEach>
                                                        </ul>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span>${shopUsers[0].username}</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <ul class="review-stars" id="shop-review-stars-container"></ul>
                                            <span>(</span><span id="shop-reviews-count"></span><span>)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>Informazioni sul venditore non disponibili</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="item-product-link">
                                    <a id="shop-page-url" class="blue-border-link-button" href="">Vai alla pagina del negozio</a>
                                </div>
                            </div>
                            <div class="item-availability-info info-noborder">
                                <p class="info-label">Disponibilità</p>
                                <div>
                                    <c:choose>
                                        <c:when test="${fn:length(shopUsers) gt 0}">
                                            <span id="availability-pieces-container" class="available"></span>
                                            <span id="availability-pieces-pickup" style="display: none">(consegna a mano disponibile)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>Informazioni sulla disponibilit&agrave; non disponibili</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="item-review-info info-noborder">
                                <p class="info-label">Valutazione</p>
                                <div>
                                    <ul class="review-stars" id="item-review-stars-container"></ul>
                                    <c:choose>
                                        <c:when test="${itemAggregateInfo.reviewsCount == 1}">
                                            <span>(1 recensione)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>(${itemAggregateInfo.reviewsCount} recensioni)</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="item-description info-noborder">
                                <p class="info-label">Descrizione</p>
                                <div>
                                   <span>${item.description}</span> 
                                </div>
                            </div>
                            <div class="item-price-info info-noborder">
                                <p class="info-label">Prezzo</p>
                                <div>
                                    <c:choose>
                                        <c:when test="${fn:length(shopUsers) gt 0}">
                                            <span class="available" id="item-price-text"></span>
                                            <span class="available">&euro; </span>
                                            <c:choose>
                                                <c:when test="${isUserLogged == true}">
                                                    <a id="add-item-to-cart" class="blue-border-link-button" href="">Aggiungi al carrello</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a class="blue-border-link-button" href="${contextPath}${LoginFormController.URL}?${LoginFormPreprocessor.REDIRECT_PARAMETER}=${currentPageControllerURI}">Accedi per poter acquistare</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <span>Prezzo non disponibile. L'articolo al momento non è in vendita</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="item-reviews-container content-box">
                    <span class="title">Recensioni</span>
                    <div class="reviews" id="item-reviews-container">
                        <c:choose>
                            <c:when test="${itemAggregateInfo.reviewsCount == 0}">
                                <div class="spacer"></div>
                                <span>Non ci sono recensioni disponibili per questo prodotto.</span>
                            </c:when>
                        </c:choose>                            
                    </div>
                    <c:if test="${itemAggregateInfo.reviewsCount > 0}">
                        <nav class="reviews-pagination-nav">
                            <ul id="reviews-pagination"></ul>
                        </nav>
                    </c:if>
                </div>
            </div>
            <%@include file="/WEB-INF/jspf/Footer.jspf" %>
        </div>
    </body>
    <!-- Script -->
    <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/jquery.twbsPagination.min.js"></script>
    <script src="${contextPath}/js/TopBar.js"></script>
    <script src="${contextPath}/js/slideshow.js"></script>
    <script src="${contextPath}/js/util.js"></script>
    <script src="${contextPath}/js/Item.js"></script>
    <script>
        // Inizializzo le variabili per il file Item.js
        var defaultShopID = "${shopUsers[0].userId}";
        var updateCartItemQuantityControllerInfo = {
            URL : "${contextPath}${UpdateCartItemQuantityController.URL}",
            shopItemIdParam : "${UpdateCartItemQuantityPreprocessor.SHOP_ITEM_ID_PARAM}",
            quantityParam : "${UpdateCartItemQuantityPreprocessor.QUANTITY_PARAM}"
        };
        var shopControllerInfo = {
            URL : "${contextPath}${ShopController.URL}",
            shopIdParam : "${ShopPreprocessor.SHOP_ID_PARAM}"
        };
        var itemReviewControllerInfo = {
            URL : "${contextPath}${ItemReviewController.URL}",
            itemIDParam : "${ItemReviewPreprocessor.ITEM_ID_PARAM}",
            offsetParam : "${ItemReviewPreprocessor.OFFSET_PARAM}",
            countParam : "${ItemReviewPreprocessor.COUNT_PARAM}"
        };
        var itemReviewsInfo = {
            isEmpty : ${itemAggregateInfo.reviewsCount == 0},
            reviewsCount : ${itemAggregateInfo.reviewsCount}
        };
        var itemInfo = {
            ID : ${itemAggregateInfo.item.itemId},
            rating : ${itemAggregateInfo.averageRating.rating}
        };
        var shopsRelatedInfo = {
            <c:forEach items="${shopsAvailabilityInfo}" var="shopAvailabilityInfo">
                    "${shopAvailabilityInfo.shopUser.userId}" : {
                        ranking : ${shopAvailabilityInfo.aggregateShopRating.rating},
                        reviewsCount : ${shopAvailabilityInfo.shopReviewsCount},
                        availability : ${shopAvailabilityInfo.shopItemEntity.availability},
                        pickupFromShop : ${shopAvailabilityInfo.pickupFromShop},
                        price : ${shopAvailabilityInfo.shopItemEntity.price},
                        shopItemId : ${shopAvailabilityInfo.shopItemEntity.shopItemId}
                    },
            </c:forEach>
        };
    </script>
</html>
