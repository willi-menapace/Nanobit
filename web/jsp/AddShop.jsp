<%--
    Document    : AddShop
    Author      : Daniele Giuliani, Alessio Paternoster
    Desciption  : Pagina utilizzata dall'utente per aprire un proprio negozio
    Requirements: Base.css, util.js
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.view.AddShopView"%>
<%@page import="it.webapp.applicationcontroller.AddShopController"%>
<%@page import="it.webapp.requestprocessor.AddShopPreprocessor"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="error_message" value="<%= (String) request.getAttribute(AddShopView.MESSAGE_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Apertura Negozio | Nanobit</title>
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
                    <span class="title">Apri negozio</span>
                    <p>Prima che tu possa aprire un negozio abbiamo bisogno di alcune informazioni.</p>
                    <p>Completa il seguente modulo per poter iniziare a vendere.</p>
                    <div class="spacer"></div>
                    <form method="POST" action="${contextPath}${AddShopController.URL}" enctype="multipart/form-data">
                        <div class="info">
                            <p class="info-label">Partita IVA</p>
                            <input type="text" name="${AddShopPreprocessor.VAT_PARAM}" class="textbox"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Descrizione del negozio</p>
                            <textarea name="${AddShopPreprocessor.DESCRIPTION_PARAM}" class="textbox"></textarea>
                        </div>
                        <div class="info">
                            <p class="info-label">Telefono</p>
                            <input type="text" name="${AddShopPreprocessor.PHONE_PARAM}" class="textbox"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Seleziona delle immagini per il tuo negozio</p>
                            <input type="file" name="${AddShopPreprocessor.IMAGES_PARAM}" multiple/>
                        </div>
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
