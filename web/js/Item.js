/*
    Author     	: Alessio Paternoster
    Desciption 	: Script JS per il file Item.jsp
*/

/* VARIABILI GLOBALI ESTERNE - Dal file utils.js */
var pagination;
var reviewStars;
var generic;

/* VARIABILI GLOBALI ESTERNE - INIZIALIZZATE IN Item.jsp */
var updateCartItemQuantityControllerInfo;
var itemReviewControllerInfo;
var itemReviewsInfo;
var defaultShopID;
var shopControllerInfo;
var shopsRelatedInfo;
var itemInfo;

/* VARIABILI LOCALI */
var shopReviewStarsContainerID = "#shop-review-stars-container";
var shopReviewsCountContainerID = "#shop-reviews-count";
var shopPageUrlLinkID = "#shop-page-url";
var itemAvailablePiecesContainerID = "#availability-pieces-container";
var itemAvailablePickUpElementID = "#availability-pieces-pickup";
var itemReviewStarsContainerID = "#item-review-stars-container";
var priceTextContainerID = "#item-price-text";
var reviewPaginationID = "#reviews-pagination";
var itemReviewsContainerID = "#item-reviews-container";
var dropdownOptionElement = ".dropdown-menu ul.dropdown-menu-list li";
var addItemToCartLinkID = "#add-item-to-cart";

var general = {
    
    /* Funzione che si occupa di mostrare il sommario delle review-info */
    setReviewsText : function(reviewCount, reviewTextContainer){
        reviewTextContainer.text(reviewCount + " " + generic.getReviewText(reviewCount));
    },
    
    /* Funzione che si occupa di mostrare il prezzo dell'item 
     * NOTA: la funzione toFixed(cifre) mostra sempre tot 'cifre' dopo la virgola */
    setPrice : function(price, priceContainerID){
        priceContainerID.text(price.toFixed(2));
    },
    
    /* Funzione che si occupa di mostrare le informazioni sulla disponibilità */
    setAvailabilityInfo : function(itemAvailablePickUpElementID, itemAvailablePiecesContainerID, pickUpFromShop, availablePieces){
        if(pickUpFromShop === true){
            $(itemAvailablePickUpElementID).show();
        }else{
            $(itemAvailablePickUpElementID).hide();
        }
        if(availablePieces === 0){
            $(itemAvailablePiecesContainerID).text(availablePieces + " pezzo");
        }else{
            $(itemAvailablePiecesContainerID).text(availablePieces + " pezzi");
        }
    },
    
    /* Funzione principe, si occupa dei cambiamenti da fare quando viene cambiato venditore 
     * NOTA: In questa funzione soo utilizzate le variabili globali*/
    onShopChange : function(shopUserId){
        if(Object.keys(shopsRelatedInfo).length > 0){
            var currentShop = shopsRelatedInfo[shopUserId];
            reviewStars.setStars(currentShop.ranking, $(shopReviewStarsContainerID));
            general.setReviewsText(currentShop.reviewsCount, $(shopReviewsCountContainerID));
            general.setAvailabilityInfo($(itemAvailablePickUpElementID), $(itemAvailablePiecesContainerID), currentShop.pickupFromShop, currentShop.availability);
            general.setPrice(currentShop.price, $(priceTextContainerID));
            $(shopPageUrlLinkID).attr("href", shopControllerInfo.URL + "?" + shopControllerInfo.shopIdParam + "=" + shopUserId);
            $(addItemToCartLinkID).attr("href", updateCartItemQuantityControllerInfo.URL + "?" + updateCartItemQuantityControllerInfo.shopItemIdParam 
                    + "=" + currentShop.shopItemId + "&" + updateCartItemQuantityControllerInfo.quantityParam + "=1");
        }
        reviewStars.setStars(itemInfo.rating, $(itemReviewStarsContainerID));
    }

};

var reviewsHandle = {
    
    reviewPerPage : 4,
    
    // I vari tipi di messaggio utilizzati per creare gli elementi informativi
    messages : {
        loading : "Caricamento recensioni in corso...",
        noReviews : "Non ci sono recensioni disponibili per questo prodotto.",
        error : "Errore durante il recupero delle recensioni. Prova a ricaricare la pagina.."
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
        dataToSend[itemReviewControllerInfo.itemIDParam] = itemInfo.ID;
        dataToSend[itemReviewControllerInfo.offsetParam] = (page - 1) * reviewsHandle.reviewPerPage;
        dataToSend[itemReviewControllerInfo.countParam] = reviewsHandle.reviewPerPage;
        
        // Prima di caricare i risultati, nascondo la pagination
        $(reviewPaginationID).hide();
        
        // Prima di ricercare gli elementi, diamo un'informazione del caricamento all'utente
        $(itemReviewsContainerID).empty();
        var loadingElement = reviewsHandle.createInfoElement(reviewsHandle.messages.loading);
        $(itemReviewsContainerID).append(loadingElement);
        
        /* Funzione AJAX che si occupa del recupero delle review */
        $.ajax({
            type: "POST",
            url: itemReviewControllerInfo.URL,
            datatype: 'json',
            data: dataToSend,
            success: function (data) {
                $(itemReviewsContainerID).empty();
                
                // Controllo se i dati JSON sono vuoti e se sono validi, in caso negativo visualizzo le review
                var isJSONData = generic.isJSONData(data);
                if(jQuery.isEmptyObject(data) || !isJSONData){
                    var noReviews = reviewsHandle.createInfoElement(reviewsHandle.messages.noReviews);
                    $(itemReviewsContainerID).append(noReviews);
                }else{
                    jQuery.each(data, function(id, obj) {
                        var review = reviewsHandle.createReview(obj.title, obj.rating.rating, obj.description);
                        $(itemReviewsContainerID).append(review);
                    });
                    $(reviewPaginationID).show();
                }
            },
            error: function (jqXHR, textStatus, thrownError) {
                $(itemReviewsContainerID).empty();
                var errorElement = reviewsHandle.createInfoElement(reviewsHandle.messages.error);
                $(itemReviewsContainerID).append(errorElement);
                console.log("ItemSearchHint Ajax - Error ! Not possible to retrive 'hint' data.");
            }
        });
    }
};

$(document).ready(function() {
    // Al caricamente della pagina carico i dati del primo shop
    general.onShopChange(defaultShopID);
    
    // Quando cambio shop, ricarico i dati
    $(dropdownOptionElement).click(function(){
        var shopuserId = $(this).find("span").text();
        general.onShopChange(shopuserId);
    });
    
    // Imposto la paginazione se ci sono review..
    if(itemReviewsInfo.isEmpty === false){
        pagination.initiateStartPageClick = true;
        pagination.totalPages = Math.ceil(itemReviewsInfo.reviewsCount / reviewsHandle.reviewPerPage);
        pagination.onPageClick = reviewsHandle.paginationReviewsRequest;
        $(reviewPaginationID).twbsPagination(pagination);
    }
});
