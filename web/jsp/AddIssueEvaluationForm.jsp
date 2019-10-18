<%--
    Document    : AddShop
    Author      : Willi Menapace
    Desciption  : Pagina utilizzata dagli amministratori per chiudere una segnalazione
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.view.AddIssueEvaluationView"%>
<%@page import="it.webapp.applicationcontroller.AddIssueEvaluationController"%>
<%@page import="it.webapp.requestprocessor.AddIssueEvaluationPreprocessor"%>
<%@page import="it.webapp.db.entities.IssueResult"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="error_message" value="<%= (String) request.getAttribute(AddIssueEvaluationView.MESSAGE_ATTRIBUTE) %>"></c:set>
<c:set var="issue_id" value="<%= request.getAttribute(AddIssueEvaluationView.ISSUE_ID_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Valuta segnalazione | Nanobit</title>
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
                    <span class="title">Chiudi la segnalazione</span>
                    <p>Completa il seguente modulo per chiudere la segnalazione.</p>
                    
                    <div class="spacer"></div>
                    <form method="POST" action="${contextPath}${AddIssueEvaluationController.URL}" enctype="multipart/form-data">
                        
                        <div class="info">
                            <p class="info-label">Esito</p>
                            <select name="${AddIssueEvaluationPreprocessor.ISSUE_RESULT_ID_PARAM}" required>
                                <c:forEach items="${IssueResult.values()}" var="currentIssueResult">
                                    <option value="${currentIssueResult.id}">${currentIssueResult.description}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="info">
                            <p class="info-label">Motivazione</p>
                            <textarea name="${AddIssueEvaluationPreprocessor.MOTIVATION_PARAM}" class="textbox" required></textarea>
                        </div>

                        <input type="hidden" name="${AddIssueEvaluationPreprocessor.ISSUE_ID_PARAM}" value='${issue_id}'/>


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