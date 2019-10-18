/*
    Author     : Alessio Paternoster
    Description: Libreria di funzionalità javascript per il file ItemSearchBase.jsp
*/

/* VARIABILI GLOBALI - Inizializzate nel file ItemSearchBase.jsp */
var staticContentControllerInfo;
var itemSearchControllerURL;
var itemControllerInfo;
var initItemSearchInfo;
var itemSearchParam;
var defaultItemImageURI;

/* VARIABILI GLOBALI (Libreria Google Maps) */
var google;

/* VARIABILI GLOBALI in utils.js */
var pagination;
var reviewStars;
var generic;

/* VARIABILI LOCALI ALLO SCRIPT ItemSearchBase.js */
var googleMapsContainerID = "google-position-map";
var googleMapsInfoLabelID = "#map-info-label";
var geoCodingLatInputID = "#item-geocoding-lat";
var geoCodingLngInputID = "#item-geocoding-lng";
var geoCodingRadiusContainerID = "#item-geocoding-radius-container";
var geoCodingRadiusInputID = "#item-geocoding-radius-input";
var itemResultFilterFormID = "#item-result-filter-form";
var itemPriceInfoErrorLabelID = "#item-price-info-error";
var itemResultsSortDropdownID = "#item-results-sort-dropdown";
var itemResultsCountDropdownID = "#item-results-count-dropdown";
var ItemResultsContainerID = "#item-catalog-results";
var itemPaginationID = "#item-results-paginaton";
var geolocationEnableDisableOptionInputName = "geolocation-option";
var geolocationMapContainerID = "#item-geolocation-map-container";

var geoCoding = {

    /* Coordinate di default. Posizione di default: Trento */
    LatCoordinate : 46.074486,
    LngCoordinate : 11.122070,

    /* Funzione che si occupa di recuperare la posizione dell'utente */
    getUserPosition : function(){
        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(geoCoding.success, geoCoding.error);
        }else{
            console.log("User's browser not support geolocation.");
        }
    },

    /* Funzione che gestisce il caso 'successo' della geolocalizzazione */
    success : function(position){
        geoCoding.LatCoordinate = position.coords.latitude;
        geoCoding.LngCoordinate = position.coords.longitude;

        // Se la geolocalizzazione è attiva aggiorno la posizione sulla mappa (e il marker)
        googleMaps.updateMapPosition();
    },

    /* Funzione che gestisce il caso 'error' della geolocalizzazione*/
    error : function(error_message){
        console.log("Geolocation error. " + error_message.message);
    }

};

var googleMaps = {

    // L'oggetto che rappresenta la mappa
    map : null,

    // Rappresenta il marker che sarà posizionato sulla mappa
    marker : null,

    // Proprietà di default della mappa
    mapDefaultProperties : {
        center: {lat: geoCoding.LatCoordinate, lng: geoCoding.LngCoordinate},
        gestureHandling: 'greedy',
        disableDefaultUI: true,
        fullscreenControl: true,
        zoom: 14
    },

    // Proprietà di default del marker
    markerDefaultProperties : {
        position: null,
        map: null,
        draggable: true
    },

    /* Funzione che si occupa di inizializzare la mappa */
    initMap : function(){
        var mapContainerElement = document.getElementById(googleMapsContainerID);
        googleMaps.map = new google.maps.Map(mapContainerElement, googleMaps.mapDefaultProperties);
        googleMaps.map.addListener('click', googleMaps.mapListener);
    },

    /* Funzione che viene utilizzata dal Geocodice per aggiornare la posizione sulla mappa */
    updateMapPosition : function(){
        if(googleMaps.map !== null){
            var position = new google.maps.LatLng(geoCoding.LatCoordinate, geoCoding.LngCoordinate);
            googleMaps.map.setCenter(position);
            googleMaps.addMarker(position);
        }
    },

    /* Funzione che viene utilizzata quando si mostra la mappa. Fa si che la mapa si resetti e venga visualizzata
     * Senza questo 'restore' la mappa viene visualizzata, ma appare come se i dati della mappa non fossero caricati */
    resetMap : function(){
        google.maps.event.trigger(googleMaps.map, 'resize');
    },

    /* Funzione utilizzata per rimuovere il marker attraverso JS */
    removeMarker : function(){
        if(googleMaps.marker !== null){
            google.maps.event.trigger(googleMaps.marker, 'click');
        }
    },

    /* Funzione che si occupa di impostare / aggiornare la posizione del marker */
    addMarker : function(position){
        if(googleMaps.marker !== null){
            googleMaps.marker.setPosition(position);
        }else{
            googleMaps.markerDefaultProperties.position = position;
            googleMaps.markerDefaultProperties.map = googleMaps.map;
            googleMaps.marker = new google.maps.Marker(googleMaps.markerDefaultProperties);
            googleMaps.marker.addListener('click', googleMaps.markerListener);
        }
        general.updateGeocodingFormObjects(true, position.lat, position.lng);
    },

    /* Listener onClick sulla mappa */
    mapListener : function(event){
        googleMaps.addMarker(event.latLng);
    },

    /* Listener onClick sul marker */
    markerListener : function(event){
        googleMaps.marker.setMap(null);
        googleMaps.markerDefaultProperties.position = null;
        googleMaps.marker = null;
        general.updateGeocodingFormObjects(false, null, null);
    }

};

var resultsHandle = {

    // Variabile che contiene i dati da spedire via AJAX
    dataToSend : {},

    // I vari tipi di messaggio utilizzati per creare gli elementi informativi
    messages : {
        loading : "Caricamento risultati in corso...",
        noResults : "Nessun risultato corrispondente ai criteri di ricerca impostati.",
        error : "Errore durante il recupero dei risultati. Prova a ricaricare la pagina.."
    },

    /* Funzione che inizializza i dati da spedire con AJAX (poi saranno modificati) */
    initData : function(){
        resultsHandle.dataToSend[itemSearchParam.termParam] = initItemSearchInfo.term;
        resultsHandle.dataToSend[itemSearchParam.departmentParam] = initItemSearchInfo.department;
        resultsHandle.dataToSend[itemSearchParam.priceLowParam] = null;
        resultsHandle.dataToSend[itemSearchParam.priceHighParam] = null;
        resultsHandle.dataToSend[itemSearchParam.latitudeParam] = null;
        resultsHandle.dataToSend[itemSearchParam.longitudeParam] = null;
        resultsHandle.dataToSend[itemSearchParam.radiusParam] = null;
        resultsHandle.dataToSend[itemSearchParam.orderParam] = initItemSearchInfo.order;
        resultsHandle.dataToSend[itemSearchParam.offsetParam] = 0;
        resultsHandle.dataToSend[itemSearchParam.countParam] = initItemSearchInfo.count;
    },

    /* Funzione utilizzate per impostare un dato dentro dataToSend */
    setDataToSendValue : function(param, value){
        resultsHandle.dataToSend[param] = value;
    },

    /* Funzione utilizzate per ricavare un dato da dataToSend */
    getDataToSendValue : function(param){
        return resultsHandle.dataToSend[param];
    },

    /* Funzione che crea un generico box informativo per l'utente nel catalogo dei risultati di ricerca */
    createInfoElement : function(infoText){
        var loadingInfoText = $("<span></span>");
        loadingInfoText.text(infoText);

        var itemLoadingInfoContainer = $("<div></div>");
        itemLoadingInfoContainer.addClass("item-results-info");
        itemLoadingInfoContainer.append(loadingInfoText);

        var container = $("<div></div>");
        container.addClass("content-box");
        container.append(itemLoadingInfoContainer);

        return container;
    },

    createItemElement : function(itemInfo){

        // Creo l'url dell'immagine
        var imageURI = "";
        if(itemInfo.image === null || itemInfo.image === undefined || itemInfo.image.filename === ""){
            imageURI = defaultItemImageURI;
        }else{
            imageURI = staticContentControllerInfo.URL + "?" + staticContentControllerInfo.imageParam + "=" + itemInfo.image.filename;
        }

        /* IMMAGINE */
        var imageElement = $("<img></img>");
        imageElement.attr("src", imageURI);

        var imageContainer = $("<div></div>");
        imageContainer.addClass("item-image-container");
        imageContainer.append(imageElement);

        /* INFORMAZIONI ITEM */
        var titleElement = $("<h3></h3>");
        titleElement.text(itemInfo.title);

        var itemInfoHeadContainer = $("<div></div>");
        itemInfoHeadContainer.addClass("item-description-vendors");
        itemInfoHeadContainer.append(titleElement);

        var starsContainer = $("<ul></ul>");
        starsContainer.addClass("review-stars");
        reviewStars.setStars(itemInfo.rating, starsContainer);

        var reviewText = $("<span></span>");
        reviewText.text("(" + itemInfo.reviewsCount + " " + generic.getReviewText(itemInfo.reviewsCount) + ")");

        var reviewContainer = $("<div></div>");
        reviewContainer.addClass("item-star-review-info");
        reviewContainer.append(starsContainer);
        reviewContainer.append(reviewText);

        var infoContainer = $("<div></div>");
        infoContainer.addClass("item-info-container");
        infoContainer.append(itemInfoHeadContainer);
        infoContainer.append(reviewContainer);

        /* PREZZO ITEM */
        var priceElement = $("<span></span>");
        priceElement.text(itemInfo.price.toFixed(2) + "€"); // toFixed() imposta sempre tot cifre dopo la virgola

        var priceCartContainer = $("<div></div>");
        priceCartContainer.addClass("item-price-info-container");
        priceCartContainer.append(priceElement);

        /* ITEM */
        var container = $("<div></div>");
        container.addClass("content-box isb-entry");
        container.append(imageContainer);
        container.append(infoContainer);
        container.append(priceCartContainer);

        /* LINK PRODOTTO */
        var productLinkContainer = $("<a></a>");
        productLinkContainer.attr("href", itemControllerInfo.URL + "?" + itemControllerInfo.itemIDParam + "=" + itemInfo.ID);
        productLinkContainer.append(container);

        return productLinkContainer;
    },

    /* Funzione utilizzate per inviare la richiesta di item al controller opportuno */
    getItemResults : function(){

        // Prima di caricare i risultati, nascondo la pagination
        $(itemPaginationID).hide();

        // Prima di ricercare gli elementi, diamo un'informazione del caricamento all'utente
        $(ItemResultsContainerID).empty();
        var loadingElement = resultsHandle.createInfoElement(resultsHandle.messages.loading);
        $(ItemResultsContainerID).append(loadingElement);

        /* Funzione AJAX che si occupa del recupero degli item */
        $.ajax({
            type: "POST",
            url: itemSearchControllerURL,
            datatype: 'json',
            data: resultsHandle.dataToSend,
            success: function (data) {
                $(ItemResultsContainerID).empty();

                // Controllo se i dati JSON sono vuoti e se sono validi, in caso negativo visualizzo gli item
                var isJSONData = generic.isJSONData(data);
                if(jQuery.isEmptyObject(data) || !isJSONData){
                    var noResultsElement = resultsHandle.createInfoElement(resultsHandle.messages.noResults);
                    $(ItemResultsContainerID).append(noResultsElement);
                }else{
                    jQuery.each(data, function(id, obj) {
                        /* Preparo le informazioni */
                        var itemInfo = {
                            ID : obj.item.itemId,
                            title : obj.item.title,
                            rating : obj.averageRating.rating,
                            reviewsCount : obj.reviewsCount,
                            price : obj.minimumPrice,
                            image : obj.image
                        };

                        /* Effettuo il rendering.. */
                        var item = resultsHandle.createItemElement(itemInfo);
                        $(ItemResultsContainerID).append(item);
                    });
                    $(itemPaginationID).show();
                }
            },
            error: function (jqXHR, textStatus, thrownError) {
                // Diamo l'informazione all'utente dell'errore
                $(ItemResultsContainerID).empty();
                var errorElement = resultsHandle.createInfoElement(resultsHandle.messages.error);
                $(ItemResultsContainerID).append(errorElement);

                console.log("ItemSearch Ajax - Error ! Not possible to retrive items data." + thrownError.message);
            }
        });
    }

};

var general = {

    /* Funzione che si occupa di attivare / disattivare il filtro sulla posizione */
    onGeocodingPositionEnableDisableOption : function(){
        var geocodingPositionEnableDisableOptionVal = $(this).val();

        // Controllo se sto attivando o disabilitando il filtro sulla posizione
        if(geocodingPositionEnableDisableOptionVal === "1"){
            $(geolocationMapContainerID).show();
            googleMaps.resetMap();
            geoCoding.getUserPosition();
        }else if(geocodingPositionEnableDisableOptionVal === "0"){
            googleMaps.removeMarker();
            $(geolocationMapContainerID).hide();
        }
    },

    /* Funzione che si occupa di aggiornare lo stato del label informativo sotto la mappa*/
    updateMapInfoLabel : function(isMarkerActive){
        if(isMarkerActive){
            $(googleMapsInfoLabelID).text("(Click sul marker per rimuovere il punto di ritiro)");
        }else{
            $(googleMapsInfoLabelID).text("(Click sulla mappa per impostare la posizione)");
        }
    },

    /* Funzione che si occupa di mostrare / nascondere il blocco per la selezione del raggio di distanza */
    updateGeocodingRadiusBoxStatus(geoCodingActive){
        // Se il geocoding è attivo, mostro il blocco di selezione del raggio di distanza
        if(geoCodingActive){
            $(geoCodingRadiusContainerID).show();
            $(geoCodingRadiusContainerID + " :input").prop('disabled', !geoCodingActive);
        }else{
            $(geoCodingRadiusContainerID).hide();
            $(geoCodingRadiusContainerID + " :input").prop('disabled', !geoCodingActive);
        }
    },

    /* Funzione che si occupa di aggiornare gli input con le informazioni della geolocalizzione e
     * di attivare / disattivare il blocco relativo alla geolocalizzazione */
    updateGeocodingFormObjects : function(isMarkerActive, latitude, longitude){
        // Se il marker non è stato rimosso, aggiorno la posizione
        if(isMarkerActive){
            // Aggiorno i valori
            $(geoCodingLatInputID).val(latitude);
            $(geoCodingLngInputID).val(longitude);
        }

        // Abilito / disabilito gli input per l'invio del form in base allo stato del marker
        $(geoCodingLatInputID).prop("disabled", !isMarkerActive);
        $(geoCodingLngInputID).prop("disabled", !isMarkerActive);

        // Aggiorno lo stato del blocco per la selezione del raggio di distanza
        general.updateGeocodingRadiusBoxStatus(isMarkerActive);

        // Aggiorno il label informativo sotto la mappa
        general.updateMapInfoLabel(isMarkerActive);

        // Imposto che il form è cambiato
        $(itemResultFilterFormID).data("changed", true);
    },

    /* Funzione che gestisce i parametri provenienti dal submit del form */
    filterFormHandle : function(event){
        // Faccio si che il form non venga inviato
        event.preventDefault();

        if($(itemResultFilterFormID).data("changed") === true){
            var formParams = $(this).serializeArray();
            var filtersValue = {};

            // Estraggo i dati dai parametri del form
            $.each(formParams, function (index, filter_param) {
                // Dal form arrivano solo numeri perciò, se non è numero imposto a null
                if(!isNaN(filter_param.value)){
                    filtersValue[filter_param.name] = filter_param.value;
                }else{
                    filtersValue[filter_param.name] = null;
                }
            });

            var priceLowString = parseFloat(filtersValue[itemSearchParam.priceLowParam]);
            var priceHighString = parseFloat(filtersValue[itemSearchParam.priceHighParam]);

            // Controlli sui prezzi (valori e fascia di prezzo)
            if(isNaN(priceLowString) || parseFloat(priceLowString) < 0){
                priceLow = null;
            }else{
                priceLow = parseFloat(priceLowString);
            }
            if(isNaN(priceHighString) || parseFloat(priceHighString) < 0){
                priceHigh = null;
            }else{
                priceHigh = parseFloat(priceHighString);
            }

            // Se uno dei due è null o la fascia non è corretta viene visualizzato il messaggio
            if(!((priceLow === null && priceHigh === null) || (priceLow !== null && priceHigh !== null && priceHigh > priceLow))){
                $(itemPriceInfoErrorLabelID).show();
                return;
            }

            // Aggiorno i dai DataToSend della resultsHandle
            resultsHandle.setDataToSendValue(itemSearchParam.latitudeParam, filtersValue[itemSearchParam.latitudeParam]);
            resultsHandle.setDataToSendValue(itemSearchParam.longitudeParam, filtersValue[itemSearchParam.longitudeParam]);
            resultsHandle.setDataToSendValue(itemSearchParam.radiusParam, filtersValue[itemSearchParam.radiusParam]);
            resultsHandle.setDataToSendValue(itemSearchParam.priceLowParam, priceLow);
            resultsHandle.setDataToSendValue(itemSearchParam.priceHighParam, priceHigh);

            // AJAX request per risultati (nascondo la pagination perchè è un nuova ricerca)
            resultsHandle.getItemResults();

            // Ricreo la pagination
            itemResultsPagination.reInitPagination();

            // Dopo aver inviato i dati di filtraggio, imposto l'attuale stato del form come quello di default
            $(itemResultFilterFormID).data("changed", false);
        }
    },

    /* Funzione che gestisce il click sul menu dropDown */
    onFilterDropdownClick : function(selectedValue, param){
        var filterValue = parseInt(selectedValue);
        var oldFilterValue = resultsHandle.getDataToSendValue(param);
        if(filterValue !== oldFilterValue){
            resultsHandle.setDataToSendValue(param, filterValue);
            resultsHandle.setDataToSendValue(itemSearchParam.offsetParam, 0);

            // AJAX request per risultati (nascondo la pagination perchè è un nuova ricerca)
            resultsHandle.getItemResults();

            // Ricreo la pagination
            itemResultsPagination.reInitPagination();
        }
    }

};

var itemResultsPagination = {

    /* Funzione che inizializza i parametri della pagination */
    init : function(){
        itemResultsPagination.setPaginationPages();
        pagination.onPageClick = itemResultsPagination.paginationResultsRequest;
    },

    setPaginationPages(){
        var totalResults = 300; // E' una dato impostato staticamente - Vedi Lucene Index
        var resultsPerPage = resultsHandle.getDataToSendValue(itemSearchParam.countParam);
        pagination.totalPages = Math.ceil(totalResults / resultsPerPage);
    },

    /* Funzione che re-inizializza la pagination */
    reInitPagination : function(){
        $(itemPaginationID).twbsPagination("destroy");
        itemResultsPagination.setPaginationPages();
        $(itemPaginationID).twbsPagination(pagination);
    },

    /* Funzione che si occupa di gestire la paginazione
     * NOTA: in input è richiesta la 'page' che non è altro che la pagina selezionata nel pagination nav */
    paginationResultsRequest : function(event, page){
        var resultsPerPage = resultsHandle.getDataToSendValue(itemSearchParam.countParam);
        var offset = (page - 1) * resultsPerPage;
        resultsHandle.setDataToSendValue(itemSearchParam.offsetParam, offset);
        resultsHandle.getItemResults();
    }
};

$(document).ready(function() {
    general.updateMapInfoLabel(false);
    resultsHandle.initData();
    resultsHandle.getItemResults();
    $(itemResultFilterFormID).submit(general.filterFormHandle);
    $(itemResultFilterFormID).change(function(){
        // Se l'utente applica dei filtri rispetto a quelli precedentement applicati, viene notificato il cambiamento
        $(itemResultFilterFormID).data("changed", true);
    });
    $(".item-price-info div.row input").click(function(){
        // Non appena si clicca su un input price per cambiare la fascia, l'eventuale errore viene nascosto
        $(itemPriceInfoErrorLabelID).hide();
    });
    $(itemResultsSortDropdownID + " ul.dropdown-menu-list li").click(function(){
        general.onFilterDropdownClick($(this).find("span").text(), itemSearchParam.orderParam);
    });
    $(itemResultsCountDropdownID + " ul.dropdown-menu-list li").click(function(){
        general.onFilterDropdownClick($(this).find("span").text(), itemSearchParam.countParam);
    });
    $("input[name='" + geolocationEnableDisableOptionInputName + "']").change(
            general.onGeocodingPositionEnableDisableOption
    );

    /* Inizializzo le la pagination */
    itemResultsPagination.init();
    $(itemPaginationID).twbsPagination(pagination);
});
