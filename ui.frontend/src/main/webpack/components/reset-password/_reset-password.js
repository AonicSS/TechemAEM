(function($) {
    "use strict";

    $(document).ready(function() {

        if($("div[data-component-name='reset-password']").length < 1) {
            return;
        }

        const form = $("#cmp-reset-password__form");
        const emailField = $("#cmp-reset-password__email");
        const sbmtBtn = $("#cmp-reset-password__submit");
        const statusDivSuccess = $("#cmp-reset-password__status_success");
        const statusDivError = $("#cmp-reset-password__status_error");

        form.submit(function(e) {
            e.preventDefault();
            sbmtBtn.prop("disabled", true);

            $.post($(this).attr("action"), { email: emailField.val() } ).done(function() {
                statusDivSuccess.show();
                sbmtBtn.prop("disabled", false);
            }).fail(function() {
                statusDivError.show();
                sbmtBtn.prop("disabled", false);
            });
        });
    });

}(jQuery));