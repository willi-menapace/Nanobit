<%--
    Document    : User Orders
    Author      : Willi Menapace
    Desciption  : Pagina utilizzata dal negozio per visualizzare i propri oridini
    
    IMPORTANTE: 
        - non modificare l'ordine dei vari elementi ne le classi a loro assegnate, altrimenti il javascript che 
          esegue le funzionalità si rompe! E' comunque possibile modificare tutto il CSS che si vuole
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.StaticContentController"%>
<%@page import="it.webapp.applicationcontroller.ShopOrdersController"%>
<%@page import="it.webapp.requestprocessor.ShopOrdersPreprocessor"%>
<%@page import="it.webapp.requestprocessor.StaticContentPreprocessor"%>
<%@page import="it.webapp.db.entities.ShopOrderStatusEntity"%>
<%@page import="it.webapp.db.entities.ShopOrderItemAggregateInfo"%>
<%@page import="it.webapp.db.entities.ShopReviewEntity"%>
<%@page import="it.webapp.db.entities.ShopOrderEntity"%>
<%@page import="it.webapp.db.entities.AddressEntity"%>
<%@page import="it.webapp.db.entities.ShopOrderAggregateInfo"%>
<%@page import="it.webapp.view.OrderInfoView"%>
<%@page import="java.util.List"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>I tuoi ordini | Nanobit</title>
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
                <div id="pagination-content"></div>
                <div class="content-box">
                    <nav class="pagination-nav">
                        <ul id="pagination"></ul>
                    </nav>
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
    <script src="${contextPath}/js/LeaveFeedback.js"></script>
    <script type="text/javascript">
    $(function () {
        window.pagObj = $('#pagination').twbsPagination({
            totalPages: 35,
            visiblePages: 10,
            first: null,
            prev: "Pag. prec",
            next: "Pag. succ",
            last: null,
            hideOnlyOnePage : true,
            onPageClick: function (event, page) {
                
                var count = 10;
                var offset = count * (page - 1);
                
                var data = {
                    ${ShopOrdersPreprocessor.OFFSET_PARAM}: offset,
                    ${ShopOrdersPreprocessor.COUNT_PARAM}: count
                };
                
                //Performs ajax request
                $.post("${contextPath}${ShopOrdersController.URL}", data, function(result){
                    $("#pagination-content").html(result);
                });
            }
        });
    });
</script>
    
    
</html>