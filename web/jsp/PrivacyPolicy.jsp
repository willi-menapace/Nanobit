<%--
    Document    : PrivacyPolicy
    Author      : Daniele Giuliani, Alessio Paternoster
    Desciption  : Pagina contenente la politica sulla privacy e sui cookie
    Requirements: Base.css, UpdateUser.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Privacy Policy | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/UpdateUser.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
    </head>
    <body>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="content-box content-box-middle main">
                    <span class="title">Politica sulla privacy</span>
                    <p>Tutte le informazioni collezionate da Nanobit servono solamente al fine di fornire un servizio migliore ai nostri utenti.</p>
                    <p>Le informazioni sensibili (email, carte di credito) fornite in fase di registrazione non verranno divulgate con terze parti, nel raro caso di attacco e furto di informazioini Nanobit contatterà tutti gli utenti interessati in un periodo inferiore ai 7 giorni</p>
                    <p>Tutte le foto o informazioni relative ad oggetti luoghi o altre informazioni relative a entità di natura non sensibile fornite in qualsiasi modo sono utilizzabili a fini commerciali, e non, da Nanobit.</p>
                    <p>Tutte le informazioni sono mantenute nei server di Nanobit in maniera sicura e possono permanere per un periodo anche superiore ai 60 giorni.</p>
                    <p>Il dominio Nanobit viene regolarmente controllato per evitare che pericolosi malware possano essere inseriti per danneggiare i nostri utenti.</p>
                    <p>Nanobit utilizza cookie per poter identificare l'utente e fornire un servizio moderno e competitivo, i cookie non vengono condivisi con terzi, tuttavia Nanobit si riserva il diritto di utilizzarli nella maniera che ritiene più appropriata.</p>
                    <p>Nanobit viene utilizzato principalmente per la vendita di beni e servizi, l'utilizzo è consentito solamente da parte di individui maggiorenni. E' esplicitamente vietato l'utilizzo da parte dei minori di 18 anni, eccetto mediante la supervisione di un adulto.</p>
                    <br>
                    <p class="info-label">Navigando sul nostro sito accetti la nostra politica sulla privacy e sui cookie.</p>
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
