<%--
    Document  	: StartResetUserPasswordSuccessful.jsp
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Pagina per confermare il corretto invio della mail di recupero password.
    Requirements: Base.css, AddUserForm.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.HomepageController" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reset Password | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
		<link rel="stylesheet" type="text/css" href="${contextPath}/css/AddUserForm.css"/>
    </head>
    <body>
        <div class="container">
            <a class="logo-container" href="${contextPath}${HomepageController.URL}">
                <img class="logo" src="${contextPath}/resource/logo.png">
            </a>
            <div class="content">
                <span class="title">Email inviata</span>
                <p class="text-small">Abbiamo inviato le istruzioni per recuperare la password all'indirizzo email associato al tuo account, se non vedi la mail ricorda di controllare la casella "spam".</p>
                <p class="text-small">Se hai dimenticato l'indirizzo email associato al tuo account contatta un'amministratore.</p>
                <p><a href="${contextPath}${HomepageController.URL}" class="link-small link-dark">Torna alla homepage</a></p>
            </div>
        </div>
    </body>
</html>
