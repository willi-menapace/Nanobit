<%--
    Document    : AddShopSuccessfull
    Author      : Daniele Giuliani, Alessio Paternoster
    Desciption  : Pagina visualizzata dall'utente dopo la corretta apertura di un negozio
	Requirements: Base.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.HomepageController"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Apertura Negozio Successo | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="content-box content-box-middle main">
                    <span class="title">Apri Negozio</span>
                    <p>
                        Congratulazioni, la procedura per aprire il negozio è stata completata. Puoi iniziare
                        a vendere immediatamente.
                    </p>
                    <p>
                        <a href="${contextPath}${HomepageController.URL}" class="link-medium link-dark">Torna alla homepage</a>
                    </p>
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
