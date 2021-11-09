(function($) {
    "use strict";

    $(document).ready(function() {

        let pathValue = window.location.pathname;
        const path2 = window.location.pathname;
        let value;

        pathValue = pathValue.split('.').slice(0, -1).join('.');
        if (pathValue.split("/")[1] == 'content'){
            value = pathValue;
        }
        else {
            value = "/content/techem" + path2;
        }
        const finalPath = value;

        $.get("/eu/techem/hreflang", { pathReq: finalPath})
         .done(function (hreflangData){
            $('head').append(hreflangData);
        });


    });
}(jQuery));