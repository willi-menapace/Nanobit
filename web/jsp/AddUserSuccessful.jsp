<%--
    Document  	: AddUserSuccessful.jsp
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Pagina visualizzata per informare l'utente dell'avvenuta registrazione e l'invio
                  del link di attivazione per mail.
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
        <title>Conferma Registrazione | Nanobit</title>
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
                <span class="title">Conferma la registrazione</span>
                <p class="text-small">
                    Per completare la registrazione clicca sul link di attivazione che abbiamo inviato 
                    alla email da te indicata. Se non hai ricevuto nessuna email controlla la casella "spam" 
                    altrimenti riprova più tardi.
                </p>
                <p class="text-small">Se hai dimenticato l'indirizzo email associato al tuo account contatta un'amministratore.</p>
                <p><a href="${contextPath}${HomepageController.URL}" class="link-small link-dark">Torna alla homepage</a></p>
            </div>
        </div>
    </body>
</html>
