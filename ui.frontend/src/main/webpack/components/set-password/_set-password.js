(function($) {
    "use strict";

    $(document).ready(function() {

        if($("div[data-component-name='set-password']").length < 1) {
            return;
        }

        const form = $("#cmp-set-password__form");
        const passField = $("#cmp-set-password__pass");
        const passFieldConfirm = $("#cmp-set-password__confirm");
        const sbmtBtn = $("cmp-set-password__submit");
        const alertPattern = $("#cmp-set-password__status_error_pattern");
        const linkError = $("#cmp-set-password__status_error_link");
        const successDiv = $("#cmp-set-password__status_success");
        const loginBtn = $("#cmp-set-password__btn-goto-login");
        const pwRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$");
        const postPath = form.attr("action");

        /* Only show form preview for authors */
        if($("div[data-component-name='set-password']").hasClass("author-mode")) {
            form.show();
            return;
        }

        /* Validate token before showing form */
        $.post(postPath, { checkTkn: true } ).done(function() {
            form.show();
        }).fail(function() {
            linkError.show();
        });

        form.submit(function(e) {
            e.preventDefault();

            /* Validate password */
            if ((passField.val() !== passFieldConfirm.val()) || passField.val().length < 12 || !pwRegex.test(passField.val())) {
                alertPattern.show();
                return;
            } else {
                alertPattern.hide();
            }

            sbmtBtn.prop("disabled", true);

            /* Submit data to servlet */
            $.post(postPath, { password: passField.val() } ).done(function() {
                successDiv.show();
                loginBtn.show();
            }).fail(function() {
                linkError.show();
            }).always(function() {
                form.hide();
            });
        });
    });

}(jQuery));