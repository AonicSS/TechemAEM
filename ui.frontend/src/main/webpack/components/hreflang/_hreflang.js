(function($) {
    "use strict";

    $(document).ready(function() {

        const field = document.getElementById("hreflang");
        var pathName = window.location.pathname;
        var arrayPath = pathName.split(".");
        var result;

        if (arrayPath[arrayPath.length - 1] == "html") {
            arrayPath[arrayPath.length - 1] = "hreflang.html";
        }
        else {
            arrayPath[arrayPath.length - 1] = arrayPath[arrayPath.length - 1] + "hreflang.html";
        }

        pathName = arrayPath.join(".");

        $.get(pathName)
         .done(function (hreflangData){
            result = hreflangData.split('<!--')[0];
            $('head').append(result);
        });


    });
}(jQuery));