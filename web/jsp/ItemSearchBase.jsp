<%--
    Document     : ItemSearchBase.jsp
    Author       : Alessio Paternoster
    Desciption   : Pagina per la visualizzazione dei risultati di ricerca
    Requirements : Base.css, ItemSearchBase.css, ItemSearchBase.js, util.js, twbs pagination JS
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.ItemSearchController" %>
<%@page import="it.webapp.applicationcontroller.StaticContentController" %>
<%@page import="it.webapp.applicationcontroller.ItemController" %>
<%@page import="it.webapp.requestprocessor.ItemSearchPreprocessor" %>
<%@page import="it.webapp.requestprocessor.StaticContentPreprocessor" %>
<%@page import="it.webapp.requestprocessor.ItemPreprocessor" %>
<%@page import="it.webapp.db.entities.Department" %>
<%@page import="it.webapp.view.ItemSearchBaseView" %>
<%@page import="it.webapp.db.dao.ItemDao.SortOrder" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="sortOrders" value="<%= (SortOrder[]) SortOrder.values() %>"></c:set>
<c:set var="itemTerm" value="<%= (String) request.getAttribute(ItemSearchBaseView.TERM_ATTRIBUTE) %>"></c:set>
<c:set var="itemDepartment" value="<%= (Department) request.getAttribute(ItemSearchBaseView.DEPARTMENT_ATTRIBUTE) %>"></c:set>
<c:set var="googleMapsAPIKey" value='<%= (String) getServletContext().getInitParameter("google_api_map_key") %>'></c:set>

<!DOCTYPE html>
<html lang="it">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Pagina risultati | Nanobit </title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/ItemSearchBase.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="filters-sidebar content-box">
                    <form id="item-result-filter-form" method="POST" action="#">
                        <div class="form-header">Filtra per ...</div>
                        <ul>
                            <li>
                                <span class="filter-label">Posizione</span>
                                <div class="item-geolocation">
                                    <div class="row">
                                        <label>
                                            <input type="radio" name="geolocation-option" value="1"/><span>Abilita</span>
                                        </label>
                                    </div>
                                    <div class="row">
                                        <label>
                                            <input type="radio" name="geolocation-option" value="0" checked/><span>Disabilita</span>
                                        </label>
                                    </div>
                                </div>
                            </li>
                            <li id="item-geolocation-map-container">
                                <span class="filter-label">Posizione del luogo di ritiro</span>
                                <div class="item-position-info">
                                    <div class="row">
                                        <div id="google-position-map"></div>
                                    </div>
                                    <div class="row">
                                        <label id="map-info-label"></label>
                                    </div>
                                    <input type="hidden" id="item-geocoding-lat" name="${ItemSearchPreprocessor.GEOFENCE_CENTER_LAT_PARAM}" value="" disabled/>
                                    <input type="hidden" id="item-geocoding-lng" name="${ItemSearchPreprocessor.GEOFENCE_CENTER_LNG_PARAM}" value="" disabled/>
                                </div>
                            </li>
                            <li id="item-geocoding-radius-container">
                                <span class="filter-label">Distanza dal luogo di ritiro</span>
                                <div class="item-distance-info">
                                    <div class="row">
                                        <label>
                                            <input type="radio" name="${ItemSearchPreprocessor.GEOFENCE_RADIUS_PARAM}" value="5" checked disabled/><span>Nel raggio di 5 Km</span>
                                        </label>
                                    </div>
                                    <div class="row">
                                        <label>
                                            <input type="radio" name="${ItemSearchPreprocessor.GEOFENCE_RADIUS_PARAM}" value="10" disabled/><span>Nel raggio di 10 Km</span>
                                        </label>
                                    </div>
                                    <div class="row">
                                        <label>
                                            <input type="radio" name="${ItemSearchPreprocessor.GEOFENCE_RADIUS_PARAM}" value="20" disabled/><span>Nel raggio di 20 Km</span>
                                        </label>
                                    </div>
                                    <div class="row">
                                        <label>
                                            <input type="radio" name="${ItemSearchPreprocessor.GEOFENCE_RADIUS_PARAM}" value="50" disabled/><span>Nel raggio di 50 Km</span>
                                        </label>
                                    </div>
                                    <div class="row">
                                        <label>
                                            <input type="radio" name="${ItemSearchPreprocessor.GEOFENCE_RADIUS_PARAM}" value="100" disabled/><span>Nel raggio di 100 Km</span>
                                        </label>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <span class="filter-label">Fascia di prezzo</span>
                                <div class="item-price-info">
                                    <table>
                                        <tr>
                                            <div class="row">
                                                <td>
                                                    <span>Da:</span>
                                                </td>
                                                <td>
                                                    <input type="number" name="${ItemSearchPreprocessor.PRICE_LOW_PARAM}" min="0">  
                                                </td>
                                                <td>
                                                    <span>&euro;</span>
                                                </td>
                                            </div>
                                        </tr>
                                        <tr>
                                            <div class="row">
                                                <td>
                                                    <span>A:</span>
                                                </td>
                                                <td>
                                                    <input type="number" name="${ItemSearchPreprocessor.PRICE_HIGH_PARAM}" min="0">  
                                                </td>
                                                <td>
                                                    <span>&euro;</span>
                                                </td>
                                            </div>
                                        </tr>                                      
                                    </table>                                   
                                    <div class="row">
                                        <label id="item-price-info">(Per disabilitare, lascia vuoti i campi di testo)</label>
                                    </div>
                                    <div class="row">
                                        <label id="item-price-info-error">Fascia di prezzo non corretta !</label>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <input class="button" type="submit" value="Applica i filtri selezionati" />
                    </form>
                </div>
                <div class="catalog-container">
                    <div class="catalog-filterbar content-box">
                        <table>
                            <tr>
                                <td class="catalog-filterbar-item">
                                    <div class="dropdown-menu" id="item-results-sort-dropdown">
                                        <span class="selected dropdown-arrow">
                                            <span class="value">${sortOrders[0].sortOrderID}</span>Ordina per ${sortOrders[0].sortOrderDescription}
                                        </span>
                                        <ul class="dropdown-menu-list">
                                            <c:forEach items="${sortOrders}" var="sortOrder">
                                                <li>
                                                    <span class="value">${sortOrder.sortOrderID}</span>Ordina per ${sortOrder.sortOrderDescription}
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </td>
                                <td class="catalog-filterbar-item">
                                    <div class="dropdown-menu" id="item-results-count-dropdown">
                                        <span class="selected dropdown-arrow"><span class="value">10</span>Mostra 10 risultati per pagina</span>
                                        <ul class="dropdown-menu-list">
                                            <li>
                                                <span class="value">10</span>Mostra 10 risultati per pagina
                                            </li>
                                            <li>
                                                <span class="value">20</span>Mostra 20 risultati per pagina
                                            </li>
                                            <li>
                                                <span class="value">50</span>Mostra 50 risultati per pagina
                                            </li>
                                            <li>
                                                <span class="value">100</span>Mostra 100 risultati per pagina
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="catalog-results" id="item-catalog-results"></div>
                    <div class="catalog-pagenav content-box">
                        <table>
	                        <tr>
	                            <td class="catalog-filterbar-item">
                                    <ul id="item-results-paginaton"></ul>
                                </td>
                            </tr>
                        </table>
                    </div>
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
    <script src="${contextPath}/js/ItemSearchBase.js"></script>
    <script>
        var defaultItemImageURI = "${contextPath}/resource/Item/default.jpg";
        var itemSearchControllerURL = "${contextPath}${ItemSearchController.URL}";
        var itemControllerInfo = {
            URL : "${contextPath}${ItemController.URL}",
            itemIDParam : "${ItemPreprocessor.ITEM_ID_PARAM}"
        };
        var staticContentControllerInfo = {
            URL : "${contextPath}${StaticContentController.URL}",
            imageParam : "${StaticContentPreprocessor.RESOURCE_PARAM}"
        }
        var initItemSearchInfo = {
            <c:choose>
                <c:when test="${not empty itemTerm}">
                    term : "${itemTerm}",
                </c:when>
                <c:otherwise>
                    term : null,
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${not empty itemDepartment}">
                    department : ${itemDepartment.ID},
                </c:when>
                <c:otherwise>
                    department : null,
                </c:otherwise>
            </c:choose>
            order : ${sortOrders[0].sortOrderID},
            count : 10
        };
        var itemSearchParam = {
            termParam: "${ItemSearchPreprocessor.TERM_PARAM}",
            departmentParam: "${ItemSearchPreprocessor.DEPARTMENT_PARAM}",
            priceLowParam: "${ItemSearchPreprocessor.PRICE_LOW_PARAM}",
            priceHighParam: "${ItemSearchPreprocessor.PRICE_HIGH_PARAM}",
            latitudeParam: "${ItemSearchPreprocessor.GEOFENCE_CENTER_LAT_PARAM}",
            longitudeParam: "${ItemSearchPreprocessor.GEOFENCE_CENTER_LNG_PARAM}",
            radiusParam: "${ItemSearchPreprocessor.GEOFENCE_RADIUS_PARAM}",
            orderParam: "${ItemSearchPreprocessor.ORDER_PARAM}",
            offsetParam: "${ItemSearchPreprocessor.OFFSET_PARAM}",
            countParam: "${ItemSearchPreprocessor.COUNT_PARAM}"
        };
    </script>
    <script src="https://maps.googleapis.com/maps/api/js?key=${googleMapsAPIKey}&callback=googleMaps.initMap" async defer></script>
</html>
