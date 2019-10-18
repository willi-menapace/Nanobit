<%--
    Document  	: Error.jsp
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Pagina generica di errore, visualizza un titolo e un messaggio
    Requirements: Base.css, AddUserForm.css, Error.css
--%>
<%@page pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.view.ErrorView" %>
<%@page import="it.webapp.applicationcontroller.HomepageController" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="errorTitle" value="<%= (String) request.getAttribute(ErrorView.TITLE_ATTRIBUTE) %>"></c:set>
<c:set var="errorDescription" value="<%= (String) request.getAttribute(ErrorView.DESCRIPTION_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html lang="it">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Errore | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/AddUserForm.css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Error.css"/>
    </head>
    <body>
        <div class="container">
            <a class="logo-container">
                <img class="logo" src="${contextPath}/resource/logo.png">
            </a>
            <div class="content">
                <div class="error-box">
                    <img src="${contextPath}/resource/error.png" class="error-icon">
                    <div>
                        <span class="title">${errorTitle}</span>
                        <p class="text-small">${errorDescription}</p>
                    </div>
                </div>
                <div class="form-group flex-centered" style="margin-top: 40px">
                    <a class="link-medium link-button" href="${contextPath}${HomepageController.URL}">Torna alla homepage</a>
                </div>
            </div>
        </div>
    </body>
</html>
