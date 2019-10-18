/*
    Author     	: Alessio Paternoster
    Desciption 	: Script JS per il file Shop.jsp
*/

/* VARIABILI GLOBALI (Libreria Google Maps) */
var google;

/* VARIABILI GLOBALI ESTERNE - Dal file utils.js */
var pagination;
var reviewStars;

/* VARIABILI GLOBALI ESTERNE - INIZIALIZZATE IN Shop.jsp */
var shopReviewsInfo;
var shopReviewControllerInfo;
var shopAddressInfo;
var shopRating;
var shopID;

/* VARIABILI LOCALI */
var reviewStartContainerID = "#shop-review-stars-container";
var reviewPaginationID = "#reviews-pagination";
var reviewContainerID = "#shop-reviews-container";
var shopMapContainerID = "google-position-map";

var googleMapsEmbedded = {
    
    // L'oggetto che rappresenta la mappa
    map : null,
    
    // Rappresenta il marker che sarà posizionato sulla mappa
    marker : null,
    
    // Proprietà della mappa dove verrà visualizzato lo shop
    mapProperties : {
        center: {lat: shopAddressInfo.LatCoordinate, lng: shopAddressInfo.LngCoordinate},
        gestureHandling: 'greedy',
        disableDefaultUI: true,
        fullscreenControl: true,
        zoom: 15
    },
    
    // Proprietà del marker che sarà mostrato sulla mappa dello shop
    markerProperties : {
        position: null,
        map: null
    },
    
    addMarker : function(){
        googleMapsEmbedded.markerProperties.map = googleMapsEmbedded.map;
        googleMapsEmbedded.markerProperties.position = googleMapsEmbedded.mapProperties.center;
        googleMapsEmbedded.marker = new google.maps.Marker(googleMapsEmbedded.markerProperties);
    },
    
    /* Funzione che si occupa di inizializzare la mappa */
    init : function(){
        var mapContainerElement = document.getElementById(shopMapContainerID);
        googleMapsEmbedded.map = new google.maps.Map(mapContainerElement, googleMapsEmbedded.mapProperties);
        googleMapsEmbedded.addMarker();
    }
    
};

var reviewsHandle = {
    
    reviewControllerInfo : {},
    reviewPaginationID : null,
    reviewObjectID : null,
    reviewContainerID : null,
    reviewPerPage : 4,
    
    // I vari tipi di messaggio utilizzati per creare gli elementi informativi
    messages : {
        loading : "Caricamento recensioni in corso...",
        noReviews : "Non ci sono recensioni disponibili.",
        error : "Errore durante il recupero delle recensioni. Prova a ricaricare la pagina.."
    },
    
    initReviewHandleInfo : function(controllerURL, idParam, offsetParam, countParam, objectID, reviewsContainerID, reviewPaginationID){
        reviewsHandle.reviewControllerInfo = {
            URL: controllerURL,
            idParam : idParam,
            offsetParam : offsetParam,
            countParam : countParam
        };
        reviewsHandle.reviewObjectID = objectID;
        reviewsHandle.reviewContainerID = reviewsContainerID;
        reviewsHandle.reviewPaginationID = reviewPaginationID;
    },
    
    /* Funzione che crea un elemento review con i dati dati in input */
    createReview : function(title, rating, description){
        var reviewTitle = $("<span></span>");
        reviewTitle.addClass("review-title");
        reviewTitle.text(title);

        var reviewStarsContainer = $("<ul></ul>");
        reviewStarsContainer.addClass("review-stars");
        reviewStars.setStars(rating, reviewStarsContainer);

        var reviewHeader = $("<div></div>");
        reviewHeader.addClass("review-head");
        reviewHeader.append(reviewTitle);
        reviewHeader.append(reviewStarsContainer);

        var reviewDescription = $("<span></span>");
        reviewDescription.addClass("review-description");
        reviewDescription.text(description);

        var reviewElement = $("<div></div>");
        reviewElement.addClass("review content-box-inner");
        reviewElement.append(reviewHeader);
        reviewElement.append(reviewDescription);
        
        return reviewElement;
    },
    
    /* Funzione che crea l'elemento informativo nel box delle recensioni */
    createInfoElement : function(infoText){
        var spacer = $("<div></div>");
        spacer.addClass("spacer");
        
        var errorText = $("<span></span>");
        errorText.text(infoText);
        
        var container = $("<div><div>");
        container.append(spacer);
        container.append(errorText);
        
        return container;
    },
    
    /* Funzione che si occupa di gestire il recupero delle review
     * NOTA: in input è richiesta la 'page' che non è altro che la pagina selezionata nel pagination nav */
    paginationReviewsRequest : function(event, page){
        var dataToSend = {};
        dataToSend[reviewsHandle.reviewControllerInfo.idParam] = reviewsHandle.reviewObjectID;
        dataToSend[reviewsHandle.reviewControllerInfo.offsetParam] = (page - 1) * reviewsHandle.reviewPerPage;
        dataToSend[reviewsHandle.reviewControllerInfo.countParam] = reviewsHandle.reviewPerPage;
        
        // Prima di caricare i risultati, nascondo la pagination
        $(reviewsHandle.reviewPaginationID).hide();
        
        // Prima di ricercare gli elementi, diamo un'informazione del caricamento all'utente
        $(reviewsHandle.reviewContainerID).empty();
        var loadingElement = reviewsHandle.createInfoElement(reviewsHandle.messages.loading);
        $(reviewsHandle.reviewContainerID).append(loadingElement);
        
        /* Funzione AJAX che si occupa del recupero delle review */
        $.ajax({
            type: "POST",
            url: reviewsHandle.reviewControllerInfo.URL,
            datatype: 'json',
            data: dataToSend,
            success: function (data) {
                $(reviewsHandle.reviewContainerID).empty();
                
                // Controllo se i dati JSON sono vuoti e se sono validi, in caso negativo visualizzo le review
                var isJSONData = generic.isJSONData(data);
                if(jQuery.isEmptyObject(data) || !isJSONData){
                    var noReviews = reviewsHandle.createInfoElement(reviewsHandle.messages.noReviews);
                    $(reviewsHandle.reviewContainerID).append(noReviews);
                }else{
                    jQuery.each(data, function(id, obj) {
                        var review = reviewsHandle.createReview(obj.title, obj.rating.rating, obj.description);
                        $(reviewsHandle.reviewContainerID).append(review);
                    });
                    $(reviewsHandle.reviewPaginationID).show();
                }
            },
            error: function (jqXHR, textStatus, thrownError) {
                $(reviewsHandle.reviewContainerID).empty();
                var errorElement = reviewsHandle.createInfoElement(reviewsHandle.messages.error);
                $(reviewsHandle.reviewContainerID).append(errorElement);
                console.log("ItemSearchHint Ajax - Error ! Not possible to retrive 'hint' data.");
            }
        });
    }
};


$(document).ready(function() {
    
    // Imposto le stelline della popolarità
    reviewStars.setStars(shopRating, $(reviewStartContainerID));
    
    // Imposto la paginazione se ci sono review..
    if(shopReviewsInfo.isEmpty === false){
        /* Inizializzo il reviewHandle, ovvero lo strumento JQuery che mi visualizzerà le reviews */
        reviewsHandle.initReviewHandleInfo(shopReviewControllerInfo.controllerURL, shopReviewControllerInfo.shopIDParam, shopReviewControllerInfo.offsetParam,
                shopReviewControllerInfo.countParam, shopID, reviewContainerID, reviewPaginationID);
                
        /* Inizializzo la pagination */
        pagination.initiateStartPageClick = true;
        pagination.totalPages = Math.ceil(shopReviewsInfo.reviewsCount / reviewsHandle.reviewPerPage);
        pagination.onPageClick = reviewsHandle.paginationReviewsRequest;
        $(reviewPaginationID).twbsPagination(pagination);
    }
    
});
    