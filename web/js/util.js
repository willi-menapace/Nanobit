/*
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Libreria javascript per funzioni di utilità comune (es. messaggi popup).
*/

$('.message-close').click(function() {

	//in caso di messaggi con overlay dobbiamo fare un'animazione diversa
	if($(this).parent().parent().attr("class") == "message-overlay") {
		var target = $(this).parent().parent();
		target.animate(
			{
				opacity: '0'
			},
			function() {
				target.remove();
			}
		);
	} else {
		var target = $(this).parent();
		target.animate(
			{
				height: 'toggle'
			},
			function() {
				target.remove();
			}
		);
	}

});

/* Object used for redirect to some page */
var redirect = {

    destinationURL : "",
    delay : 5000, // delay in millisecondi

    redirectTo : function() {
        window.location.href = redirect.destinationURL;
    },

    startRedirectTimeout : function() {
        window.setTimeout(redirect.redirectTo, redirect.delay);
    }
};

/* Regole genercihec JS e JQuery per i menu dropdown */
var dropdownMenu = {

    /* Funzione utilizzata per mostrare o nascondere il menu dropdown quando lo si seleziona */
    toggleMenu : function(){
        $(this).parents(".dropdown-menu").find("ul.dropdown-menu-list").toggle();
    },

    /* Funzione utilizzata per nascondere il menu (utilizzata in funzioni interne / qui sotto)*/
    hideMenu : function(menuItem){
        menuItem.find("ul.dropdown-menu-list").hide();
    },

    /* Funzione utilizzata per selezionare un'opzione e sostituirla a quella esistente
       NOTA: l'opzione viene sostituita solo se diversa dall'opzione correntemente utilizzata */
    onMenuSelectItem : function(){
        var currentSelected = $(this).parents(".dropdown-menu").find("span.selected");
        var currentSelectedValue = currentSelected.html();
        var menuSelectedValue = $(this).html();
        if(menuSelectedValue !== currentSelectedValue){
            currentSelected.html(menuSelectedValue);
        }
        dropdownMenu.hideMenu($(this).parents(".dropdown-menu"));
    },

    /* Funzione utilizzata per chiudere tutti i menu dropdown qualora si facesse click fuori da queste aree */
    onDocumentClick : function(mouseEvent){
        var clickedElement = $(mouseEvent.target);
        if (!(clickedElement.parents().hasClass("dropdown-menu"))){
            dropdownMenu.hideMenu($(".dropdown-menu"));
        }
    }

};

$(document).ready(function() {
    $(".dropdown-menu span.selected").click(dropdownMenu.toggleMenu);
    $(".dropdown-menu ul.dropdown-menu-list li").click(dropdownMenu.onMenuSelectItem);
});

$(document).bind('click', function(event){
    dropdownMenu.onDocumentClick(event);
});

/* Regole genercihec JS e JQuery per la disposizione della stelline */
var reviewStars = {

    // Indica il numero massimo di 'stelline' dedicate al rating di una review
    totalStars : 5,

    /* Funzione utilizzata per posizionare una 'stellina' nell'apposito container (<ul>)*/
    placeStar : function(containerElement, starElement){
        containerElement.append(starElement);
    },

    /* Funzione utilizzata per accendere la 'stellina' ovvero mostrare la stellina accesa = gialla */
    activateStar: function(starElement){
        starElement.addClass("star-on");
    },

    /* Funzione utilizzata per creare l'elemento che rappresenterà la 'stellina' */
    createStar : function(){
        return $(document.createElement('li'));
    },

    /* Funzione che si occupa di disporre su una riga tutte le 'stelline' che rappresentano il rating di una review
       Il numero di 'stelline' accese o spente varia in base al parametro passato in input */
    placeStarsToRow : function(activeStarsNumber, containerElement){
        containerElement.empty();
        for(starCount=0; starCount<reviewStars.totalStars; starCount++){
            var starElement = reviewStars.createStar();
            reviewStars.placeStar(containerElement, starElement);
            if(starCount < activeStarsNumber){
                reviewStars.activateStar(starElement);
            }
        }
    },

    /* Funzione che si occupa di tradurre il raking in stelline
     * NOTA: se raking / 2 è maggiore di 0.5 approssima all'intero superiore, altrimenti all'intero inferiore*/
    rankingToActiveStars : function(ranking){
        return Math.round(ranking / 2);
    },

    /* Funzione ch che si occupa di mostrare le 'stelline' in base al ranking */
    setStars : function(ranking, starContainer){
        var activeStars = reviewStars.rankingToActiveStars(ranking);
        reviewStars.placeStarsToRow(activeStars, starContainer);
    }

};

var generic = {

    /* Funzione che si occupa di verficare se un certo oggetto è un oggetto JSON oppure no*/
    isJSONData : function(data){
        // Prende i dati e gli trasforma in plainText / Stringa
        if(typeof data !== 'string'){
            data = JSON.stringify(data);
        }

        // Prova, facendo il parsing JSON, a vedere se sono dati JSON oppure no
        try{
            jQuery.parseJSON(data);
            return true;
        }catch(parsingException){
            return false;
        }
    },

    /* Funzione che si occupa di decidere se scrivere recensione o recensioni */
    getReviewText : function(reviewCount){
        if(reviewCount === 1){
            return "recensione";
        }else{
            return "recensioni";
        }
    }

};

/*
 * Template generico per lo script paginatione per la twbs pagination
 *
 * Parametri da impostare prima dell'utilizzo:
 *      - totalPages: numero totale di pagine con i risultati
 *      - onPageClick: funzione da eseguire al cambio di pagina. Deve essere una funzione function(event, page)
 *
 * Documentazione plugin: http://esimakin.github.io/twbs-pagination/
 * */
var pagination = {
    totalPages: null,
    visiblePages: 5,
    first: null,
    prev: "Pag. prec",
    next: "Pag. succ",
    last: null,
    onPageClick: null,
    initiateStartPageClick: false,
    hideOnlyOnePage : true
};
