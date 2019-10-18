<%--
    Document    : Homepage
    Author      : Daniele Giuliani, Alessio Paternoster
    Desciption  : Homepage del sito web
    Requirements: Base.css, Homepage.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.db.entities.Department"%>
<%@page import="it.webapp.applicationcontroller.ItemSearchBaseController" %>
<%@page import="it.webapp.requestprocessor.ItemSearchBasePreprocessor" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="departements" value="<%= (Department[]) Department.values() %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Homepage | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Homepage.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="homepage">
                    <span class="title">Seleziona una categoria</span>
                    <ul class="department-grid">
                        <a href="${contextPath}${ItemSearchBaseController.URL}"><li>Tutte le categorie</li></a>
                        <c:forEach items="${departements}" var="department">
                            <a href="${contextPath}${ItemSearchBaseController.URL}?${ItemSearchBasePreprocessor.DEPARTMENT_PARAM}=${department.ID}"><li>${department.description}</li></a>
                        </c:forEach>
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
