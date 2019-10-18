<%--
    Document   : Shop.jsp
    Author     : Daniele Giuliani, Willi Menapace, Alessio Paternoster
    Desciption : Pagina che rappresenta la pagina di un determinato negozio
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="it.webapp.view.ShopView"%>
<%@page import="it.webapp.db.entities.AddressEntity"%>
<%@page import="it.webapp.db.entities.ShopReviewAggregateInfo"%>
<%@page import="it.webapp.db.entities.ShopEntity"%>
<%@page import="it.webapp.db.entities.ResourceEntity"%>
<%@page import="it.webapp.db.entities.UserEntity"%>
<%@page import="it.webapp.applicationcontroller.AuthenticationInfo"%>
<%@page import="it.webapp.applicationcontroller.ShopReviewController"%>
<%@page import="it.webapp.applicationcontroller.UpdateShopFormController"%>
<%@page import="it.webapp.applicationcontroller.UpdateShopShipmentTypeFormController"%>
<%@page import="it.webapp.applicationcontroller.StaticContentController"%>
<%@page import="it.webapp.requestprocessor.StaticContentPreprocessor"%>
<%@page import="it.webapp.requestprocessor.ShopReviewPreprocessor"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="shopEntity" value="<%= (ShopEntity) request.getAttribute(ShopView.SHOP_ATTRIBUTE) %>"></c:set>
<c:set var="shopAddress" value="<%= (AddressEntity) request.getAttribute(ShopView.ADDRESS_ATTRIBUTE) %>"></c:set>
<c:set var="shopReviewsAggregateInfo" value="<%= (ShopReviewAggregateInfo) request.getAttribute(ShopView.RATING_AGGREGATE_ATTRIBUTE) %>"></c:set>
<c:set var="googleMapsAPIKey" value='<%= (String) getServletContext().getInitParameter("google_api_map_key") %>'></c:set>

<% 
    List<ResourceEntity> images = (List<ResourceEntity>) request.getAttribute(ShopView.IMAGES_ATTRIBUTE);
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
    
    /* Controllo se lo shop è quello dell'utente oppure no */
    boolean isThatShopTheUserShop = false;
    UserEntity shopUser = (UserEntity) request.getAttribute(ShopView.USER_ATTRIBUTE);
    AuthenticationInfo authenticatedUser = (AuthenticationInfo) request.getSession().getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE); 
    if(authenticatedUser != null && authenticatedUser.isShop() && shopUser != null && authenticatedUser.getUserId() == shopUser.getUserId()){
        isThatShopTheUserShop = true;
    }
    
    /* Rendo disponibili le informazioni nella pagina */
    pageContext.setAttribute("shopImageURIs", imageURIs);
    pageContext.setAttribute("shopUser", shopUser);
    pageContext.setAttribute("isThatShopTheUserShop", isThatShopTheUserShop);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Info Negozio | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Shop.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="content-box">
                    <div class="shop-title-header">
                        <span class="title">Negozio ${shopUser.username}</span>
                        <c:if test="${isThatShopTheUserShop == true}">
                            <div>
                                <div>
                                    <a class="blue-border-link-button" href="${contextPath}${UpdateShopFormController.URL}">Modifica dati negozio</a>
                                </div>
                                <div>
                                    <a class="blue-border-link-button" href="${contextPath}${UpdateShopShipmentTypeFormController.URL}">Modifica costi spedizioni</a>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="shop-grid-container">
                        <div class="shop-images-container">
                            <div>
                                <div class="shop-image-slideshow">
                                    <c:forEach items="${shopImageURIs}" var="currentImageURI">
                                        <img class="slideshow" src="${contextPath}${currentImageURI}">
                                    </c:forEach>
                                </div>
                                <c:if test="${fn:length(shopImageURIs) gt 1}">
                                    <div class="shop-image-slideshow-nav">
                                        <button class="slideshow-nav-previous">Precedente</button>
                                        <div class="slideshow-nav-button-separator"></div>
                                        <button class="slideshow-nav-next">Successiva</button>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="shop-info-container">
                            <div class="info-noborder shop-review-info" >
                                <p class="info-label">Popolarit&agrave;</p>
                                <div>
                                    <ul class="review-stars" id="shop-review-stars-container"></ul>
                                    <c:choose>
                                        <c:when test="${shopReviewsAggregateInfo.ratingsCount == 1}">
                                            <span>(1 recensione)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>(${shopReviewsAggregateInfo.ratingsCount} recensioni)</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Descrizione</p>
                                <c:choose>
                                    <c:when test="${not empty shopEntity.description}">
                                        <p class="info-value">${shopEntity.description}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="info-value">Nessuna descrizione disponibile.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Partita IVA</p>
                                <c:choose>
                                    <c:when test="${not empty shopEntity.vatNumber}">
                                        <p class="info-value">${shopEntity.vatNumber}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="info-value">Partita IVA non disponibile.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Telefono</p>
                                <c:choose>
                                    <c:when test="${not empty shopEntity.phoneNumber}">
                                        <p class="info-value">${shopEntity.phoneNumber}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="info-value">Numero di telefono non disponibile.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Aperto dal</p>
                                <c:choose>
                                    <c:when test="${not empty shopEntity.upgradeDate}">
                                        <p class="info-value">${shopEntity.upgradeDate}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="info-value">La data di apertura del negozio non è disponibile.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Indirizzo</p>
                                <c:choose>
                                    <c:when test="${not empty shopAddress}">
                                        <p class="info-value">${shopAddress.street}, ${shopAddress.streetNumber}</p>
                                        <p class="info-value">${shopAddress.zip} ${shopAddress.city} (${shopAddress.district}), ${shopAddress.country}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="info-value">Indirizzo del negozio non disponibile.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="info-noborder">
                                <p class="info-label">Posizione</p>
                                <div id="google-position-map"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="shop-reviews-container content-box">
                    <span class="title">Recensioni</span>
                    <div class="reviews" id="shop-reviews-container">
                        <c:choose>
                            <c:when test="${shopReviewsAggregateInfo.ratingsCount == 0}">
                                <div class="spacer"></div>
                                <span>Non ci sono recensioni disponibili per questo negozio.</span>
                            </c:when>
                        </c:choose>                            
                    </div>
                    <c:if test="${shopReviewsAggregateInfo.ratingsCount > 0}">
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
    <script src="${contextPath}/js/util.js"></script>
    <script src="${contextPath}/js/slideshow.js"></script>
    <script>
        var shopID = ${shopEntity.shopId};
        var shopRating = ${shopReviewsAggregateInfo.averageRating.rating};
        var shopReviewsInfo = {
            isEmpty : ${shopReviewsAggregateInfo.ratingsCount == 0},
            reviewsCount : ${shopReviewsAggregateInfo.ratingsCount}
        };
        var shopReviewControllerInfo = {
            controllerURL : "${contextPath}${ShopReviewController.URL}",
            shopIDParam : "${ShopReviewPreprocessor.SHOP_ID_PARAM}",
            offsetParam : "${ShopReviewPreprocessor.OFFSET_PARAM}",
            countParam : "${ShopReviewPreprocessor.COUNT_PARAM}"
        };
        var shopAddressInfo = {
            LatCoordinate : ${shopAddress.latitude},
            LngCoordinate : ${shopAddress.longitude}
        };
    </script>
    <script src="${contextPath}/js/Shop.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=${googleMapsAPIKey}&callback=googleMapsEmbedded.init" async defer></script>
</html>