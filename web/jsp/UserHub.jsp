<%--
    Document    : UserHub
    Author      : Daniele Giuliani, Alessio Paternoster, Willi Menapace
    Desciption  : Pagina principale dell'utente dove visualizza i vari menù (es. ordini, modifica dati, etc.)
    Requirements: Base.css, UserHub.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.AuthenticationInfo" %>
<%@page import="it.webapp.applicationcontroller.CartController"%>
<%@page import="it.webapp.applicationcontroller.UserOrdersBaseController"%>
<%@page import="it.webapp.applicationcontroller.NotificationBaseController"%>
<%@page import="it.webapp.applicationcontroller.UserIssueBaseController"%>
<%@page import="it.webapp.applicationcontroller.UserProfileController"%>
<%@page import="it.webapp.applicationcontroller.ShopController"%>
<%@page import="it.webapp.applicationcontroller.ShopIssueBaseController"%>
<%@page import="it.webapp.applicationcontroller.ShopOrdersBaseController"%>
<%@page import="it.webapp.applicationcontroller.AddShopFormController"%>
<%@page import="it.webapp.applicationcontroller.AdminIssueBaseController"%>
<%@page import="it.webapp.applicationcontroller.LogoutController"%>
<%@page import="it.webapp.applicationcontroller.HomepageController"%>
<%@page import="it.webapp.requestprocessor.ShopPreprocessor"%>
<%@page import="it.webapp.requestprocessor.LogoutPreprocessor"%>

<%
    boolean isShop = false;
    boolean isAdmin = false;
    int userId = 0;

    /* Preparo le informazioni per controllare poi il tipo di utente che ha effettuato l'accesso */
    AuthenticationInfo userAuthInfoUserHub = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
    if(userAuthInfoUserHub != null && userAuthInfoUserHub.isAuthenticated()){
        isShop = userAuthInfoUserHub.isShop();
        isAdmin = userAuthInfoUserHub.isAdmin();
        userId = userAuthInfoUserHub.getUserId();
    }

    /* Rendo disponibili le informazioni nella pagina */
    pageContext.setAttribute("isShop", isShop);
    pageContext.setAttribute("isAdmin", isAdmin);
    pageContext.setAttribute("userId", userId);
%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Account | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/UserHub.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/jquery-ui.min.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="userhub">
                    <span class="title">Il mio account</span>
                    <ul class="menu-grid">
                        <a href="${contextPath}${CartController.URL}"><li class="icon-cart">Carrello</li></a>
                        <a href="${contextPath}${UserOrdersBaseController.URL}"><li class="icon-box">I miei ordini</li></a>
                        <a href="${contextPath}${NotificationBaseController.URL}"><li class="icon-info">Notifiche</li></a>
                        <a href="${contextPath}${UserIssueBaseController.URL}"><li class="icon-warning">Segnalazioni</li></a>
                        <a href="${contextPath}${UserProfileController.URL}"><li class="icon-user">Il mio profilo</li></a>
                        <c:choose>
                            <c:when test="${isShop}"> <%-- Se l'utente ha un negozio --%>
                                <a href="${contextPath}${ShopController.URL}?${ShopPreprocessor.SHOP_ID_PARAM}=${userId}">
                                    <li class="icon-shop">Il mio negozio</li>
                                </a>
                                <a href="${contextPath}${ShopIssueBaseController.URL}">
                                    <li class="icon-alert">Segnalazioni negozio</li>
                                </a>
                                <a href="${contextPath}${ShopOrdersBaseController.URL}">
                                    <li class="icon-list">Ordini negozio</li>
                                </a>
                            </c:when>
                            <c:otherwise> <%-- Se è un utente normale, può aprire un negozio --%>
                                <a href="${contextPath}${AddShopFormController.URL}">
                                    <li class="icon-shop">Crea un negozio</li>
                                </a>
                            </c:otherwise>
                        </c:choose>
                        <c:choose> <%-- Se l'utente e' amministratore --%>
                            <c:when test="${isAdmin}">
                                <a href="${contextPath}${AdminIssueBaseController.URL}">
                                    <li class="icon-shop">Valuta segnalazioni</li>
                                </a>
                            </c:when>
                        </c:choose>
                        <a href="${contextPath}${LogoutController.URL}?${LogoutPreprocessor.REDIRECT_PARAMETER}=${contextPath}${HomepageController.URL}">
                            <li class="icon-logout">Esci</li></a>
                    </ul>
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
