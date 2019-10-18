<%--
    Document  	: EndResetUserPasswordSuccessful.jsp
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Pagina per confermare il corretto reset della password.
    Requirements: Base.css, AddUserForm.css, util.js
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
        <title>Reset Password Successo | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/AddUserForm.css"/>
    </head>
    <body>
        <div class="container">
            <a class="logo-container" href="${contextPath}${HomepageController.URL}">
                <img class="logo" src="${contextPath}/resource/logo.png">
            </a>
            <div class="content">
                <span class="title">Finito</span>
                <p class="text-small">
                    La tua password è stata reimpostata correttamente, verrari reindirizzato alla homepage per effettuare il login,
                    se non dovesse accadere nulla <a href="${contextPath}${HomepageController.URL}" class="link-small link-dark">clicca qui</a>.
                </p>
            </div>
        </div>
	</body>
    <!-- Script -->
    <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/util.js"></script>        
    <script>
        // Sets redirect param and do the redirect
        redirect.destinationURL = "${contextPath}${HomepageController.URL}";
        redirect.startRedirectTimeout();
    </script>
</html>
