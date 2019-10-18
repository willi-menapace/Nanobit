<%--
    Document    : AddShop
    Author      : Willi Menapace
    Desciption  : Pagina utilizzata dal negozio per modificare lo stato di un ordine
    Requirements: Base.css, util.js
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.view.UpdateOrderStatusView"%>
<%@page import="it.webapp.applicationcontroller.UpdateOrderStatusController"%>
<%@page import="it.webapp.requestprocessor.UpdateOrderStatusPreprocessor"%>
<%@page import="it.webapp.db.entities.ShopOrderStatusType"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="error_message" value="<%= (String) request.getAttribute(UpdateOrderStatusView.MESSAGE_ATTRIBUTE) %>"></c:set>
<c:set var="shop_order_id" value="<%= request.getAttribute(UpdateOrderStatusView.SHOP_ORDER_ID_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Aggiorna stato ordine | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
    </head>
    <body>
        <c:if test="${not empty error_message}">
            <div class="message-overlay">
                <div class="message message-info">
                    <span class="message-title">Attenzione!</span>
                    <span class="message-text">${error_message}</span>
                    <button class="message-close">Chiudi</button>
                </div>
            </div>
        </c:if>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="content-box content-box-middle main">
                    <span class="title">Modifica stato ordine</span>
                    <p>Completa il seguente modulo per segnalare l'avanzamento dell'ordine.</p>
                    
                    <div class="spacer"></div>
                    <form method="POST" action="${contextPath}${UpdateOrderStatusController.URL}" enctype="multipart/form-data">
                        <div class="info">
                            <p class="info-label">Nuovo stato</p>
                            <select name="${UpdateOrderStatusPreprocessor.ORDER_STATUS_TYPE_ID_PARAM}" required>
                                <c:forEach items="${ShopOrderStatusType.values()}" var="currentStatus">
                                    <option value="${currentStatus.id}">${currentStatus.description}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <input type="hidden" name="${UpdateOrderStatusPreprocessor.SHOP_ORDER_ID_PARAM}" value='${shop_order_id}'/>


                        <div class="submit-wrap">
                            <button type="submit" class="link-button">Conferma</button>
                        </div>
                    </form>
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
</html>
