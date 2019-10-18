<%--
    Document    : User Orders
    Author      : Willi Menapace
    Desciption  : Pagina utilizzata dall'utente per visualizzare le proprie issues
    
    IMPORTANTE: 
        - non modificare l'ordine dei vari elementi ne le classi a loro assegnate, altrimenti il javascript che 
          esegue le funzionalità si rompe! E' comunque possibile modificare tutto il CSS che si vuole
--%>
<%@page import="it.webapp.view.UserIssueBaseView"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.UserIssueController"%>
<%@page import="it.webapp.requestprocessor.UserIssuePreprocessor"%>

<%@page import="java.util.List"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="error_message" value="<%= (String) request.getAttribute(UserIssueBaseView.MESSAGE_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Le tue segnalazioni | Nanobit</title>
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
                    ${UserIssuePreprocessor.OFFSET_PARAM}: offset,
                    ${UserIssuePreprocessor.COUNT_PARAM}: count
                };
                
                //Performs ajax request
                $.post("${contextPath}${UserIssueController.URL}", data, function(result){
                    $("#pagination-content").html(result);
                });
            }
        });
    });
</script>
    
</html>