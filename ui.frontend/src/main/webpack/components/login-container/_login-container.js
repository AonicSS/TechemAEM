(function($) {
    "use strict";

    const form = $("#login-form");

    if(form.length > 0 ){
        const error = $("#login-error-message");
        const postPath = form.attr("action");
        const linkURL = $("#linkForRedirect").data('link-url');

        form.submit(function(event) {
            event.preventDefault();
            $.post(postPath, $(this).serialize()).done(function() {
                        error.hide();
                        window.location.href=linkURL;
                     }).fail(function(jqXHR, textStatus) {
                        error.show();
                     });
        })
    }
}(jQuery));
