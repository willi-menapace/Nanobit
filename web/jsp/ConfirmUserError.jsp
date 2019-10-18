<%--
    Document  	: ConfirmUserError.jsp
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Pagina visualizzata dopo che la conferma dell'account è fallita.
    Requirements: Base.css, AddUserForm.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.view.ConfirmUserErrorView" %>
<%@page import="it.webapp.applicationcontroller.HomepageController" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="message" value="<%= (String) request.getAttribute(ConfirmUserErrorView.MESSAGE_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registrazione Errore | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/AddUserForm.css"/>
    </head>
    <body>
        <div class="container">
            <a class="logo-container">
                <img class="logo" src="${contextPath}/resource/logo.png">
            </a>
            <div class="content">
                <span class="title">Attenzione</span>
                <p class="text-small">Purtroppo si è verificato un errore.</p>
                <p class="text-small">${message}</p>
                <p class="text-small">
                    <a href="${contextPath}${HomepageController.URL}" class="link-small link-dark">Clicca qui per tornare alla homepage.</a>
                </p>
            </div>
        </div>
    </body>
</html>
