/*
    Created on : 14-aug-2017
    Author     : Alessio Paternoster, Giuliani Daniele
*/
var slideshowFunctions = {

    // Indica l'immagine attualmente 'in mostra'. Di default è la prima.
    slideshowIndex : 1,

    /* Funzione che si occupa di mostrare una determinata immagine (individuata dall'indice)
       e di nascondere le altre -> è il getore vero e proprio dello slidehow. */
    showImage : function(){
        var images = $(".slideshow");
        var numberOfImages = images.length;

        if(slideshowFunctions.slideshowIndex > numberOfImages){
            slideshowFunctions.slideshowIndex = 1;
        }else if(slideshowFunctions.slideshowIndex < 1){
            slideshowFunctions.slideshowIndex = numberOfImages;
        }

        images.css({'display' : 'none'});
        images.eq(slideshowFunctions.slideshowIndex-1).css({'display' : 'block'});
    },

    /* Funzione utilizzata per richiamare o/e mostrare l'immagine successiva */
    nextImage : function(){
        slideshowFunctions.slideshowIndex += 1;
        slideshowFunctions.showImage();
    },

    /* Funzione utilizzata per richiamare o/e mostrare l'immagine precedente */
    prevImage : function(){
        slideshowFunctions.slideshowIndex -= 1;
        slideshowFunctions.showImage();
    }

};

$(document).ready(function() {
    slideshowFunctions.showImage();
    $(".slideshow-nav-previous").click(slideshowFunctions.prevImage);
    $(".slideshow-nav-next").click(slideshowFunctions.nextImage);
});

